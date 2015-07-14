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
package eu.trentorise.opendata.traceprov.services;

import com.google.common.collect.ImmutableList;
import eu.trentorise.opendata.traceprov.data.DcatMetadata;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

/**
 * Interface to load a file from a source, like a url, a db, etc...
 *
 * Implementations are intended to be Java beans and their setters will ba
 * called to set parameters for loader methods (i.e. auth token)
 *
 * Implementations need not be thread-safe
 *
 * @author David Leoni
 */
public interface ILoader {

    /**
     * Loads from a given url a resource into output stream.
     *
     * @param url Resource location
     * @param output The output stream where the file will be loaded     
     *
     * @throws eu.trentorise.opendata.traceprov.LoadException if some error
     * occurs.
     */
    void loadData(URL url, OutputStream output);

    /**
     * Loads and returns a metadata for a resource at given url. If loading of
     * some metadata fails method will return
     * {@link eu.trentorise.opendata.traceprov.data.DcatMetadata#of()} in the
     * corresponding position of the result list.
     *
     * @param url the url of the distribution, see
     * {@link eu.trentorise.opendata.traceprov.dcat.DcatDistribution#getUri()}
     */
    public ImmutableList<DcatMetadata> loadMetadata(Iterable<URL> url);

}
