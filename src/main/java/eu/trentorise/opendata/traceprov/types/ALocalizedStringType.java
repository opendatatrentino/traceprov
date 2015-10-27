package eu.trentorise.opendata.traceprov.types;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import eu.trentorise.opendata.commons.LocalizedString;
import eu.trentorise.opendata.commons.SimpleStyle;



@Value.Immutable
@SimpleStyle
@JsonSerialize(as = LocalizedStringType.class)
@JsonDeserialize(as = LocalizedStringType.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ALocalizedStringType extends TraceType {

    private static final long serialVersionUID = 1L;
    
    @Override
    public Class getJavaClass() {
	return LocalizedString.class;
    }

    @Override
    public boolean isImmutable() {
	return true;
    }

}
