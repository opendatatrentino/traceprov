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
package eu.trentorise.opendata.traceprov.engine;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.TodUtils;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import java.util.List;
import javax.annotation.Nullable;
import org.immutables.value.Value;

/**
 * The result of a conversion
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = CastResult.class)
@JsonDeserialize(as = CastResult.class)
public abstract class ACastResult {

    /**
     * The accuracy of the conversion in the range [0.0, 1.0]. For example,
     * converting from String "July 1996" to Java Date 1996-07-01T00:00:00 might
     * have 0.6 accuracy as it 'invented' the day and time. Defaults to 0.5.
     */
    @Value.Default
    public double getAccuracy() {
        return 0.5;
    }

    /**
     * Returns true if conversion completely succeded. Note that even if
     * returned value is false, you may still be able to get a converison result
     * from {@link #getValue()} with low accuracy.
     *
     * @see #completelyFailed()
     */
    public final boolean completelySucceded() {
        return getAccuracy() >= 1.0 - TodUtils.TOLERANCE;
    }

    /**
     * Returns true if conversion completely failed. This means a call to
     * {@link #value()} will throw an exception.
     *
     * @see #completelySucceded()
     */
    public final boolean completelyFailed() {
        return getAccuracy() <= 0.0 + TodUtils.TOLERANCE;
    }

    /**
     * The interpolated message for the conversion result
     */
    @Value.Default
    public String getMessage() {
        return "";
    }

    /**
     * The non-interpolated error message for the conversion result
     */
    @Value.Default
    String getMessageTemplate() {
        return "";
    }


    /* todo this is too much for now, accuracy is enough
     missingElements: JsonPath [ ] for structures | delta for numbers
     original value 
     */
    /**
     *
     * Returns the value resulting from the conversion, if there is one with
     * sufficient confidence, otherwise throws exception. 
     * threeshold.
     *
     * @throws eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException if
     * there is no such value.
     *
     * @see #getValue()
     */
    @Nullable
    public Object value() {
        if (completelyFailed()) {
            throw new TraceProvNotFoundException("There is no cast result value (probably because conversion didn't succeed!");
        } else {
            return getValue();
        }
    }

    /**
     * The resulting value of the conversion, if present, null otherwise
     * 
     * @see #value()     
     */
    @Nullable
    public abstract Object getValue();

    /**
     * Returns candidate values of the conversion.
     */
    public abstract List<WeightedResult> getCandidates();

    // todo javadoc
    public static CastResult of(@Nullable Object value, double accuracy) {
        return CastResult.builder().addCandidates(WeightedResult.of(1.0, value)).setAccuracy(accuracy).build();
    }
}
