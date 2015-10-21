package eu.trentorise.opendata.traceprov.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.validation.Ref;
import eu.trentorise.opendata.traceprov.data.TraceNode;
import eu.trentorise.opendata.traceprov.data.DataValue;
import eu.trentorise.opendata.traceprov.data.NodeMetadata;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.dcat.AFoafAgent;
import eu.trentorise.opendata.traceprov.dcat.FoafAgent;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

/**
 *
 * @author David Leoni
 */
public class DbTest {

    private static final Logger LOG = Logger.getLogger(DbTest.class.getCanonicalName());

    public static final String TEST_PUBLISHER_URI = "tracedb-test:publisher";
    
    private static final AFoafAgent TEST_PUBLISHER = FoafAgent.builder()
	    .setName(Dict.of("Test Publisher"))
	    .setUri(TEST_PUBLISHER_URI)
	    .build();

    
    public static final NodeMetadata INIT_TEST_PUBLISHER_METADATA = 
	    NodeMetadata.builder()	    
	    .build();
    
    TraceDb db;
    
    @Before
    public void before() throws Exception {
	db = TraceDb.createInMemoryDb(randomId(), TypeRegistry.of());
	TraceDb.setCurrentDb(db);
    }

    @After
    public void after() {
	db.drop();
    }

    private String randomId(){
	return UUID.randomUUID().toString();
    }
        
    private TraceNode makePublisher(){
	List<TraceNode> dns = db.createPublisher(
		DataValue.of(	Ref.ofDocumentId(TEST_PUBLISHER_URI), 
				INIT_TEST_PUBLISHER_METADATA, 
				"h"));
	assertEquals(1, dns.size());
	return dns.get(0);	
    }
    
    
    @Test
    public void testCreatePublisher()  {	
	TraceNode pub1 = makePublisher();	
	assertTrue(pub1.getId() == 1L);	
	assertEquals(pub1.getId(), pub1.getMetadata().getPublisherId());
	assertTrue(db.sameAs(pub1.getId(), pub1.getId()));
	pub1.getRef().uri(); // should not thow exception		
	TraceNode readPub1 = db.read(pub1.getId());
	assertEquals(pub1, readPub1);
	assertTrue(pub1.getId() == 1L);
	TraceNode readPubByIdUrl = db.read(pub1.getId(), pub1.getRef().getDocumentId());
	//assertTrue(pub1.equals(readPubByIdUrl));
	assertTrue(pub1.getId() == 1L);
	assertEquals(pub1, readPubByIdUrl);	
	
    }
    
    private NodeMetadata makeMetadata(TraceNode pub){
	return NodeMetadata.builder().setPublisherId(pub.getId()).build();	
    }
    @Test
    public void testCreate()  {
	TraceNode pub = makePublisher();
	List<TraceNode> dns = db.create(
		DataValue.of(Ref.ofDocumentId("a"), 
			     makeMetadata(pub), 
			     "b"));
	assertEquals(1, dns.size());
	TraceNode dn = dns.get(0);	
	assertTrue(dn.getId() >= 0);
		
	assertEquals("a", dn.getRef().getDocumentId());
	assertEquals(makeMetadata(pub), dn.getMetadata());
	assertEquals("b", dn.getRawValue());
	dn.getRef().uri(); // should not thow exception
	assertTrue(db.sameAs(dn.getId(), dn.getId()));
    }
    
    @Test
    public void testReadDefaultNodes() throws IOException {
	
	TraceNode tpub = db.read(TraceDb.TRACEDB_PUBLISHER_ID);

	// tracedb publisher is self published
	assertEquals(TraceDb.TRACEDB_PUBLISHER_ID, tpub.getMetadata().getPublisherId());
	assertTrue(db.sameAs(tpub.getId(), tpub.getId()));
    }
       
    @Test
    @Ignore
    public void testClique(){        
        throw new UnsupportedOperationException("TODO IMPLEMENT ME!");
        // odrDb.updateSameAsIds(null, null, null)
        // odrDb.addSameAsIds(null, null)
    }

    @Test
    @Ignore
    public void testUpdateRead() {
	throw new UnsupportedOperationException("TODO IMPLEMENT ME!");
    }
}
