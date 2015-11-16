/*
 * Copyright 2015 Trento Rise  (trentorise.eu) 
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
package eu.trentorise.opendata.traceprov.exceptions;

import java.util.logging.Logger;

/**
 * Thrown when it is not possible to identify a given url.
 * 
 * @author David Leoni
 * @since 0.4
 */
public class AmbiguousUrlException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = Logger.getLogger(AmbiguousUrlException.class.getSimpleName());

    String url;

    protected AmbiguousUrlException() {
        super();
    }

    public AmbiguousUrlException(String msg, String url) {
        super(msg);
        setUrl(url);
    }

    public AmbiguousUrlException(String msg, String url, Throwable ex) {
        super(msg, ex);
        setUrl(url);
    }

    private void setUrl(String url) {
        if (url == null) {
            LOG.warning("Received null url, converting it to string \"null\"");
            url = "null";
        } else {
            this.url = url;
        }
        this.url = String.valueOf(url);
    }

}
