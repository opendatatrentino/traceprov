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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import eu.trentorise.opendata.commons.NotFoundException;
import java.util.Iterator;
import java.util.List;
import javax.validation.Path;
import org.immutables.value.Value;

/**
 * A Java validation api implementation, extended with some JsonPath construct.
 * For JsonPath we try to follow
 * <a href="https://github.com/jayway/JsonPath" target="_blank">Jayway
 * implementation </a>
 * 
 * TODO MUCH WORK IN PROGRESS
 * 
 * @author David Leoni
 */
@JsonSerialize(using = ToStringSerializer.class)
// todo @JsonDeserialize(as = PropertyPath.class)

@Value.Immutable
@BuilderStylePublic
abstract class ATracePath implements Path {

    public abstract List<TracePathElement> getNodes();

    @Value.Default
    public TracePath getNext() {
	throw new UnsupportedOperationException("todo implement me");
	// return TracePath.of();
    }

    /**
     * Returns the next element in the path.
     *
     * @throws eu.trentorise.opendata.commons.NotFoundException
     *             if not found.
     */
    public TracePath next() {
	throw new UnsupportedOperationException("todo implement me");
	/*
	 * if (isLeaf()) { throw new NotFoundException(
	 * "Current path token is a leaf"); } return getNext();
	 */
    }

    /*
     * public boolean isUpstreamDefinite(Path prev, boolean isRoot) { boolean
     * isUpstreamDefinite = isTokenDefinite(); if (isUpstreamDefinite &&
     * !isRoot) { isUpstreamDefinite = prev.isPathDefinite(); } return
     * isUpstreamDefinite; }
     */
    // could be optimized
    public boolean isPathDefinite() {

	throw new UnsupportedOperationException("todo implement me");
	/*
	 * if (getNodes().isEmpty()) { return true; // todo check about empty
	 * nodes... anyway RootNode IS definite. } else { for (PathNode pn :
	 * getNodes()) { if (!pn.isTokenDefinite()) { return false; } } } return
	 * true;
	 */
    }

    @Override
    public String toString() {
	throw new UnsupportedOperationException("todo implement me");
	/*
	 * if (isLeaf()) { return getPathFragment(); } else { return
	 * getPathFragment() + next().toString(); }
	 */
    }

    public boolean isLeaf() {
	throw new UnsupportedOperationException("todo implement me");
	// return getNext() == null;
    }

    @Override
    public Iterator<Node> iterator() {
	return (Iterator<Node>) (Iterator<?>) getNodes().iterator();
    }
}
