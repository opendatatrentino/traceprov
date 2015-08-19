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
import eu.trentorise.opendata.commons.BuilderStylePublic;
import java.util.List;
import org.immutables.value.Value;

/**
 * The schema of a json, loosely modeled after what you can express with
 * a jsonld context and Typescript.
 *
 * @author David Leoni
 */
@Value.Immutable
@BuilderStylePublic
@JsonSerialize(as = ClassType.class)
@JsonDeserialize(as = ClassType.class)
abstract class AClassType extends Type {

    private static final long serialVersionUID = 1L;
   
    /**
     * The property definitions of the class
     */
    public abstract List<Def> getPropertyDefs();

    /**
     * The unique indexes tht may constrain sets of schema values to be unique
     * in the array they are in.
     *
     */
    public abstract List<UniqueIndex> getUniqueIndexes();

    @Override
    public Class getJavaClass(){
        return Class.class;
    }
/*
    @Override
    public String getDatatypeStandardId(){
        return "https://schema.org/Class";
    }*/
    
    

 

}
