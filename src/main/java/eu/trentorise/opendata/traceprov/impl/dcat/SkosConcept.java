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

import eu.trentorise.opendata.traceprov.dcat.ISkosConcept;
import eu.trentorise.opendata.traceprov.dcat.ISkosConceptScheme;

/**
 * Mutable implementation of a SkosConcept:
 * http://www.w3.org/2009/08/skos-reference/skos.html#Concept
 * @author David Leoni
 */
public class SkosConcept implements ISkosConcept {
    
    public static final SkosConcept UNKWOWN_SKOS_CONCEPT = new SkosConcept();
    
    private String uri;
    private String prefLabel;

    public SkosConcept() {
       uri = "";
        prefLabel = "";
    }
    
    
    private ISkosConceptScheme inScheme;

    @Override
    public String getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel) {
        if (prefLabel == null){
            throw new IllegalArgumentException("null prefLabel is not accepted!");
        }
        this.prefLabel = prefLabel;
    }

    @Override
    public ISkosConceptScheme getInScheme() {        
        return inScheme;
    }

    public void setInScheme(ISkosConceptScheme inScheme) {
        if (inScheme == null){
            throw new IllegalArgumentException("null inScheme is not accepted!");
        }        
        this.inScheme = inScheme;
    }

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        if ( uri == null){
            throw new IllegalArgumentException("nulluri is not accepted!");
        }
        
        this.uri =uri;
    }
}
