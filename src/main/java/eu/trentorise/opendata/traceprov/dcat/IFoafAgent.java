/**
 * *****************************************************************************
 * Copyright 2013-2014 Trento Rise (www.trentorise.eu/)
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 *
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************
 */
package eu.trentorise.opendata.traceprov.dcat;

import java.util.Locale;
import java.util.Map;

/**
 * Models a minimal FOAF Agent: http://xmlns.com/foaf/spec/
 *
 * @author David Leoni
 */
public interface IFoafAgent {

     /**
     * Returns the uri of the agent.
     */
    String getUri();
    
    /**
     * http://xmlns.com/foaf/0.1/name
     */
    Map<Locale, String> getName();
    
    /**
     * Returns the mail box
     * http://xmlns.com/foaf/0.1/mbox
     */
    String getMbox();
        
    
}
