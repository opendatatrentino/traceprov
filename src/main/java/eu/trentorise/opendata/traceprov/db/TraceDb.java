package eu.trentorise.opendata.traceprov.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;

import eu.trentorise.opendata.commons.NotFoundException;
import eu.trentorise.opendata.commons.OdtUtils;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;
import eu.trentorise.opendata.traceprov.TraceProvs;
import eu.trentorise.opendata.traceprov.data.DataNode;
import eu.trentorise.opendata.traceprov.data.DataObject;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;

/**
 * Database of TraceProv. Serialization is done with Jackson.
 *
 * Current implementation is just a prototype and thus super inefficient.
 *
 * @author David Leoni
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class TraceDb {

    public static final String IN_MEMORY_URL = "memory:TraceDb";
    public static final String TRACEDB_FILE = "TraceDb.json";

    private static final String FILE_PREFIX = "file://";

    public static final long TRACEDB_ORIGIN_ID = 0L;

    private static Logger LOG = Logger.getLogger(TraceDb.class.getSimpleName());

    private Map<String, String> prefixes;

    private String dbUrl;

    private long originIdCounter;

    private TypeRegistry typeRegistry;

    /**
     * Maps {@code <originId, externalId>} pairs (i.e.
     * 4, "http://entitypedia.org/entities/123") to corresponding views. This is
     * a way to say traceprov knows about entity 123 of entitypedia, which
     * has storedOrigin id = 4
     *
     * odr todo 0.3 horrible table, replace with proper LRU cache/db
     */
    private Table<Long, String, DataNode> storedValuesByUrl;

    /**
     * trace id -> Jacksonizable object
     */
    private HashMap<Long, DataNode> storedValuesById;

    /**
     * a map origin id -> urls under which that origin is known
     */
    private ImmutableListMultimap<Long, String> storedOrigins;

    /**
     * If there is a corresponding odr view of the view, its id will be the
     * first of the list. id -> id1, id2, id3 ...
     */
    private HashMap<Long, LinkedHashSet<Long>> sameAsIds;

    /**
     * Database with an in-memory db and default Jackson object mapper
     */
    private TraceDb() {
	this.dbUrl = IN_MEMORY_URL;
	this.storedValuesByUrl = HashBasedTable.create();
	this.storedValuesById = new HashMap();
	this.storedOrigins = ImmutableListMultimap.of();
	this.prefixes = new HashMap();
	this.sameAsIds = new HashMap();
	this.originIdCounter = 0;
	this.typeRegistry = TypeRegistry.of();
    }

    /**
     * Initializes db with default values.
     */
    private void init() {
	createOrigin(ImmutableList.of(TraceProvs.TRACEPROV_IRI));
    }

    /**
     * Database will point to a folder in the local hard drive
     */
    private TraceDb(String folderPath, TypeRegistry typeRegistry) {
	this();
	checkNotNull(folderPath);
	checkNotNull(typeRegistry);
	this.dbUrl = FILE_PREFIX + folderPath;
	this.typeRegistry = typeRegistry;
    }

    /**
     */
    public static TraceDb createInMemoryDb(TypeRegistry typeRegistry) {
	TraceDb ret = new TraceDb("", typeRegistry);
	ret.init();
	return ret;
    }

    /**
     * Connects to a database on the local hard drive and returns it.
     *
     * @param folderpath
     *            The folder where the db is located
     * @param objectMapper
     *            The object mapper to use for serialization and
     *            deserialization. Normally it can be safely shared with other
     *            instances except when they reconfigure it - in this case other
     *            threads must not use the object mapper during reconfiguration.
     * @throws TraceProvNotFoundException
     *             if the database is not found;
     */
    public static TraceDb connectToDb(String folderpath, TypeRegistry typeRegistry) {
	LOG.info("Connecting to TraceDb at " + folderpath + "   ...");
	checkNotEmpty(folderpath, "path to db folder is invalid!");
	checkNotNull(typeRegistry, "Type registry must not be null!");

	File json = new File(folderpath + File.pathSeparator + "");
	if (json.exists()) {
	    try {
		TraceDb ret = typeRegistry.getObjectMapper().readValue(json, TraceDb.class);
		ret.typeRegistry = typeRegistry;
		LOG.info("Connected to TraceDb at " + folderpath);
		return ret;
	    } catch (IOException ex) {
		throw new TraceProvException("Couldn't load the odr database", ex);
	    }
	} else {
	    throw new NotFoundException("Couldn't find any TraceDb database in folder " + folderpath);
	}
    }

    private String folderPath() {
	return dbUrl.substring(FILE_PREFIX.length());
    }

    /**
     * Deletes the database from disk. If database is not present silently exits
     */
    public void drop() {

	LOG.info("Dropping TraceDb... at " + dbUrl);

	if (IN_MEMORY_URL.equals(dbUrl)) {
	    return;
	}

	File dir = new File(folderPath());

	if (!existsDb(dir.getAbsolutePath())) {
	    throw new IllegalStateException(
		    "Tried to drop a directory which doesn't look like a TraceDb folder! " + dir.getAbsolutePath());
	}

	try {
	    FileUtils.deleteDirectory(dir);
	} catch (Exception ex) {
	    throw new TraceProvException("Couldn't delete odr db file: " + dir, ex);
	}

	LOG.info("Dropped TraceDb at " + dbUrl);
    }

    private ObjectMapper om() {
	return typeRegistry.getObjectMapper();
    }

    /**
     * Flushes cache to dbURL
     */
    public void flush() {

	LOG.info("Flushing TraceDb....");

	if (IN_MEMORY_URL.equals(dbUrl)) {
	    throw new IllegalStateException("In memory database can't be flushed!");
	}

	OutputStream file = null;
	ObjectOutput oos = null;

	File outputFile = new File(dbUrl, TRACEDB_FILE);
	try {
	    FileWriter w = new FileWriter(outputFile);
	    om().writeValue(w, this);

	} catch (Exception ex) {
	    throw new TraceProvException("Couldn't write to file: " + outputFile.getAbsolutePath(), ex);
	} finally {
	    try {
		if (oos != null) {
		    oos.close();
		}

		if (file != null) {
		    file.close();
		}
	    } catch (IOException ex) {
		LOG.log(Level.SEVERE, "Error while closing TraceDb file", ex);
	    }

	}

	LOG.info("Done flushing Odr db.");

    }

    /**
     * Returns true if there already an TraceDb in provided folder.
     */
    public static boolean existsDb(String folderPath) {
	return new File(folderPath + File.pathSeparator + TRACEDB_FILE).exists();
    }

    /**
     * Creates a database on the local hard drive and returns it.
     *
     * @param folderpath
     *            The folder where the db is located
     * @throws IllegalStateException
     *             if folderpath is non empty
     */
    public static TraceDb createDb(String folderpath, TypeRegistry typeRegistry) {

	LOG.info("Creating TraceDb at " + folderpath + "  ...");

	checkNotEmpty(folderpath, "path to db folder is invalid!");
	checkNotNull(typeRegistry);

	File dir = new File(folderpath);
	File json = new File(folderpath + File.pathSeparator + "");
	if (dir.exists()) {
	    if ((dir.isFile())) {
		throw new IllegalStateException(
			"Error while creating an TraceDb, target path should be a directory, found a file instead! "
				+ folderpath);
	    }

	    if (dir.list().length > 0) {
		throw new IllegalStateException(
			"Error while creating an TraceDb, target directory is not empty! " + folderpath);
	    }
	}
	TraceDb TraceDb = new TraceDb(folderpath, typeRegistry);
	try {
	    FileWriter fw = new FileWriter(json);
	    typeRegistry.getObjectMapper().writeValue(fw, TraceDb);
	} catch (Throwable tr) {
	    throw new TraceProvException("Error while writing odr db to disk!", tr);

	}

	TraceDb.init();

	LOG.info("Created TraceDb at " + folderpath);

	return TraceDb;
    }

    /**
     * Stores the provided prefix and its expansion in the database.
     *
     * @param prefix
     *            Notice prefix is supposed to include the colon, like 'foaf:'
     * @param url
     *            i.e. http://www.w3.org/1999/02/22-rdf-syntax-ns#
     */
    public synchronized void putPrefix(String prefix, String url) {
	checkNotEmpty(prefix, "Url prefix is invalid!");
	checkNotEmpty(url, "Url is invalid!");
	prefixes.put(prefix, url);
    }

    /**
     * Returns if provided prefix has associated an expansion in the database.
     *
     * @param prefix
     *            Notice prefix is supposed to include the colon, like 'foaf:'
     */
    public synchronized boolean hasPrefix(String prefix) {
	checkNotEmpty(prefix, "URL prefix is invalid!");
	String pfx = prefixes.get(prefix);
	return pfx != null;
    }

    /**
     * Returns the prefix expansion, if present in the db.
     *
     * @throws NotFoundException
     *             if prefix doesn't have associated any expansion in the db
     */
    public synchronized String getPrefix(String prefix) {
	checkNotEmpty(prefix, "URL prefix is invalid!");
	String pfx = prefixes.get(prefix);
	if (pfx == null) {
	    throw new NotFoundException("Prefix not found!");
	} else {
	    return pfx;
	}
    }

    /**
     * Returns the expanded version of the provided URL, according to the stored
     * prefixes. If provided URL has an unknown prefix, the URL is returned as
     * it is.
     */
    public synchronized String expandUrl(String url) {
	checkNotEmpty(url, "URL is invalid!");

	for (String prefix : prefixes.keySet()) {
	    if (url.startsWith(prefix)) {
		return prefixes.get(prefix) + url.substring(prefix.length());
	    }
	}
	return url;
    }

    /**
     * Expands the provided url and removes eventual slashes / at the end
     *
     * @return the normalized url
     */
    public String normalizeUrl(String url) {
	checkNotEmpty(url, "Invalid url!");
	// odr todo 0.3 this is rough....
	return OdtUtils.removeTrailingSlash(expandUrl(url));
    }

    /**
     * Returns an immutable map with [prefix, expanded url] pairs
     */
    public synchronized Map<String, String> getAllPrefixes() {
	return ImmutableMap.copyOf(prefixes);
    }

    /**
     * Returns the url where the db is located. If db is inmemory,
     * {@link #IN_MEMORY_URL} is returned.
     */
    public String getDbUrl() {
	return dbUrl;
    }

    /**
     * Returns the object with given id.
     *
     * @param viewId
     *            the TraceProv internal id of the view to retrieve.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    public synchronized DataNode read(long viewId) {
	checkArgument(viewId >= 0);
	DataNode ret = storedValuesById.get(viewId);
	if (ret == null) {
	    throw new ViewNotFoundException(
		    "Couldn't find view with traceprov internal id " + viewId);
	} else {
	    return ret;
	}
    }

    /**
     * Returns the eventual main view generated by odr that might be aliasing
     * the provided view.
     *
     * @param viewId
     *            the odr internal id of the view to retrieve.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    private DataNode readOdrView(long viewId) {
	checkArgument(viewId >= 0);

	ImmutableList<Long> clique = readSameAsIds(viewId);

	if (!clique.isEmpty()) {

	    DataNode odrView = read(clique.get(0));

	    if (odrView != null && TRACEDB_ORIGIN_ID == odrView.getOriginId()) {
		return odrView;
	    }
	}
	throw new ViewNotFoundException(
		"Couldn't find view with internal traceprov id " + viewId + " of class " + clazz.getName());

    }

    /**
     * Returns the eventual main view generated by odr that might be aliasing
     * the provided view.
     *
     * @param originId
     *            the id of the origin server of the view to retrieve
     * @param externalId
     *            the id in the external server of the object to retrieve.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    private <C extends View> C readOdrView(Class<C> clazz, long originId, String externalId) {
	checkNotNull(clazz);
	checkArgument(originId >= 0);
	checkNotEmpty(externalId, "Invalid external id!");

	ImmutableList<Long> clique = readSameAsIds(clazz, originId, externalId);

	if (!clique.isEmpty()) {

	    C odrView = read(clazz, clique.get(0));

	    if (odrView != null && TRACEDB_ORIGIN_ID == odrView.getOriginId()) {
		return odrView;
	    }
	}
	throw new ViewNotFoundException(clazz, originId, externalId);
    }

    /**
     * Returns the view with given url. First origin context searched is
     * {@link #TRACEDB_ORIGIN_ID}, then all others are tried sequentially. If
     * url is not in any context, an exception is thrown.
     *
     * @param url
     *            the url of the object to retrieve.
     * @return the object with given url or null if not found.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    public synchronized <C> DataObject<C> read(String url) {
	String normalizedUrl = normalizeUrl(url);

	DataNode candidate1 = read(TRACEDB_ORIGIN_ID, normalizedUrl);

	if (candidate1 == null) {
	    
	    for (Long originId : storedValuesByUrl.rowKeySet()) {
		DataNode candidate2 = storedValuesByUrl.get(originId, normalizedUrl);
		if (candidate2 != null) {
		    C candidate3 = readOdrView(candidate2.getId());
		    if (candidate3 != null) {
			return candidate3;
		    }

		    return candidate2;
		}
	    }
	    throw new ViewNotFoundException("Couldn't find view with url: " + url + " of class: " + clazz.getName());
	} else {
	    return candidate1;
	}
    }

    /**
     * Returns the view with given url at given origin id. If not present, an
     * exception is thrown.
     *
     * @param originUrl
     *            The url of the context that originated the context.
     * @param url
     *            the url of the object to retrieve.
     * @return the object with given url.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    public synchronized <C extends View> C read(Class<C> clazz, long originId, String url) {
	checkNotNull(clazz);
	checkArgument(originId >= 0);
	checkNotEmpty(url, "Invalid url!");

	checkRegistered(clazz);

	String normalizedUrl = normalizeUrl(url);

	C ret = (C) storedValuesByUrl.get(clazz.getName()).get(originId, normalizedUrl);
	if (ret == null) {
	    throw new ViewNotFoundException("Couldn't find view with url " + url + " and class " + clazz.getName()
		    + " having origin server id " + originId);
	} else {
	    return ret;
	}

    }

    public synchronized List<String> readOriginUrls(long originId) {
	return storedOrigins.get(originId);
    }

    /**
     * Creates a new view.
     *
     * @return a new view with assigned id.
     */
    public synchronized DataNode create(DataNode view) {
	checkNotNull(view);
	checkRegistered(view.getClass());
	checkArgument(view.getId() < 0, "Tried to create view with non-negative id: %s", view.getId());
	throw new UnsupportedOperationException("TODO IMPLEMENT ME!");
    }

    /**
     * Writes provided view to its originId / foreign identifier slot. This is a
     * hard update, if possible just create a new view instead.
     *
     * @param view
     *            the view to update
     * @return the value previously associated with the originId / foreign
     *         identifier slot
     * @throws ViewNotFoundException
     */
    @Nullable
    public synchronized View update(View view) {
	checkNotNull(view);
	checkRegistered(view.getClass());

	View existingView = read(view.getClass(), view.getId());

	String normalizedUrl = normalizeUrl(view.getUrl());
	checkArgument(normalizedUrl.equals(view.getUrl()),
		"Tried to insert view with non-normalized url: %s  \nExpected url: %s", view.getUrl(), normalizedUrl);
	checkArgument(readOriginUrls(view.getOriginId()).size() > 0, "There is no stored origin with id %s",
		view.getOriginId());

	Table<Long, String, ? super View> table = storedValuesByUrl.get(view.getClass().getName());

	return (View) table.put(view.getOriginId(), view.getUrl(), view);
    }

    /**
     *
     * Returns the ids of the views considered to be the same as the provided
     * one.
     */
    public synchronized ImmutableList<Long> readSameAsIds(Class<? extends View> clazz, long originId, String url) {
	checkRegistered(clazz);
	checkArgument(originId >= 0);
	checkNotEmpty(url, "invalid url!");

	throw new UnsupportedOperationException("TODO IMPLEMENT ME");
    }

    /**
     *
     * Returns the ids of the views considered to be the same as the provided
     * one.
     */
    public synchronized ImmutableList<Long> readSameAsIds(long id) {
	checkArgument(id >= 0);
	return ImmutableList.copyOf(sameAsIds.get(id));
    }

    public synchronized ImmutableList<Long> updateSameAsIds(long originId, String url) {
	checkArgument(originId >= 0);
	checkNotEmpty(url, "invalid url!");
	throw new UnsupportedOperationException("TODO IMPLEMENT ME");
    }

    /**
     * States all provided view ids are 'same as'. If they are already same as
     * with some other id outside the provided ones, the old same as relation is
     * enlarged to include the new relations.
     *
     * @param clazz
     * @param ids
     *            the sameas ids
     * @return the old list of same as
     * @throws IllegalStateException
     *             if there are no corresponding views for the provided ids
     */
    public synchronized ImmutableSet<Long> addSameAsIds(Class<? extends View> clazz, Iterable<Long> ids) {
	checkRegistered(clazz);
	checkNotNull(ids);

	throw new UnsupportedOperationException("TODO IMPLEMENT ME");
    }

    /**
     * Registers a class in the db.
     * 
     * @param clazz
     */
    public void registerClass(Class clazz) {
	checkNotNull(clazz);
	checkState(isRegistered(clazz), "Class %s is already registered in %s !", clazz.getName(),
		TraceDb.class.getSimpleName());	
    }



    /**
     * Returns the controller status of the controller associated to the clique
     * to which the provided view id belongs
     *
     * @param viewId
     *            the id of a view, which is not necessarily a view generated by
     *            odr.
     * @param originId
     *            the id of the server of origin
     */
    public ControllerStatus controllerStatus(long viewId, long originId) {
	checkArgument(viewId >= 0);
	checkArgument(originId >= 0);
	// The view is NEW if it is not in any equivalence relation with objects
	// from the domain.
	throw new UnsupportedOperationException("TODO IMPLEMENT ME");
    }

    /**
     * Creates a new origin (i.e. something from which we can get data, i.e. a
     * server, organization, ... ) with the given url aliases (first url is the
     * prefereed one).
     *
     * @return the id of the inserted origin.
     * @throws IllegalStateException
     *             in case any of the urls is already associated to an origin.
     */
    public synchronized long createOrigin(Iterable<String> urls) {

	for (String url : urls) {
	    String normalizedUrl = normalizeUrl(url);
	    if (storedOrigins.containsValue(normalizedUrl)) {
		ImmutableList<Long> inverse = storedOrigins.inverse().get(url);
		throw new IllegalStateException("Url " + url + " is already associated to origin ids: " + inverse);
	    }
	}
	long ret = originIdCounter;
	storedOrigins = ImmutableListMultimap.<Long, String> builder().putAll(storedOrigins).putAll(ret, urls).build();

	originIdCounter += 1;

	return ret;
    }

    /**
     * Returns true if the stored views corresponding to the two provided ids
     * are equal using Java equality.
     *
     * @throws ViewNotFoundException
     *             if objects are not found.
     */
    public boolean shallowEqual(long viewId1, long viewId2) {
	View view1 = (View) read(clazz, viewId1);
	View view2 = (View) read(clazz, viewId2);
	throw new UnsupportedOperationException("TODO IMPLEMENT ME!");
	// return view1.controlledEquals(view2);
    }

    /**
     * Returns true if the stored objects corresponding to the two provided ids
     * are equal using Java equality.
     *
     * @throws ViewNotFoundException
     */
    public boolean deepEqual(long objId1, long objId2) {
	throw new UnsupportedOperationException("TODO Implement me!!");
    }

    /**
     * Returns true if first view is same as second view. Notice two views may
     * be 'same as' even if they are structually different or represent a new
     * and an old view of the same object.
     *
     * @throws ViewNotFoundException
     */
    public boolean sameAs(long viewId1, long viewId2) {

	read(viewId1);
	read(viewId2);

	LinkedHashSet<Long> sames = sameAsIds.get(viewId1);

	return sames.contains(viewId2);
    }

    /**
     * @throws ViewNotFoundException
     */
    public boolean isSynchronized(Class clazz, long originId, String externalId) {
	DataNode odrView =  readOdrView(originId, externalId);
	DataNode foreignView =  read(originId, externalId);

	throw new UnsupportedOperationException("TODO IMPLEMENT ME!");
	// todo check odrity
	/*
	 * return (odrView.getTimestamp().isBefore(foreignView.getTimestamp())
	 * || odrView.getTimestamp().isEqual(foreignView.getTimestamp())); //&&
	 * odrView.controlledEquals(foreignView)
	 */

    }

}
