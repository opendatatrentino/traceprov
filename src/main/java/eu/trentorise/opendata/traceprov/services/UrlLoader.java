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

import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.OdtUtils.checkNotEmpty;
import java.io.OutputStream;

/**
 * Loads a file from a URL
 * @author David Leoni
 * 
 * 
 */
public class UrlLoader implements ILoader {

    @Override
    public void load(String url, OutputStream output, Object... args) {
        checkNotEmpty(url, "invalid url!");
        checkNotNull(output);        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}