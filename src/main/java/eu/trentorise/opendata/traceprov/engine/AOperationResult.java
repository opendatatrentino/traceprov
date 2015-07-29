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
import eu.trentorise.opendata.traceprov.engine.OperationResult;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = OperationResult.class)
@JsonDeserialize(as = OperationResult.class)
public class AOperationResult {
    
   /**
     * The accuracy of the conversion in the range [0.0, 1.0]. For example,
     * transforming from String "July 1996" to Java Date 1996-07-01T00:00:00 might
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
     * 
     * H423: il valore "blabla" non è corretto. 
     */
    @Value.Default
    public String getMessage(){
        return "";
    }

    /**
     * The non-interpolated error message for this constraint violation
     * 
     * H423: il valore ${a.b.c} non è corretto.      
     * 
     */
    @Value.Default
    String getMessageTemplate(){
        return "";
    }

}