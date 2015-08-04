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
package eu.trentorise.opendata.traceprov.engine;

import eu.trentorise.opendata.traceprov.types.Type;
import java.util.Locale;


/**
 * odr new
 * @author David Leoni
 */
public class CastException extends Exception {

    private static String makeMessage(Object value, Type type, Locale locale, String reason){
        String javaclass = value == null ? "" : " of Java class " + value.getClass().getName();
        
        return "Couldn't cast value " + value + javaclass + "  to type " + type + " (with locale " + locale + "). \nREASON: " + reason;
    }
        
    public CastException(Object value, Type type, Locale locale, String reason) {
        super(makeMessage(value, type, locale, reason));
    }

    public CastException(Object value, Type type, Locale locale, String reason, Exception ex) {
       super(makeMessage(value, type, locale, reason), ex);
    }
    
    
}
