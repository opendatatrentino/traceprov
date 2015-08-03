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
import java.util.List;
import javax.annotation.Nullable;

/**
 *
 * @author David Leoni
 */
public class Preconditions {

    /**
     *
     *
     * @param prependedErrorMessage the exception message to use if the check
     * fails; will be converted to a string using String.valueOf(Object) and
     * prepended to more specific error messages.
     *
     * @param position a list of coordinates
     * @throws IllegalArgumentException on invalid coordinates
     * @return the validated coordinates
     */
    public static List<Double> checkPosition(List<Double> position, @Nullable Object prependedErrorMessage) {        
        if (position.size() < 2) {
            throw new IllegalStateException(String.valueOf(prependedErrorMessage) + "Found " + position.size() + " coordinates, required at least 2 ");
        }
        for (Double d : position){
            checkNotNull(d, "Found null in coordintate!");
        }
        return position;
    }

    /**
     *
     *
     * @param prependedErrorMessage the exception message to use if the check
     * fails; will be converted to a string using String.valueOf(Object) and
     * prepended to more specific error messages.
     *
     * @throws IllegalArgumentException on invalid coordinates
     * @return the validated coordinates
     */
    public static List<List<Double>> checkPositions(List<List<Double>> positions, int min, @Nullable Object prependedErrorMessage) {
        checkNotNull(positions);
        checkArgument(positions.size() >= min, "At least %s positions are required, found instead %s", min, positions.size());
        for (int i = 0; i < positions.size(); i++) {
            List<Double> c = positions.get(i);
            checkPosition(c, "Wrong position at index " + i +".");            
        }
        return positions;
    }

}
