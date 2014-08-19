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

package eu.trentorise.opendata.traceprov.ref;

/**
 * Represent a reference to an element of a dcat dataset, like for example the title
 * @author David Leoni
 */
public interface IDcatMetadataRef {
    
    static public String DUBLIC_CORE_TERMS_TITLE = "http://purl.org/dc/terms/title";
    
    /**
     * Let's say we are referring to a title of a dcat dataset, then we would
     * return "http://purl.org/dc/terms/title", as dcat express title with a
     * Dublin core vocabulary attribute
     */
    String getPropertyURI();

}
