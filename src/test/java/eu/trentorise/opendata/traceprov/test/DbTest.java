package eu.trentorise.opendata.traceprov.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.NotFoundException;
import eu.trentorise.opendata.traceprov.db.TraceDb;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.types.DictType;
import eu.trentorise.opendata.traceprov.types.TypeRegistry;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class DbTest {

    private static final Logger LOG = Logger.getLogger(DbTest.class.getCanonicalName());

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() {
    }

    private String randomId(){
	return UUID.randomUUID().toString();
    }
    
    @Test
    public void testInMemoryCreateDrop() throws IOException {
	TraceDb db = TraceDb.createInMemoryDb("a", TypeRegistry.of());
	TraceDb.setCurrentDb(db);
	assertEquals(TraceDb.IN_MEMORY_PREFIX + "a", db.getDbUrl());
	db.drop();
    }

    private static boolean isDirEmpty(final Path directory) throws IOException {
	try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
	    return !dirStream.iterator().hasNext();
	}
    }

    @Test
    public void testCreateDrop() throws IOException {
	Path dir = Files.createTempDirectory("tracedb-");

	TraceDb db = TraceDb.createDb(dir.toString(), TypeRegistry.of());
	TraceDb.setCurrentDb(db);
	assertEquals(dir.toUri().toString(), db.getDbUrl());
	assertFalse(isDirEmpty(dir));

	db.drop();

	assertFalse(Files.exists(dir));
    }

    @Test
    public void testDoubleCreation() throws IOException {
	Path dir = Files.createTempDirectory("odrdb");
	
	TraceDb db1 = TraceDb.createDb(dir.toString(), TypeRegistry.of());
	TraceDb.setCurrentDb(db1);
	try {
	    TraceDb db2 = TraceDb.createDb(dir.toString(), TypeRegistry.of());
	    TraceDb.setCurrentDb(db2);
	    Assert.fail("Shouldn't arrive here!");
	} catch (Exception ex) {

	}
    }

    @Test
    public void testConnect() throws IOException {
	Path dir = Files.createTempDirectory("tracedb-");
	TraceDb db1 = TraceDb.createDb(dir.toString(), TypeRegistry.of());
	TraceDb.setCurrentDb(db1);
	TraceDb db2 = TraceDb.connectToDb(dir.toString(), TypeRegistry.of());
	TraceDb.setCurrentDb(db2);
	TraceDb db3 = TraceDb.connectToDb(dir.toString(), TypeRegistry.of());
	TraceDb.setCurrentDb(db3);
    }

    private TraceDb newInMemorydb(){
	TraceDb db = TraceDb.createInMemoryDb("a", TypeRegistry.of());
	TraceDb.setCurrentDb(db);
	return db;
    }
    
    @Test
    public void testPrefix() {
	TraceDb db = newInMemorydb();
	
	assertEquals(0, db.getAllPrefixes().size());

	assertFalse(db.hasPrefix("a"));
	try {
	    db.getPrefix("a");
	} catch (TraceProvNotFoundException ex) {
	}

	db.putPrefix("a", "b");
	assertTrue(db.hasPrefix("a"));
	assertEquals("b", db.getPrefix("a"));
	assertFalse(db.hasPrefix("a:"));
	db.putPrefix("c:", "d");
	assertTrue(db.hasPrefix("c:"));
	assertEquals("d", db.getPrefix("c:"));
	assertFalse(db.hasPrefix("c"));
	assertEquals("de", db.expandUrl("c:e"));
	assertEquals(ImmutableMap.of("a", "b", "c:", "d"), db.getAllPrefixes());
	assertEquals("http://a.b/c", db.normalizeUrl("http://a.b/c/"));
    }

    
    
    @Test
    public void testClassRegistration() {
	TraceDb db = TraceDb.createInMemoryDb(randomId(), TypeRegistry.empty());
	TraceDb.setCurrentDb(db);
	TypeRegistry typeReg = db.getTypeRegistry();
	assertFalse(typeReg.contains(DictType.of().getId()));
	try {
	    typeReg.checkRegistered(DictType.of().getId());
	} catch (IllegalStateException ex) {

	}
	typeReg.put(DictType.of());
	assertTrue(typeReg.contains(DictType.of().getId()));
	typeReg.contains(DictType.of().getId());
    }

    @Test
    @Ignore
    public void testClique(){
        TraceDb db = newInMemorydb();
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
