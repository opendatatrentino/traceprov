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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.trentorise.opendata.commons.NotFoundException;
import java.util.Iterator;
import javax.annotation.Nullable;

/**
 * A JsonPath expression.
 *
 * Adapted from
 * https://github.com/jayway/JsonPath/blob/json-path-2.0.0/json-path/src/main/java/com/jayway/jsonpath/internal/token/PathToken.java
 *
 * @author David Leoni
 */
@JsonSerialize(using = ToStringSerializer.class)
// todo @JsonDeserialize(as = PropertyPath.class)
public abstract class APath {

    @Nullable
    public abstract APath getNext();

    public abstract boolean isTokenDefinite();

    public abstract String getPathFragment();

    /**
     * Returns the next element in the path.
     *
     * @throws eu.trentorise.opendata.commons.NotFoundException if not found.
     */
    public APath next() {
        if (isLeaf()) {
            throw new NotFoundException("Current path token is a leaf");
        }
        return getNext();
    }

    public boolean isLeaf() {
        return getNext() == null;
    }

    public boolean isUpstreamDefinite(APath prev, boolean isRoot) {
        boolean isUpstreamDefinite = isTokenDefinite();
        if (isUpstreamDefinite && !isRoot) {
            isUpstreamDefinite = prev.isPathDefinite();
        }
        return isUpstreamDefinite;
    }

    // could be faster..
    public int getTokenCount() {
        int cnt = 1;
        APath token = this;

        while (!token.isLeaf()) {
            token = token.next();
            cnt++;
        }
        return cnt;
    }

    // could be optimized
    public boolean isPathDefinite() {
        boolean isDefinite = isTokenDefinite();
        if (isDefinite && !isLeaf()) {
            isDefinite = next().isPathDefinite();
        }
        return isDefinite;
    }

    @Override
    public String toString() {
        if (isLeaf()) {
            return getPathFragment();
        } else {
            return getPathFragment() + next().toString();
        }
    }

    /**
     * Accepts a collection of objects, since all objects have toString()
     *
     * @param wrap first object is wrapped between {@code wrap}.
     */
    protected static String join(String delimiter, String wrap, Iterable<? extends Object> objs) {
        Iterator<? extends Object> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(wrap).append(iter.next()).append(wrap);
        while (iter.hasNext()) {
            buffer.append(delimiter).append(wrap).append(iter.next()).append(wrap);
        }
        return buffer.toString();
    }
}
