/*
 * Copyright 2015 Trento Rise.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.trentorise.opendata.traceprov.path;

import com.google.common.base.Joiner;
import java.util.List;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */

//@Value.Immutable
abstract class APathArray extends TracePathElement { 
    
    /** 
     * todo put examples
     */
    public static enum Operation {

        CONTEXT_SIZE,
        SLICE_TO,
        SLICE_FROM,
        SLICE_BETWEEN,
        INDEX_SEQUENCE,
        SINGLE_INDEX;
    }

    public abstract List<Integer> getCriteria();

    @Value.Default
    public Operation getOperation() {
        return Operation.SINGLE_INDEX;
    }

    @Override
    public String getPathFragment() {
        StringBuilder sb = new StringBuilder();
        if (Operation.SINGLE_INDEX == getOperation() || Operation.INDEX_SEQUENCE == getOperation()) {
            sb.append("[")
                    .append(Joiner.on(",").join(getCriteria()))
                    .append("]");
        } else if (Operation.CONTEXT_SIZE == getOperation()) {
            sb.append("[@.size()")
                    .append(getCriteria().get(0))
                    .append("]");
        } else if (Operation.SLICE_FROM == getOperation()) {
            sb.append("[")
                    .append(getCriteria().get(0))
                    .append(":]");
        } else if (Operation.SLICE_TO == getOperation()) {
            sb.append("[:")
                    .append(getCriteria().get(0))
                    .append("]");
        } else if (Operation.SLICE_BETWEEN == getOperation()) {
            sb.append("[")
                    .append(getCriteria().get(0))
                    .append(":")
                    .append(getCriteria().get(1))
                    .append("]");
        } else {
            sb.append("NOT IMPLEMENTED");
        }

        return sb.toString();
    }

    @Override
    public boolean isTokenDefinite() {
        return (Operation.SINGLE_INDEX == getOperation() || Operation.CONTEXT_SIZE == getOperation());
    }
}