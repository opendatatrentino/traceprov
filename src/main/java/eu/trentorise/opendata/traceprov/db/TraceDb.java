package eu.trentorise.opendata.traceprov.db;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.NotFoundException;
import eu.trentorise.opendata.commons.OdtUtils;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.TraceProvs;
import eu.trentorise.opendata.traceprov.data.DataMap;
import eu.trentorise.opendata.traceprov.data.DataNode;
import eu.trentorise.opendata.traceprov.data.DataObject;
import eu.trentorise.opendata.traceprov.dcat.AFoafAgent;
import eu.trentorise.opendata.traceprov.dcat.FoafAgent;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException;
import eu.trentorise.opendata.traceprov.types.Type;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;
import eu.trentorise.opendata.traceprov.types.Types;

/**
 * Database of TraceProv. Allows storing foreign objects while
 * tracking their provenance. Follows a NOSQL philophy, where objects are wrapped in
 * {@link DataNode DataNodes} and indexing is manual.
 * 
 * Currently Serialization is done with Jackson. Object cloning with Kryo.
 *
 * NOTE: Current implementation is just a prototype and thus super inefficient.
 *
 * @author David Leoni
 */
// todo make interface
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class TraceDb {

    private static Logger LOG = Logger.getLogger(TraceDb.class.getSimpleName());

    public static final String IN_MEMORY_URL = "memory:TraceDb";
    public static final String TRACEDB_FILE = "TraceDb.json";

    private static final String FILE_PREFIX = "file://";

    public static final long TRACEDB_PUBLISHER_ID = 0L;
    public static final String TRACEDB_PUBLISHER_URI = TraceProvs.TRACEPROV_IRI + "/db/tracedb-publisher";

    /**
     * Note this one doesn't have id as it must be created first.
     */
    private static final DataNode INIT_TRACEDB_PUBLISHER = DataObject.builder()
	    .setRawValue(FoafAgent.builder()
		    .setName(Dict.of("TraceDb Default Publisher"))
		    .setUri(TRACEDB_PUBLISHER_URI))
	    .build();

    /**
     * TODO super prototype, naming is probably wrong
     */
    private static final ThreadLocal<TraceDb> dbPool = new ThreadLocal<TraceDb>() {
	@Override
	protected TraceDb initialValue() {
	    return new TraceDb();
	}
    };

    private Map<String, String> prefixes;

    private String dbUrl;

    private long idCounter;

    private TypeRegistry typeRegistry;

    private boolean initialized;

    /**
     * Maps {@code <publisherId, externalId>} pairs (i.e. 4,
     * "http://entitypedia.org/entities/123") to corresponding views. This is a
     * way to say TraceProv knows about entity 123 of publisher Entitypedia,
     * which has traceprov internal id = 4
     * 
     */
    private Table<Long, String, DataNode> storedValuesByUrl;

    /**
     * trace id -> DataNode object
     */
    private HashMap<Long, DataNode> storedValuesById;

    /**
     * A multimap type id -> traceprov internal ids of DataNode which are
     * instances of that type
     */
    private Multimap<String, Long> indexedValues;

    private Set<String> indexedTypes;

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
	this.indexedValues = HashMultimap.create();
	this.indexedTypes = new HashSet();
	this.prefixes = new HashMap();
	this.sameAsIds = new HashMap();
	this.idCounter = 0;
	this.typeRegistry = TypeRegistry.of();
    }

    /**
     * The publisher of objects created with Tracedb. TODO this should be
     * configurable.
     */
    public AFoafAgent readTracedbPublisher() {
	return (AFoafAgent) read(TRACEDB_PUBLISHER_ID).getRawValue();
    }

    /**
     * The publisher to use for objects with unknown publisher.
     */
    public AFoafAgent readUnknownPublisher() {
	return (AFoafAgent) read(TRACEDB_PUBLISHER_ID).getRawValue();
    }

    /**
     * Initializes db with default values.
     */
    private void init() {

	if (initialized) {
	    LOG.warning("Initializing database twice!");
	} else {
	    create(INIT_TRACEDB_PUBLISHER);
	}
	initialized = true;
	LOG.info("TraceDB " + getDbUrl() + " is now initialized.");
    }

    /**
     * Database will point to a folder in the local hard drive
     */
    private void init(String folderPath, TypeRegistry typeRegistry) {
	if (initialized) {
	    LOG.warning("Initializing database twice!");
	} else {
	    create(INIT_TRACEDB_PUBLISHER);
	}
	checkNotNull(folderPath);
	checkNotNull(typeRegistry);
	this.dbUrl = FILE_PREFIX + folderPath;
	this.typeRegistry = typeRegistry;
	initialized = true;
	LOG.info("TraceDB " + getDbUrl() + " is now initialized.");
    }

    /**
     * @throws IllegalStateException
     */
    void checkInitialized() {
	if (!initialized) {
	    throw new IllegalStateException("TraceDb was not properly initialized!");
	}
    }

    /**
     */
    public static TraceDb createInMemoryDb(TypeRegistry typeRegistry) {
	TraceDb ret = dbPool.get();
	ret.init("", typeRegistry);
	return ret;
    }

    /**
     * Indexes all objects which are instances of provided {@coder traceType}.
     * Successive put operations will maintain the index updated.
     * 
     * @param typeId
     */
    public void indexType(String typeId) {
	checkNotEmpty(typeId, "Invalid TraceType id!");
	typeRegistry.checkRegistered(typeId);
	Type type = typeRegistry.getType(typeId);
	for (Map.Entry<Long, DataNode> entry : this.storedValuesById.entrySet()) {
	    Object rawValue = entry.getValue().getRawValue();
	    if (type.isInstance(rawValue)) {
		indexedValues.put(typeId, entry.getKey());
	    }
	}

    }

    /**
     * Connects to a database on the local hard drive and returns it.
     *
     * @param folderpath
     *            The folder where the db is located
     * @param typeRegistry
     *            The type registry to use for type casting, serialization and
     *            deserialization. Normally it can be safely shared with other
     *            instances except when they reconfigure it - in this case other
     *            threads must not use the object mapper during reconfiguration.
     * @throws TraceProvNotFoundException
     *             if the database is not found
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
		throw new TraceProvException("Couldn't load TraceDB", ex);
	    }
	} else {
	    throw new TraceProvNotFoundException("Couldn't find any TraceDb database in folder " + folderpath);
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
	TraceDb traceDb = dbPool.get();
	traceDb.init(folderpath, typeRegistry);
	try {
	    FileWriter fw = new FileWriter(json);
	    typeRegistry.getObjectMapper().writeValue(fw, traceDb);
	} catch (Throwable tr) {
	    throw new TraceProvException("Error while writing odr db to disk!", tr);

	}

	LOG.info("Created TraceDb at " + folderpath);

	return traceDb;
    }

    /**
     * Stores the provided prefix and its expansion in the database.
     *
     * @param prefix
     *            Notice prefix is supposed to include the colon, like 'foaf:'
     * @param url
     *            i.e. http://www.w3.org/1999/02/22-rdf-syntax-ns#
     */
    public void putPrefix(String prefix, String url) {
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
    public boolean hasPrefix(String prefix) {
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
    public String getPrefix(String prefix) {
	checkNotEmpty(prefix, "URL prefix is invalid!");
	String pfx = prefixes.get(prefix);
	if (pfx == null) {
	    throw new TraceProvNotFoundException("Prefix not found!");
	} else {
	    return pfx;
	}
    }

    /**
     * Returns the expanded version of the provided URL, according to the stored
     * prefixes. If provided URL has an unknown prefix, the URL is returned as
     * it is.
     */
    public String expandUrl(String url) {
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
    public Map<String, String> getAllPrefixes() {
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
    public DataNode read(long viewId) {
	checkInitialized();
	checkArgument(viewId >= 0);
	DataNode ret = storedValuesById.get(viewId);
	if (ret == null) {
	    throw new ViewNotFoundException(
		    "Couldn't find view with traceprov internal id " + viewId);
	} else {
	    return ret;
	}
    }

    private DataNode readPublisherNode(DataNode dataNode) {

	String uri = dataNode.getMetadata().getPublisher().getUri();
	String normalizedUrl = normalizeUrl(uri);

	return read(normalizedUrl);
    }

    /**
     * Returns the eventual main view generated by TraceProv that might be
     * aliasing the provided view.
     *
     * @param viewId
     *            the TraceProv internal id of the view to retrieve.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    private DataNode readMainObject(long viewId) {
	checkInitialized();
	checkArgument(viewId >= 0);

	ImmutableList<Long> clique = readSameAsIds(viewId);

	if (!clique.isEmpty()) {

	    DataNode odrView = read(clique.get(0));

	    if (TRACEDB_PUBLISHER_ID == readPublisherNode(odrView).getId()) {
		return odrView;
	    }
	}
	throw new ViewNotFoundException(
		"Couldn't find view with internal traceprov id " + viewId);

    }

    /**
     * Returns the view with given url. First publisher context searched is
     * {@link #TRACEDB_PUBLISHER_ID}, then all others are tried sequentially. If
     * url is not in any context, an exception is thrown.
     *
     * @param url
     *            the url of the object to retrieve.
     * @return the object with given url or throws exception if not found.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    public DataNode read(String url) {
	String normalizedUrl = normalizeUrl(url);

	try {
	    return read(TRACEDB_PUBLISHER_ID, normalizedUrl);
	} catch (ViewNotFoundException ex1) {

	    for (Long publisherId : storedValuesByUrl.rowKeySet()) {
		DataNode candidate2 = storedValuesByUrl.get(publisherId, normalizedUrl);
		if (candidate2 != null) {
		    try {
			return readMainObject(candidate2.getId());
		    } catch (ViewNotFoundException ex2) {
			return candidate2;
		    }
		}
	    }
	    throw new ViewNotFoundException(
		    "Couldn't find view with url: " + url);
	}
    }

    /**
     * Returns the view with given url at given origin id. If not present, an
     * exception is thrown.
     * 
     * @param publisherId
     *            The traceprov internal id of the publisher that originated the
     *            context.
     * @param url
     *            the url of the object to retrieve.
     * @return the object with given url.
     * @throws eu.trentorise.opendata.traceprov.exceptions.ViewNotFoundException
     */
    public DataNode read(long publisherId, String url) {

	checkArgument(publisherId >= 0);
	checkNotEmpty(url, "Invalid url!");

	String normalizedUrl = normalizeUrl(url);

	DataNode ret = storedValuesByUrl.get(publisherId, normalizedUrl);
	if (ret == null) {
	    throw new ViewNotFoundException("Couldn't find view with url " + url
		    + " having publisher traceprov id " + publisherId);
	} else {
	    return ret;
	}
    }

    /**
     * @see #create(Iterable)
     */
    public List<DataNode> create(DataNode... dataNodes) {
	return create(Arrays.asList(dataNodes));
    }

    private void index(DataNode dataNode) {
	ImmutableList<Type> types = typeRegistry.getTypesFromInstance(dataNode.getRawValue());
	for (Type type : types) {
	    if (indexedTypes.contains(type.getId())) {
		indexedValues.put(type.getId(), idCounter);
	    }
	}
    }

    /**
     * Creates new views and return them. They all must have valid publisher and
     * refs.
     *
     * @return a new view with assigned id.
     */
    public List<DataNode> create(Iterable<DataNode> dataNodes) {
	checkInitialized();
	ImmutableList.Builder<DataNode> retb = ImmutableList.builder();

	for (DataNode dataNode : dataNodes) {
	    checkNotNull(dataNode);
	    checkArgument(dataNode.getId() < 0, "Tried to create view with non-negative id: %s", dataNode.getId());
	    checkArgument(!dataNode.getMetadata().getPublisher().equals(FoafAgent.of()),
		    "Tried to create datanode with invalid publisher, found one is empty!");
	    String publisherUri = dataNode.getMetadata().getPublisher().getUri();
	    checkNotEmpty(publisherUri,
		    "Tried to create datanode with publisher which has invalid uri!");
	    long publisherId = read(publisherUri).getId();

	    DataNode toCreate = dataNode.fromThis()
		    .setId(idCounter)
		    .build();
	    storedValuesById.put(idCounter, toCreate);
	    storedValuesByUrl.put(publisherId, dataNode.getRef().uri(), dataNode);
	    index(toCreate);
	    idCounter += 1;

	    retb.add(toCreate);
	}
	return retb.build();

    }

    /**
     * 
     * @see #update(Iterable)
     */
    public List<DataNode> update(DataNode... dataNodes) {
	return Arrays.asList(dataNodes);
    }

    /**
     * Writes provided dataNodes to their originId / foreign identifier slot.
     * This is a hard update, if possible just create a new view instead.
     *
     * @param view
     *            the view to update
     * @return the value previously associated with the originId / foreign
     *         identifier slot
     * @throws ViewNotFoundException
     */
    @Nullable
    public List<DataNode> update(Iterable<DataNode> dataNodes) {
	throw new UnsupportedOperationException("TODO implement me!");
    }

    /**
     *
     * Returns the ids of the views considered to be the same as the provided
     * one.
     */
    public ImmutableList<Long> readSameAsIds(long id) {
	checkInitialized();
	checkArgument(id >= 0);
	return ImmutableList.copyOf(sameAsIds.get(id));
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
    public ImmutableSet<Long> addSameAsIds(Iterable<Long> ids) {
	checkNotNull(ids);

	throw new UnsupportedOperationException("TODO IMPLEMENT ME");
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
     * Returns true if the stored views corresponding to the two provided ids
     * are equal using Java equality.
     *
     * @throws ViewNotFoundException
     *             if objects are not found.
     */
    public boolean shallowEqual(long viewId1, long viewId2) {
	DataNode view1 = read(viewId1);
	DataNode view2 = read(viewId2);
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
    public boolean isSynchronized(long originId, String externalId) {

	throw new UnsupportedOperationException("TODO IMPLEMENT ME!");
	// todo check odrity
	/*
	 * return (odrView.getTimestamp().isBefore(foreignView.getTimestamp())
	 * || odrView.getTimestamp().isEqual(foreignView.getTimestamp())); //&&
	 * odrView.controlledEquals(foreignView)
	 */
    }

    public static TraceDb getCurrentDb() {
	return dbPool.get();
    }

    public TypeRegistry getTypeRegistry() {
	return typeRegistry;
    }

    public void setTypeRegistry(TypeRegistry typeRegistry) {
	checkNotNull(typeRegistry);
	this.typeRegistry = typeRegistry;
    }
}
