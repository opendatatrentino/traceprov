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
package eu.trentorise.opendata.traceprov.impl.dcat;

import eu.trentorise.opendata.traceprov.dcat.IFoafAgent;
import static eu.trentorise.opendata.traceprov.impl.TraceProvUtils.checkNonNull;

/**
 * Mutable implementation of a FOAF Agent: http://xmlns.com/foaf/spec/
 *
 * @author David Leoni
 */
public class FoafAgent implements IFoafAgent {
    
    /**
     * Object to use when required agent is unknown.
     */
    public static final FoafAgent UNKNOWN_AGENT = new FoafAgent();

    private String uri;
    private String name;
    private String mbox;

    public FoafAgent() {
        uri = "";
        name = "";
        mbox = "";
    }


    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("nulluri is not accepted!");
        }

        this.uri = uri;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkNonNull(name, "FoafAgent name");
        this.name = name;
    }

    public String getMbox() {
        return mbox;
    }

    public void setMbox(String mbox) {
        checkNonNull(mbox, "FoafAgent mbox");
        this.mbox = mbox;
    }

}
