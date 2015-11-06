package eu.trentorise.opendata.traceprov.exceptions;

public class DataNotFoundException extends TraceProvNotFoundException {

    private static final long serialVersionUID = 1L;

    public DataNotFoundException(String msg) {
	super(msg);	
    }
    
    public DataNotFoundException(Class clazz, long originId, String externalId){
	super("Couldn't find view in db for class " + clazz.getName() + " original server id: " + originId + ", id on foreign server: " + externalId);
    }
    
    public DataNotFoundException(String prependedMsg, Class clazz, long originId, String externalId){
	super(String.valueOf(prependedMsg) + "  Couldn't find view in db for class " + clazz.getName() + " original server id: " + originId + ", id on foreign server: " + externalId);
    }

}
