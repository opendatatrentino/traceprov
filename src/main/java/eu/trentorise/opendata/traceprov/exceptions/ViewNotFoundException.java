package eu.trentorise.opendata.traceprov.exceptions;

public class ViewNotFoundException extends TraceProvNotFoundException {

    private static final long serialVersionUID = 1L;

    public ViewNotFoundException(String msg) {
	super(msg);	
    }
    
    public ViewNotFoundException(Class clazz, long originId, String externalId){
	super("Couldn't find view in db for class " + clazz.getName() + " original server id: " + originId + ", id on foreign server: " + externalId);
    }
    
    public ViewNotFoundException(String prependedMsg, Class clazz, long originId, String externalId){
	super(String.valueOf(prependedMsg) + "  Couldn't find view in db for class " + clazz.getName() + " original server id: " + originId + ", id on foreign server: " + externalId);
    }

}
