/* 
 * Copyright 2015 Trento Rise  (trentorise.eu) 
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
package eu.trentorise.opendata.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;

/**
 * Annotated abstract class (let's suppose it's named AbstractMyClass) will be
 * used as template for generating a corresponding immutable class named
 * MyClass, along with a builder to create instances of it.
 *
 * This annotation will configure
 * <a href="http://immutables.github.io/">Immutables</a> to expect the annotated
 * class to have bean style getters. The generated builder will have bean style
 * setters. Also, generated immutable objects will all have an empty object
 * retrievable with a method of the form ImmutableMyClass.of(). Immutable class
 * will have same visibility as the abstract one.
 *
 * NOTE: Annotated abstract class name MUST begin with 'Abstract'.
 * 
 * @author David Leoni
 * @see SimpleStyle
 */
@Value.Style(get = {"is*", "get*"},
        init = "set*",
        typeAbstract = {"Abstract*"},
        typeImmutable = "",
        defaults = @Value.Immutable(singleton = true))
@Target({ElementType.TYPE, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface BuilderStyle {
}

