package eu.trentorise.opendata.traceprov.db;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.collect.Table;

import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.NotFoundException;
import eu.trentorise.opendata.commons.TodUtils;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.TraceProvs;
import eu.trentorise.opendata.traceprov.data.TraceData;
import eu.trentorise.opendata.traceprov.data.TraceDatas;
import eu.trentorise.opendata.traceprov.data.DataObject;
import eu.trentorise.opendata.traceprov.data.NodeMetadata;
import eu.trentorise.opendata.traceprov.dcat.AFoafAgent;
import eu.trentorise.opendata.traceprov.dcat.FoafAgent;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.exceptions.AmbiguousUrlException;
import eu.trentorise.opendata.traceprov.exceptions.DataNotFoundException;
import eu.trentorise.opendata.traceprov.exceptions.IncomparableVersionsException;
import eu.trentorise.opendata.traceprov.types.TraceType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

/**
 * Database of TraceProv. Allows storing foreign objects while tracking their
 * provenance. Follows a NOSQL philophy, where objects are wrapped in
 * {@link TraceData DataNodes} and indexing is manual.
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

    public static final String IN_MEMORY_PREFIX = "memory://";
    public static final String TRACEDB_FILE = "tracedb.json";

    private static final String FILE_PREFIX = "file://";

    public static final long TRACEDB_PUBLISHER_ID = 0L;
    public static final String TRACEDB_PUBLISHER_URI = TraceProvs.TRACEPROV_IRI + "/db/tracedb-publisher";

    private static final AFoafAgent TRACEDB_PUBLISHER = FoafAgent.builder()
                                                                 .setName(Dict.of("TraceDb Default Publisher"))
                                                                 .setUri(TRACEDB_PUBLISHER_URI)
                                                                 .build();

    private static final NodeMetadata TRACEDB_DEFAULT_NODE_METADATA = NodeMetadata.builder()
                                                                                  .setPublisherId(TRACEDB_PUBLISHER_ID)
                                                                                  .build();

    /**
     * Note this one doesn't have id as it must be created first (although we
     * know id will be 0). Note also this one self publishes itself.
     */
    private static final TraceData INIT_TRACEDB_PUBLISHER = DataObject.builder()
                                                                      .setRef(Ref.ofDocumentId(TRACEDB_PUBLISHER_URI))
                                                                      .setRawValue(TRACEDB_PUBLISHER)
                                                                      .setMetadata(TRACEDB_DEFAULT_NODE_METADATA)
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

    /**
     * Not called init() yet
     */
    private static int INIT_LEVEL_0 = 0;

    /**
     * just called init(), but not populated with default values.
     */
    private static int INIT_LEVEL_1 = 1;

    /**
     * populated with default values.
     */
    private static int INIT_LEVEL_2 = 2;

    /**
     * Finished initialization
     */
    private static int INIT_LEVEL_3 = 3;

    private int initLevel;

    /**
     * Maps {@code <publisherId, externalId>} pairs (i.e. 4,
     * "http://entitypedia.org/entities/123") to corresponding views. This is a
     * way to say TraceProv knows about entity 123 of publisher Entitypedia,
     * which has traceprov internal id = 4
     * 
     */
    private Table<Long, String, ArrayList<TraceData>> storedValuesByUrl;

    /**
     * trace id -> DataNode object
     */
    private HashMap<Long, TraceData> storedValuesById;

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
    private LinkedHashMultimap<Long, Long> sameAsIds;

    /**
     * Database with an in-memory db and default Jackson object mapper
     */
    private TraceDb() {

        this.dbUrl = IN_MEMORY_PREFIX + "/tracedb/defaultdb";
        this.storedValuesByUrl = HashBasedTable.create();
        this.storedValuesById = new HashMap();
        this.indexedValues = HashMultimap.create();
        this.indexedTypes = new HashSet();
        this.prefixes = new HashMap();
        this.sameAsIds = LinkedHashMultimap.create();
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
        init(IN_MEMORY_PREFIX + "tracedb/defaultdb", TypeRegistry.of());
    }

    /**
     * Database will point to a folder in the local hard drive, This method WILL
     * NOT write in the folder.
     */
    private void init(String dbUrl, TypeRegistry typeRegistry) {
        checkNotEmpty(dbUrl, "Invalid db Url!");
        checkArgument(dbUrl.startsWith(IN_MEMORY_PREFIX) || dbUrl.startsWith(FILE_PREFIX),
                "Only supported db url prefixes are " + IN_MEMORY_PREFIX + " and " + FILE_PREFIX
                        + ", found instead url " + dbUrl);

        if (initLevel > 0) {
            LOG.warning("Tried to initialize database twice!");
        } else {
            initLevel = INIT_LEVEL_1;
            this.dbUrl = dbUrl;
            this.typeRegistry = typeRegistry;
            createPublisher(INIT_TRACEDB_PUBLISHER);
            initLevel = INIT_LEVEL_2;
            initLevel = INIT_LEVEL_3;
            LOG.info("TraceDB " + getDbUrl() + " is now initialized.");
        }
    }

    /**
     * Tests init() was called (db may still not have default values)
     * 
     * @throws IllegalStateException
     */
    void checkInitialized(int level) {
        if (initLevel < level) {
            throw new IllegalStateException("TraceDb was not properly initialized!");
        }
    }

    /**
     * Tests db is fully initialized
     * 
     * @throws IllegalStateException
     */
    void checkInitialized() {
        if (initLevel < INIT_LEVEL_3) {
            throw new IllegalStateException("TraceDb was not properly initialized!");
        }
    }

    /**
     * @param dbId
     *            a name uniquely identifying the db, like mycompany.org/superdb
     *            resulting Url of the db will be memory://mycompany.org/superdb
     */
    public static TraceDb createInMemoryDb(String dbId, TypeRegistry typeRegistry) {
        TraceDb newDb = new TraceDb();
        newDb.init(IN_MEMORY_PREFIX + dbId, typeRegistry);

        TraceDb curDb = dbPool.get();
        if (curDb.initLevel == INIT_LEVEL_0) {
            LOG.info("Found no db set in current thread, setting it to " + newDb.getDbUrl());
            dbPool.set(newDb);
        } else {
            LOG.info(
                    "(Found another db set in current thread, to replace it, you need to manually call TraceDb.setCurrentDb())");
        }
        return newDb;
    }

    /**
     * Indexes all objects which are instances of provided {@code traceType}.
     * Successive put operations will maintain the index updated.
     * 
     * @param typeId
     */
    public void indexType(String typeId) {
        checkNotEmpty(typeId, "Invalid TraceType id!");
        typeRegistry.checkRegistered(typeId);
        TraceType type = typeRegistry.get(typeId);
        for (Map.Entry<Long, TraceData> entry : this.storedValuesById.entrySet()) {
            Object rawValue = entry.getValue()
                                   .getRawValue();
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

        File json = new File(folderpath + File.separator + TRACEDB_FILE);
        if (json.exists()) {
            try {
                TraceDb ret = typeRegistry.getObjectMapper()
                                          .readValue(json, TraceDb.class);
                ret.typeRegistry = typeRegistry;
                ret.initLevel = INIT_LEVEL_3;
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

        TraceDb db = dbPool.get();
        if (db.getDbUrl()
              .equals(this.getDbUrl())) {
            dbPool.set(new TraceDb());
            // todo what about other threads?
            LOG.info("Removed db from local thread.");
        }

        if (dbUrl.startsWith(IN_MEMORY_PREFIX)) {
            return;
        }

        Path dir;
        try {
            dir = Paths.get(new URI(getDbUrl()));
        } catch (URISyntaxException ex) {
            throw new TraceProvException("Some error occurred.", ex);
        }

        if (!existsDb(dir.toString())) {
            throw new IllegalStateException(
                    "Tried to drop a directory which doesn't look like a TraceDb folder! " + dir.toString());
        }

        try {
            FileUtils.deleteDirectory(new File(dir.toString()));
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

        if (dbUrl.startsWith(IN_MEMORY_PREFIX)) {
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

        LOG.info("Done flushing TraceDb at " + getDbUrl());

    }

    /**
     * Returns true if there already a TraceDb in provided folder.
     */
    public static boolean existsDb(String folderPath) {

        return new File(folderPath + File.separator + TRACEDB_FILE).exists();
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
        File jsonFile = new File(folderpath + File.separator + TRACEDB_FILE);
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

        TraceDb newDb = new TraceDb();
        Path path = Paths.get(folderpath);
        newDb.init(path.toUri()
                       .toString(),
                typeRegistry);
        try {
            FileWriter fw = new FileWriter(jsonFile);
            typeRegistry.getObjectMapper()
                        .writeValue(fw, newDb);
        } catch (Throwable tr) {
            throw new TraceProvException("Error while writing trace db to disk!", tr);
        }

        LOG.info("Created TraceDb at " + folderpath);

        TraceDb curDb = dbPool.get();
        if (curDb.initLevel == INIT_LEVEL_0) {
            LOG.info("Found no default db set for this thread, setting it to newly created db.");
            dbPool.set(newDb);
        } else {
            LOG.info(
                    "(Found another db set in current thread, to replace it, you need to manually call TraceDb.setCurrentDb())");
        }

        return newDb;
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
        return TodUtils.removeTrailingSlash(expandUrl(url));
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
     * Returns the datanode with given id.
     *
     * @param datanodeId
     *            the TraceProv internal id of the datanode to retrieve.
     * @throws eu.trentorise.opendata.traceprov.exceptions.DataNotFoundException
     */
    public TraceData read(long datanodeId) {
        return read(Arrays.asList(datanodeId)).get(0);
    }

    /**
     * Read all the datanodes with given ids
     * 
     * @throws eu.trentorise.opendata.traceprov.exceptions.DataNotFoundException
     *             if any of the ids is not found.
     */
    public List<TraceData> read(Iterable<Long> datanodeIds) {
        checkInitialized(INIT_LEVEL_0);
        List<TraceData> ret = new ArrayList();
        for (Long datanodeId : datanodeIds) {
            checkNotNull(datanodeId);
            checkArgument(datanodeId >= 0);
            TraceData cand = storedValuesById.get(datanodeId);
            if (cand == null) {
                throw new DataNotFoundException("Couldn't find view with traceprov internal id " + datanodeId);
            } else {
                ret.add(cand);
            }
        }
        return ret;

    }

    protected boolean selfPublished(TraceData mainTraceView) {
        return mainTraceView.getId() == mainTraceView.getMetadata()
                                                     .getPublisherId();
    }

    /**
     * Returns the main view of the sameas clique where {@code datanodeId}
     * belongs to.
     *
     * @param datanodeId
     *            the TraceProv internal id of the view to retrieve.
     * @throws eu.trentorise.opendata.traceprov.exceptions.DataNotFoundException
     */
    public TraceData readMainObject(long datanodeId) {
        checkInitialized();
        checkArgument(datanodeId >= 0);

        List<Long> clique = readSameAsIds(datanodeId);

        if (!clique.isEmpty()) {
            return read(clique.get(0));
        }
        throw new DataNotFoundException("Couldn't find view with internal traceprov id " + datanodeId);

    }

    /**
     * Searches sames cliques for a data object with provided url as external
     * id. If found, returns the main view of the sameas clique. If url is in
     * more than one clique, or in no clique, an exception is thrown.
     *
     * @param url
     *            the url of the object to retrieve.
     * @return the data object with given url or throws exception if not found.
     * @throws eu.trentorise.opendata.traceprov.exceptions.DataNotFoundException
     *             if no clique is found.
     * @throws eu.trentorise.opendata.traceprov.exceptions.AmbiguousUrlException
     *             if two cliques are found.
     */
    public TraceData read(String url) {
        String normalizedUrl = normalizeUrl(url);

        // publisherId -> many original urls
        Map<Long, ArrayList<TraceData>> publisherIdToData = this.storedValuesByUrl.column(normalizedUrl);

        if (publisherIdToData.isEmpty()) {
            throw new DataNotFoundException("Couldn't find any stored object with url " + url);
        }

        Set<Long> ids = new HashSet();
        for (List<TraceData> tds : publisherIdToData.values()) {
            ids.addAll(TraceDatas.ids(tds));
        }

        if (ids.isEmpty()) {
            throw new IllegalStateException("Values shouldn't be empty for normalizedUrl " + normalizedUrl);
        }

        if (!sameAs(ids)) {
            throw new AmbiguousUrlException("Tried to read url which is in more then one sameas clique!",
                    normalizedUrl);

        }

        return readMainObject(ids.iterator()
                                 .next());

    }

    /**
     * Returns the main view with given url at given origin id. If not present,
     * an exception is thrown.
     * 
     * @param publisherId
     *            The traceprov internal id of the publisher that originated the
     *            context.
     * @param url
     *            the url of the object to retrieve.
     * @return the object with given url.
     * @throws eu.trentorise.opendata.traceprov.exceptions.DataNotFoundException
     */
    public TraceData read(long publisherId, String url) {

        checkArgument(publisherId >= 0);
        checkNotEmpty(url, "Invalid url!");

        String normalizedUrl = normalizeUrl(url);

        List<TraceData> rets = getStoredValuesByUrl(publisherId, normalizedUrl);

        if (rets.isEmpty()) {
            throw new DataNotFoundException("Couldn't find view identified by publisher id " + publisherId
                    + " and external url " + normalizedUrl);
        }

        return rets.get(0);

    }

    /**
     * @see #create(Iterable)
     */
    public <T extends TraceData> List<T> create(T... dataNodes) {
        return create(Arrays.asList(dataNodes));
    }

    private void index(TraceData dataNode) {
        ImmutableList<TraceType> types = typeRegistry.getTypesFromInstance(dataNode.getRawValue());
        for (TraceType type : types) {
            if (indexedTypes.contains(type.getId())) {
                indexedValues.put(type.getId(), idCounter);
            }
        }
    }

    /**
     * Returns the publisher id
     * 
     * @throws IllegalArgumentException
     * @throws TraceProvNotFoundException
     */
    private long checkDataNodeToStore(TraceData dataNode, boolean toCreate, boolean publisher) {
        checkNotNull(dataNode);
        if (toCreate) {
            checkArgument(dataNode.getId() < 0, "Tried to create view with non-negative id: %s", dataNode.getId());
        }

        try {
            String refUri = dataNode.getRef()
                                    .uri();
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException(
                    "Tried to create a DataNode without a valid ref! Ref is " + dataNode.toString(), ex);
        }

        if (publisher) {
            /*
             * todo useless check? String docId =
             * dataNode.getRef().getDocumentId();
             * 
             * String pubUri = dataNode.getMetadata().getPublisher().getUri();
             * /* checkArgument(docId.equals(pubUri),
             * "A Publisher self publishes itself, " +
             * "so ref.documentId must be equal to metadata.publisher.uri! " +
             * "Found instead:\n  ref.documentId=%s\n  metadata.publisher.uri=%s"
             * , docId, pubUri);
             */
            if (toCreate) {
                long pubId = dataNode.getMetadata()
                                     .getPublisherId();
                if (pubId == -1) {
                    return idCounter; // a publisher can self publish itself
                } else {
                    return pubId;
                }

            } else {
                return dataNode.getId();
            }
        } else {
            long pubId = dataNode.getMetadata()
                                 .getPublisherId();
            checkArgument(pubId != -1, "Tried to create datanode with invalid publisher, found one is empty!");
            return read(pubId).getId();
        }
    }

    /**
     * @see #createPublisher(Iterable)
     */
    public List<TraceData> createPublisher(TraceData... dataNodes) {
        return createPublisher(Arrays.asList(dataNodes));
    }

    /**
     * Creates new data nodes and return them. A Publisher is a some
     * organization/person/entity from which we can take data. In TraceProv, it
     * is a DataNode like the others with the peculiarity that it can be its own
     * publisher, which means ref.documentId can be equal to
     * metadata.publisher.uri
     * 
     * @return new data nodes with newly assigned id.
     */
    public List<TraceData> createPublisher(Iterable<TraceData> dataNodes) {
        return create(dataNodes, true);
    }

    /**
     * Creates new data nodes and return them. They all must have valid
     * publisher and refs.
     *
     * @return new data nodes with newly assigned id.
     */
    public <T extends TraceData> List<T> create(Iterable<T> dataNodes) {
        return create(dataNodes, false);
    }

    /**
     * In case key has no value, the empty array is returned.
     * <strong>NOTE:</strong> this empty array is <strong>not</strong> stored in
     * the table.
     */
    private ArrayList<TraceData> getStoredValuesByUrl(long publisherId, String url) {
        ArrayList<TraceData> ret = this.storedValuesByUrl.get(publisherId, url);
        if (ret == null) {
            return new ArrayList();
        } else {
            return ret;
        }

    }

    private void insertStoredValueByUrl(TraceData traceData) {
        long id = traceData.getId();
        long pubId = traceData
                .getMetadata()
                .getPublisherId();
        checkArgument(id >= 0, "Invalid tracedata id! Found: %s", id);
        String uri = traceData.getRef()
                              .uri();
        ArrayList<TraceData> datanodes = storedValuesByUrl.get(pubId,
                uri);
        if (datanodes == null) {
            storedValuesByUrl.put(pubId, uri, Lists.newArrayList(traceData));
        } else {
            datanodes.add(traceData);
            storedValuesByUrl.put(pubId, uri, datanodes);
        }
    }

    /**
     * Creates new data nodes and return them. They all must have valid
     * publisher and refs.
     *
     * @param publisher
     *            if true node will
     * @return new data nodes with newly assigned id.
     */
    private <T extends TraceData> List<T> create(Iterable<T> dataNodes, boolean publisher) {
        checkInitialized(0);
        ImmutableList.Builder<T> retb = ImmutableList.builder();

        for (TraceData dataNode : dataNodes) {

            long publisherId = checkDataNodeToStore(dataNode, true, publisher);

            NodeMetadata newwMetadata = NodeMetadata.builder()
                                                    .from(dataNode.getMetadata())
                                                    .setPublisherId(publisherId)
                                                    .setTimestamp(new Timestamp(System.currentTimeMillis()))
                                                    .build();

            T toCreate = (T) dataNode.fromThis()
                                     .setId(idCounter)
                                     .setMetadata(newwMetadata)
                                     .build();
            storedValuesById.put(idCounter, toCreate);
            insertStoredValueByUrl(toCreate);
            putSameAsIds(idCounter);

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
    public List<TraceData> update(TraceData... dataNodes) {
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
     * @throws DataNotFoundException
     */
    @Nullable
    public List<TraceData> update(Iterable<TraceData> dataNodes) {
        throw new UnsupportedOperationException("TODO implement me!");
    }

    /**
     *
     * Returns the ids of the views considered to be the same as the provided
     * one (including it).
     */
    public List<Long> readSameAsIds(long id) {
        checkInitialized();
        checkArgument(id >= 0);
        return ImmutableList.copyOf(sameAsIds.get(id));
    }

    /**
     * @see #putSameAsIds(Iterable)
     */
    public void putSameAsIds(long mainId, Long... ids) {
        putSameAsIds(Arrays.asList(ids), mainId);
    }

    /**
     * Sets provided datanode as the main datanode in the sameAs clique
     * 
     */
    public void setMainNode(long datanodeId) {
        read(datanodeId);

        Set<Long> ids = ImmutableSet.copyOf(sameAsIds.get(datanodeId));

        for (Long id : ids) {
            // because get returns a view
            Set<Long> valsCopy = new LinkedHashSet(this.sameAsIds.get(id));
            this.sameAsIds.removeAll(id);
            this.sameAsIds.put(id, datanodeId);
            this.sameAsIds.putAll(id, valsCopy);
        }
    }

    /**
     * States all provided view ids are 'same as'. If they are already same as
     * with some other id outside the provided ones, the old relations are
     * merged into the provided one.
     *
     * @param ids
     *            the sameas ids
     * @param mainId
     *            the id of the main view among provided ids. It must belong to
     *            the enlarged clique.
     * @throws IllegalStateException
     *             if there are no corresponding views for the provided ids,
     * 
     */
    public void putSameAsIds(Iterable<Long> ids, long mainId) {
        checkNotNull(ids);
        checkArgument(mainId >= 0, "Invalid id: " + mainId);
        if (!Iterables.contains(ids, mainId)) {
            read(mainId);
        }

        ImmutableSet.Builder<Long> enlargedCliqueb = ImmutableSet.builder();
        enlargedCliqueb.add(mainId);
        for (Long id : ids) {
            enlargedCliqueb.addAll(readSameAsIds(id));
        }

        ImmutableSet<Long> enlargedClique = enlargedCliqueb.build();

        for (Long id : enlargedClique) {
            this.sameAsIds.removeAll(id);
            this.sameAsIds.putAll(id, enlargedClique);

        }

        assert true;
    }

    /**
     * Returns the controller status of the controller associated to the clique
     * to which the provided view id belongs
     *
     * @param viewId
     *            the id of a view, which may not necessarily be a view
     *            generated by odr.
     * @param originId
     *            the id of the server of origin
     */
    public ControllerStatus controllerStatus(long viewId) {
        checkArgument(viewId >= 0);

        // The view is NEW if it is not in any equivalence relation with objects
        // from the domain.
        throw new UnsupportedOperationException("TODO IMPLEMENT ME");
    }

    /**
     * Returns true if the stored views corresponding to the two provided ids
     * are equal using Java equality.
     *
     * @throws DataNotFoundException
     *             if objects are not found.
     */
    public boolean shallowEqual(long viewId1, long viewId2) {
        TraceData view1 = read(viewId1);
        TraceData view2 = read(viewId2);
        throw new UnsupportedOperationException("TODO IMPLEMENT ME!");
        // return view1.controlledEquals(view2);
    }

    /**
     * Returns true if the stored objects corresponding to the two provided ids
     * are equal using Java equality.
     *
     * @throws DataNotFoundException
     */
    public boolean deepEqual(long objId1, long objId2) {
        throw new UnsupportedOperationException("TODO Implement me!!");
    }

    /**
     * @see #sameAs(List)
     */
    public boolean sameAs(Long... viewIds) {
        return sameAs(Arrays.asList(viewIds));
    }

    /**
     * Returns true if provided views all belong to the same clique.
     *
     * @throws DataNotFoundException
     */
    public boolean sameAs(Iterable<Long> viewIds) {
        long firstMainId = -1;
        for (Long viewId : viewIds) {
            long curMainId = readMainObject(viewId).getId();
            if (firstMainId == -1) {
                firstMainId = curMainId;
            } else {
                if (curMainId != firstMainId) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @throws DataNotFoundException
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

    /**
     * Returns the database in the current thread.
     * 
     * todo what about initialization??
     */
    public static TraceDb getDb() {
        return dbPool.get();
    }

    public static void setCurrentDb(TraceDb db) {
        checkNotNull(db);
        db.checkInitialized();
        dbPool.set(db);
        LOG.info("Set current thread tracedb to " + db.getDbUrl());
    }

    public TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    public void setTypeRegistry(TypeRegistry typeRegistry) {
        checkNotNull(typeRegistry);
        this.typeRegistry = typeRegistry;
    }

    /**
     * 
     * Compares the versions of the gieven datanodes.
     * 
     * <p>
     * Returns a negative integer, zero, or a positive integer as obj1 is less
     * recent than, same as, or more recent than the specified object according
     * to its publisher
     * </p>
     * 
     * <p>
     * Comparison is done first between raw objects, by using their associated
     * tracetypes to which objects belong must agree on version. If they don't
     * an exception is thrown. If there are no tracetypes for which the raw
     * objects can be compared, comparison is done on the node metadata
     * timestamps.
     * </p>
     * 
     * @throw IncomparableVersionsException if data objects are not comparable,
     *        that is if at least two tracetypes to which both objects belong
     *        disagree on version.
     */
    public int compareVersion(long objId1, long objId2) {
        TraceData node1 = read(objId1);
        TraceData node2 = read(objId2);

        ImmutableList<TraceType> types1 = typeRegistry.getTypesFromInstance(node1.getRawValue());
        ImmutableList<TraceType> types2 = typeRegistry.getTypesFromInstance(node1.getRawValue());

        SetView<TraceType> types = Sets.intersection(new HashSet(types1), new HashSet(types2));

        Multimap<Integer, TraceType> agreeing = HashMultimap.create();

        for (TraceType type : types) {
            try {
                int x = type.compareVersion(node1.getRawValue(), node2.getRawValue());
                if (x < 0) {
                    agreeing.put(-1, type);
                } else if (x == 0) {
                    agreeing.put(0, type);
                } else {
                    agreeing.put(+1, type);
                }
            } catch (IncomparableVersionsException ex) {

            }
        }

        int lastKey = -2;
        for (Integer key : agreeing.keySet()) {
            if (agreeing.get(key)
                        .size() > 0) {
                if (lastKey == -2) {
                    lastKey = key;
                } else {
                    throw new IncomparableVersionsException(
                            "Found at least two types that were in disagreement on version " + "of data nodes "
                                    + node1.getId() + " and " + node2.getId() + ". \n Agreement map: "
                                    + agreeing.toString());
                }
            }
        }
        if (lastKey == -2) {
            return node1.getMetadata()
                        .getTimestamp()
                        .compareTo(node2.getMetadata()
                                        .getTimestamp());

        } else {
            return lastKey;
        }

    }
}
