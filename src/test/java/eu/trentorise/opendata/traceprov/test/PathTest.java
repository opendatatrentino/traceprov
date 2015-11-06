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
package eu.trentorise.opendata.traceprov.test;

import com.google.common.collect.ImmutableList;
import com.jayway.jsonpath.internal.CompiledPath;
import com.jayway.jsonpath.internal.Path;
import com.jayway.jsonpath.internal.PathCompiler;
import com.jayway.jsonpath.internal.token.PathToken;
import com.jayway.jsonpath.internal.token.PropertyPathToken;
import com.jayway.jsonpath.internal.token.RootPathToken;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author David Leoni
 */
public class PathTest {

    private static final Logger LOG = Logger.getLogger(PathTest.class.getName());

    /**
     * Hacky way to access JsonPath internal compiled path.
     */
   private PathToken takeRoot(CompiledPath compiledPath) {
        try {
            Field f = compiledPath.getClass().getDeclaredField("root"); //NoSuchFieldException
            f.setAccessible(true);
             return (PathToken) f.get(compiledPath); //IllegalAccessException
        }
        catch (NoSuchFieldException |SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Couldn't access root in Jayway JsonPath CompiledPath implementation! ");
        }
    }

   private static class MyPathToken extends RootPathToken {
       void blip(){
    
       }
   }
   
    @Test
    public void test() {
        PropertyPathToken propertyPathToken = new PropertyPathToken(ImmutableList.of("bli", "blo"));
        CompiledPath path = (CompiledPath) PathCompiler.compile("a[*].b");
        LOG.log(Level.FINE, "path is {0}", path);
        PathToken root = takeRoot(path);
        if (root instanceof RootPathToken){
            RootPathToken rp = (RootPathToken) root;
    
        
        }
        //assertEquals(root.)
        LOG.log(Level.FINE, "path is {0}", root);
    }
}
