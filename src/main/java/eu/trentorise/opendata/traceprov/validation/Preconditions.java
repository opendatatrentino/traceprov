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
package eu.trentorise.opendata.traceprov.validation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;

import com.google.common.collect.ImmutableList;

import eu.trentorise.opendata.traceprov.types.TraceType;

import java.util.List;
import javax.annotation.Nullable;

/**
 *
 * @author David Leoni
 */
public final class Preconditions {

    private Preconditions(){}
    
    
    public static TraceType checkType(@Nullable TraceType type){
	checkNotNull(type);
	checkNotEmpty(type.getId(), "Invalid type datatype id!");
	return type;
    }
    
    /**
     *
     *
     * @param prependedErrorMessage the exception message to use if the check
     * fails; will be converted to a string using String.valueOf(Object) and
     * prepended to more specific error messages.
     *
     * @param position a list of coordinates
     * @throws IllegalArgumentException on invalid coordinates     
     */
    public static void checkPosition(List<Double> position, @Nullable Object prependedErrorMessage) {      
        checkNotNull(position, String.valueOf(prependedErrorMessage) + " - " + prependedErrorMessage);
        if (position.size() < 2) {
            throw new IllegalStateException(String.valueOf(prependedErrorMessage) + "Found " + position.size() + " coordinates, required at least 2 ");
        }
        for (Double d : position){
            checkNotNull(d, String.valueOf(prependedErrorMessage) + "Found null in coordinate!");
        }
   
    }

    /**
     * Checks provided positions are correct
     *
     * @param prependedErrorMessage the exception message to use if the check
     * fails; will be converted to a string using String.valueOf(Object) and
     * prepended to more specific error messages.
     *
     * @param min there must be at least {@code min} positions
     * @throws IllegalArgumentException on invalid coordinates
     */
    public static void checkPositions(List<ImmutableList<Double>> positions, int min, @Nullable Object prependedErrorMessage) {
        checkNotNull(positions, String.valueOf(prependedErrorMessage));
        checkArgument(positions.size() >= min, String.valueOf(prependedErrorMessage) + "At least %s positions are required, found instead %s", min, positions.size());
        for (int i = 0; i < positions.size(); i++) {
            List<Double> c = positions.get(i);
            checkPosition(c, String.valueOf(prependedErrorMessage) + " - Wrong position at index " + i +".");            
        }        
    }
    
    
   /**
     * Checks provided polygon is correct.
     *
     * @param prependedErrorMessage the exception message to use if the check
     * fails; will be converted to a string using String.valueOf(Object) and
     * prepended to more specific error messages.
     *
     * @param min there must be at least {@code min} positions
     * @throws IllegalArgumentException on invalid coordinates
     */    
    public static void checkPolygon(List<ImmutableList<ImmutableList<Double>>> positions, @Nullable Object prependedErrorMessage){
        checkNotNull(positions, String.valueOf(prependedErrorMessage) + " - " + prependedErrorMessage);
        for (ImmutableList<ImmutableList<Double>> lst : positions){
            checkPositions(lst, 4, String.valueOf(prependedErrorMessage) + " - Invalid polygon!");
        }
    }

    
}
