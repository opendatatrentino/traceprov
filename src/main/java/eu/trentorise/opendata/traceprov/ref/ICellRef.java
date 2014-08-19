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
 * Represents a reference to a cell in a dataset in tabular format
 *
 * @author David Leoni
 */
public interface ICellRef {

    /**
     * Returns the index of column the cell belongs to.
     */
    int getColumnIndex();

    /**
     * Returns the index of row the cell belongs to.
     */    
    int getRowIndex();

}
