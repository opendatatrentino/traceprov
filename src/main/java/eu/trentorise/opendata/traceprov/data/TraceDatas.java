package eu.trentorise.opendata.traceprov.data;

import java.util.ArrayList;
import java.util.List;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for {@link TraceData} nodes.
 * @author da
 *
 */
public final class TraceDatas {

    private TraceDatas(){}
    
    /**
     * Extracts ids from given nodes
     */
    public static List<Long> ids(List<TraceData> nodes){
        checkNotNull(nodes);
        ArrayList<Long> ret = new ArrayList();
        for (TraceData td : nodes){
            ret.add(td.getId());
        }
        return ret;
    }

}
