/**
 * A transfomormation engine for data nodes
 * {@link eu.trentorise.opendata.traceprov.data.ANode}
 *
 * Desiderata:
 *
 * <ul>
 * <li>Transformations should take into account metadata (data accuracy,
 * license, spatial, temporal, ...) </li>
 * <li>Should be generic enough to work have different implementations, a dummy
 * one and possibly a Spark impl</li>
 * </ul>
 *
 * TODO much work in progress
 */
package eu.trentorise.opendata.traceprov.engine;
