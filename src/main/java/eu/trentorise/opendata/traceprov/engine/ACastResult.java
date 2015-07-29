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
import eu.trentorise.opendata.commons.OdtUtils;
import eu.trentorise.opendata.traceprov.engine.CastResult;
import eu.trentorise.opendata.traceprov.engine.WeightedResult;
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
     * have 0.6 accuracy as it 'invented' the day and time.
     */
    @Value.Default
    public double getAccuracy() {
        return 0.0;
    }

    /**
     * 
     */
    public final boolean succeded() {
        return getAccuracy() >= 1.0 - OdtUtils.TOLERANCE;
    }

    /**
     * The interpolated message for the conversion result
     */
    @Value.Default
    public String getMessage(){
        return "";
    }

    /**
     * The non-interpolated error message for this constraint violation
     */
    @Value.Default
    String getMessageTemplate(){
        return "";
    }


    /* todo this is too much for now, accuracy is enough
     missingElements: JsonPath [ ] for structures | delta for numbers
     original value 
     */
    /**
     *
     * Returns the value resulting from the conversion, if there is one with
     * sufficient confidence. todo define threeshold.
     *
     * @throws eu.trentorise.opendata.commons.NotFoundException if there is no
     * such value.
     *
     * @see #getValues
     */
    @Nullable
    public Object value() {
        throw new UnsupportedOperationException("TODO implement me");
    }

    /**
     * Returns candidate values of the conversion.
     */
    public abstract List<AWeightedResult> getCandidates();

    // todo javadoc
    public static CastResult of(@Nullable Object value, double accuracy) {
        return CastResult.builder().addCandidates(WeightedResult.of(1.0, value)).setAccuracy(accuracy).build();
    }
}
