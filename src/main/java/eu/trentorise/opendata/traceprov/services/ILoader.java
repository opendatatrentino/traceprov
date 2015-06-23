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

import java.io.OutputStream;

/**
 * Interfacce to load a fils from a source, like a url, a db, etc...
 * @author David Leoni
 */
public interface ILoader {
    
    /**
     * Loads a resource from an url into provided output streams. 
     * @param url   Resource location
     * @param output    The output stream where the file will be loaded
     * @param args  optional arguments for the loader
     * @throws eu.trentorise.opendata.traceprov.LoadException if some error occurs. 
     */
    void load(String url, OutputStream output, Object... args);
    
}




