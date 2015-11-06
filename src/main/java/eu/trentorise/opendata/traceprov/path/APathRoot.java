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

/**
 * Adapted from
 * https://github.com/jayway/JsonPath/blob/json-path-2.0.0/json-path/src/main/java/com/jayway/jsonpath/internal/token/RootPathToken.java
 *
 */
//@Value.Immutable
//@SimpleStyle
abstract class APathRoot extends TracePathElement {

    @Override
    public String getPathFragment() {
        return "$";
    }

    @Override
    public boolean isTokenDefinite() {
        return true;
    }

}