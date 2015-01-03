package eu.trentorise.opendata.traceprov.dcat;

import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable(singleton = true)
@Value.Style(get = {"is*", "get*"}, init = "set*", typeAbstract = {"Abstract*"}, typeImmutable = "" )
public abstract class  AbstractFoafPerson extends AbstractFoafAgent {
    
}
