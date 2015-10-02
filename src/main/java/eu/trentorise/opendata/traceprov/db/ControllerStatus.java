package eu.trentorise.opendata.traceprov.db;

/**
 *
 * @author David Leoni
 */
public enum ControllerStatus {
    
    /** Controller represents an object not present in Ekb */
    NEW,
    /** Controller represents an object present in Ekb but that gor modified by the user */
    MODIFIED, 
    /** Controller represents an object present in Ekb that was not modified by the user */
    UNTOUCHED;
    
    /**
     * odr todo 0.3 probably not necessary
     * Both controllers URLs must match, otherwise an IllegalArgumentException is thrown.
     * 
     * UNTOUCHED UNTOUCHED -> t1 > t2
     * UNTOUCHED MODIFIED t1 > t2 ->  
     * UNTOUCHED MODIFIED t1 < t2 -> newer
     * UNTOUCHED MODIFIED t1 = t2 -> newer
     * UNTOUCHED NEW -> 
     * MODIFIED UNMODIFIED -> newer
     * 
  
    public static boolean newerThan(Controller2 ctr1, Controller2 ctr2){
        checkNonNull(ctr1, "first controller");
        checkNonNull(ctr2, "second controller");
        
    } */
}
