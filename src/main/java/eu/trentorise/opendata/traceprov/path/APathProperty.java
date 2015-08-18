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

import java.util.List;
import javax.validation.Path;

/**
 *
 * @author David Leoni
 */
abstract class APathProperty extends PathElement implements Path.PropertyNode {

    public abstract List<String> getProperties();

    @Override
    public boolean isTokenDefinite() {
        return true;
    }

    @Override
    public String getPathFragment() {
        return new StringBuilder()
                .append("[")
                .append(PathElement.join(", ", "'", getProperties()))
                .append("]").toString();
    }
}
