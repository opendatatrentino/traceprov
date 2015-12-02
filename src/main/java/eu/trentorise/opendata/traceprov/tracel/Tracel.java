package eu.trentorise.opendata.traceprov.tracel;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TraceProv Expression Language
 *
 */
public final class Tracel {
    
    private Tracel(){}
    
    public static boolean isValidId(String id){
        checkNotNull(id);
        return !id.isEmpty() && !id.contains(" ") && !Character.isDigit(id.charAt(0));
    }
}
