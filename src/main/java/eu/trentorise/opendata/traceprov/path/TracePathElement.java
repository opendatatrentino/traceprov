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
package eu.trentorise.opendata.traceprov.path;

import java.util.Iterator;
import javax.validation.ElementKind;
import javax.validation.Path;

/**
 * TODO much work in progress
 * Inspired by  {@link Path.Node} of java validation api
 */
public abstract class TracePathElement implements Path.Node {
    
    
    /**
     * From Java validation api
     *
     * ----- todo This is the loooong Java validation spec: -----  <br/>
     *
     * Returns the name of the element which the node represents:
     * <ul>
     * <li>{@code null} if it is a leaf node which represents an entity / bean.
     * In particular, the node representing the root object.</li>
     * <li>The property name for a property.</li>
     * <li>The method name for a method.</li>
     * <li>The unqualified name of the type declaring the constructor for a
     * constructor.</li>
     * <li>The parameter named as defined by the {@link ParameterNameProvider}
     * for a method or constructor parameter.</li>
     * <li>The literal {@code <cross-parameter>} for a method or constructor
     * cross-parameter.</li>
     * <li>The literal {@code <return value>} for a method or constructor return
     * value.</li>
     * </ul>
     *
     * @return name of the element which the node represents
     */
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * From Java validation api
     *
     * @return {@code true} if the node represents an object contained in an
     * {@code Iterable} or in a {@code Map}, {@code false} otherwise
     */
    @Override
    public boolean isInIterable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * From Java validation api
     *
     * @return the index the node is placed in if contained in an array or
     * {@code List}; {@code null} otherwise
     */
    @Override
    public Integer getIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * From Java validation api
     *
     * @return the key the node is placed in if contained in a {@code Map},
     * {@code null} otherwise
     */
    @Override
    public Object getKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * From Java validation api ----- todo This is the loooong Java validation
     * spec: -----  <br/>
     *
     * The kind of element represented by the node. The following relationship
     * between an {@link ElementKind} and its {@code Node} subtype exists:
     * <ul>
     * <li>{@link ElementKind#BEAN}: {@link BeanNode}</li>
     * <li>{@link ElementKind#PROPERTY}: {@link PropertyNode}</li>
     * <li>{@link ElementKind#METHOD}: {@link MethodNode}</li>
     * <li>{@link ElementKind#CONSTRUCTOR}: {@link ConstructorNode}</li>
     * <li>{@link ElementKind#PARAMETER}: {@link ParameterNode}</li>
     * <li>{@link ElementKind#CROSS_PARAMETER}: {@link CrossParameterNode}</li>
     * <li>{@link ElementKind#RETURN_VALUE}: {@link ReturnValueNode}</li>
     * </ul>
     * <p/>
     * This is useful to narrow down the {@code Node} type and access node
     * specific information:
     * <pre>
     * switch(node.getKind() {
     * case METHOD:
     *     name = node.getName();
     *     params = node.as(MethodNode.class).getParameterTypes();
     * case PARAMETER:
     *     index = node.as(ParameterNode.class).getParameterIndex();
     * [...]
     * }
     * </pre>
     *
     * @return the {@code ElementKind}
     *
     */
    @Override
    public ElementKind getKind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * From Java validation api ----- todo This is the loooong Java validation
     * spec: -----  <br/>
     *
     * Narrows the type of this node down to the given type. The appropriate
     * type should be checked before by calling {@link #getKind()}.
     *
     * @param <T> the type to narrow down to
     * @param nodeType class object representing the descriptor type to narrow
     * down to to
     *
     * @return this node narrowed down to the given type.
     *
     * @throws ClassCastException If this node is not assignable to the type
     * {@code T}
     */
    @Override
    public <T extends Path.Node> T as(Class<T> nodeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    // *********** JSONPATH stuff
    
    /**
     * From JsonPath 
     */
    public abstract String getPathFragment();

    /**
     * From JsonPath 
     */    
    public abstract boolean isTokenDefinite();

    /**
     * From JsonPath 
     */    
    public boolean isLeaf() {
        throw new UnsupportedOperationException("todo implement me");
        //return getNext() == null;
    }

    /**
     * From JsonPath 
     * 
     * Accepts a collection of objects, since all objects have toString()
     *
     * @param wrap first object is wrapped between {@code wrap}.
     */
    protected static String join(String delimiter, String wrap, Iterable<? extends Object> objs) {
        Iterator<? extends Object> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(wrap).append(iter.next()).append(wrap);
        while (iter.hasNext()) {
            buffer.append(delimiter).append(wrap).append(iter.next()).append(wrap);
        }
        return buffer.toString();
    }

}

/**
 * @deprecated just a silly experiment
 */
abstract class TrialPathNode implements Path.Node {
    /**
     * From Java validation api
     *
     * ----- todo This is the loooong Java validation spec: -----  <br/>
     *
     * Returns the name of the element which the node represents:
     * <ul>
     * <li>{@code null} if it is a leaf node which represents an entity / bean.
     * In particular, the node representing the root object.</li>
     * <li>The property name for a property.</li>
     * <li>The method name for a method.</li>
     * <li>The unqualified name of the type declaring the constructor for a
     * constructor.</li>
     * <li>The parameter named as defined by the {@link ParameterNameProvider}
     * for a method or constructor parameter.</li>
     * <li>The literal {@code <cross-parameter>} for a method or constructor
     * cross-parameter.</li>
     * <li>The literal {@code <return value>} for a method or constructor return
     * value.</li>
     * </ul>
     *
     * @return name of the element which the node represents
     */
    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * From Java validation api
     *
     * @return {@code true} if the node represents an object contained in an
     * {@code Iterable} or in a {@code Map}, {@code false} otherwise
     */
    @Override
    public boolean isInIterable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * From Java validation api
     *
     * @return the index the node is placed in if contained in an array or
     * {@code List}; {@code null} otherwise
     */
    @Override
    public Integer getIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * From Java validation api
     *
     * @return the key the node is placed in if contained in a {@code Map},
     * {@code null} otherwise
     */
    @Override
    public Object getKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * From Java validation api ----- todo This is the loooong Java validation
     * spec: -----  <br/>
     *
     * The kind of element represented by the node. The following relationship
     * between an {@link ElementKind} and its {@code Node} subtype exists:
     * <ul>
     * <li>{@link ElementKind#BEAN}: {@link BeanNode}</li>
     * <li>{@link ElementKind#PROPERTY}: {@link PropertyNode}</li>
     * <li>{@link ElementKind#METHOD}: {@link MethodNode}</li>
     * <li>{@link ElementKind#CONSTRUCTOR}: {@link ConstructorNode}</li>
     * <li>{@link ElementKind#PARAMETER}: {@link ParameterNode}</li>
     * <li>{@link ElementKind#CROSS_PARAMETER}: {@link CrossParameterNode}</li>
     * <li>{@link ElementKind#RETURN_VALUE}: {@link ReturnValueNode}</li>
     * </ul>
     * <p/>
     * This is useful to narrow down the {@code Node} type and access node
     * specific information:
     * <pre>
     * switch(node.getKind() {
     * case METHOD:
     *     name = node.getName();
     *     params = node.as(MethodNode.class).getParameterTypes();
     * case PARAMETER:
     *     index = node.as(ParameterNode.class).getParameterIndex();
     * [...]
     * }
     * </pre>
     *
     * @return the {@code ElementKind}
     *
     */
    @Override
    public ElementKind getKind() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * From Java validation api ----- todo This is the loooong Java validation
     * spec: -----  <br/>
     *
     * Narrows the type of this node down to the given type. The appropriate
     * type should be checked before by calling {@link #getKind()}.
     *
     * @param <T> the type to narrow down to
     * @param nodeType class object representing the descriptor type to narrow
     * down to to
     *
     * @return this node narrowed down to the given type.
     *
     * @throws ClassCastException If this node is not assignable to the type
     * {@code T}
     */
    @Override
    public <T extends Path.Node> T as(Class<T> nodeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}