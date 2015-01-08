package eu.trentorise.opendata.traceprov.dcat;

import eu.trentorise.opendata.traceprov.BuilderStyle;
import org.immutables.value.Value;

/**
 * Models a <a href="http://xmlns.com/foaf/0.1/Person"> foaf:Person </a>
 * @author David Leoni
 */
@Value.Immutable
@BuilderStyle
public abstract class AbstractFoafPerson extends AbstractFoafAgent {
    public static final String CLASS_URI="http://xmlns.com/foaf/0.1/Person";
}
