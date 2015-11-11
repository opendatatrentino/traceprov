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

import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Visitor for {@link TraceType} objects. default visit method is
 * {@link #visitDefault(eu.trentorise.opendata.traceprov.types.TraceType) visitDefault(Type)},
 * but you can define more specific {@code visit(MyType)} methods and those will
 * be automatically picked by a reflection mechanism.
 *
 * @author David Leoni
 */
public abstract class TypeVisitor {

    /**
     * Default visit method. Used only if no other {@code visit(MyType)} more
     * specific method has been found.
     */
    public abstract void visitDefault(TraceType type);

    /**
     * Retrieves most specific {@code visit} method. Defaults to
     * {@link #visit(eu.trentorise.opendata.traceprov.types.TraceType) visitDefault(Type)}
     *
     * @throws TraceProvException on error
     */
    protected Method getVisitMethod(TraceType type) {
        Class cl = type.getClass();  // the bottom-most class
        // Check through superclasses for matching method
        while (!cl.equals(Object.class)) {
            try {
                return getClass().getDeclaredMethod("visit", new Class[]{cl});
            }
            catch (NoSuchMethodException ex) {
                cl = cl.getSuperclass();
            }
        }
        // Check through interfaces for matching method
        Class[] interfaces = type.getClass().getInterfaces();
        for (Class intf : interfaces) {
            try {
                return getClass().getDeclaredMethod("visit", new Class[]{intf});
            }
            catch (NoSuchMethodException ex) {
            }
        }

        try {
            // debugMsg("Giving up");
            return getClass().getMethod("visitDefault",
                    new Class[]{(TraceType.class)});
        }
        catch (NoSuchMethodException ex) {
            throw new TraceProvException("Internal error, looked for method " + getClass().getCanonicalName() + "'.visitDefault(Type)' but couldn't find it - and this means class is just broken! ", ex); // Can't happen

        }
    }

    /**
     *
     * Executes most specific {@code visit} method, defaulting to
     * {@link #visit(eu.trentorise.opendata.traceprov.types.TraceType) visitDefault(Type)}
     * if none is found.
     *
     * @throws TraceProvException on error
     */
    public void visit(TraceType type) {
        Method method = getVisitMethod(type);
        try {
            method.invoke(this, new Object[]{type});
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new TraceProvException("Internal error, tried to invoke method" + method + " but some Java oddity happened!", ex);
        }

    }

}
