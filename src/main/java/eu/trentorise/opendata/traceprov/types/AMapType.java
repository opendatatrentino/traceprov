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
package eu.trentorise.opendata.traceprov.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import eu.trentorise.opendata.commons.SimpleStyle;
import static eu.trentorise.opendata.traceprov.types.Types.XSD;
import java.util.Map;
import org.immutables.value.Value;

/**
 *
 * @author David Leoni
 */
@Value.Immutable
@SimpleStyle
@JsonSerialize(as = MapType.class)
@JsonDeserialize(as = MapType.class)
abstract class AMapType extends Type {

    private static final long serialVersionUID = 1L;

    /**
     * By default assumes {@link StringType#of()}, although {@link IntType#of()}
     * is also supported.
     */
    @Value.Default
    public Type getKeyType() {
	return StringType.of();
    }

    /**
     * By default assumes {@link AnyType#of()}.
     */
    @Value.Default
    public Type getValueType() {
	return AnyType.of();
    }

    /*
     * @Override public String getDatatypeStandardId(){ return XSD +
     * "complexType"; }
     */

    @Override
    public Class getJavaClass() {
	return Map.class;
    }

    @Override
    public boolean isImmutable() {
	return getKeyType().isImmutable() && getValueType().isImmutable();
    }
}
