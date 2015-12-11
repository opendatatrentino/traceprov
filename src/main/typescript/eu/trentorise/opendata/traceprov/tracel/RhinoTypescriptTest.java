package eu.trentorise.opendata.traceprov.test.experimental.tracel;
/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/. */

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.Global;

/**
* Example of controlling the JavaScript execution engine.
*
* We evaluate a script and then manipulate the result.
*
*/
public class RhinoTypescriptTest {

    private static final String PREFIX = "src/test/java/eu/trentorise/opendata/traceprov/test/experimental/tracel";
    
   /**
    * Main entry point.
    *
    * Process arguments as would a normal Java program. Also
    * create a new Context and associate it with the current thread.
    * Then set up the execution environment and begin to
    * execute scripts.
 * @throws IOException 
    */
    @Test
   public void testRhino() throws IOException {
       Context cx = Context.enter();
       try {
         
           File typescriptServicesFile = new File(PREFIX + "/node_modules/typescript/lib/typescriptServices.js");
           
           File rhinoTestFile = new File(PREFIX + "/js/TsTest.js");
           
           String typescriptServicesString = FileUtils.readFileToString(typescriptServicesFile);
           
           String rhinoTestString = FileUtils.readFileToString(rhinoTestFile);
           
           // so we don't get stupid 64kb function limit error. todo find wayto reenable opt           
           cx.setOptimizationLevel(-1);
           
           cx.setLanguageVersion(Context.VERSION_1_8);

           Global global = new Global(cx); // todo we need a custom scope, this one has too much stuff other than print 
           Scriptable scope = cx.initStandardObjects(global);

           // Now we can evaluate a script. Let's create a new object
           // using the object literal notation.
           Object result1 = cx.evaluateString(scope, typescriptServicesString,
                                             "MySource", 1, null);
           
           cx.evaluateString(scope, "var console={log:print, error:print}; console.log('HELLO'); console.log('MY FRIEND');",
                   "MySource", 1, null);
           
           Object result2 = cx.evaluateString(scope, rhinoTestString,
                   "MySource", 1, null);
           
           Object obj =  scope.get("ts", scope);

           // Should print "obj == result" (Since the result of an assignment
           // expression is the value that was assigned)
           System.out.println("obj " + obj);
           
           List<String> result3 = (List) cx.evaluateString(scope, "parsePropertyPath('a.b')",
                   "MySource", 1, null);

           System.out.println("the parsed list is : " );
           for (String ob : result3){
               System.out.println(ob );              
           }
           
           
           // Should print "obj.a == 1"
          // System.out.println("obj.a == " + obj.get("a", obj));

           //Scriptable b = (Scriptable) obj.get("b", obj);

           // Should print "obj.b[0] == x"
           //System.out.println("obj.b[0] == " + b.get(0, b));

           // Should print "obj.b[1] == y"
           //System.out.println("obj.b[1] == " + b.get(1, b));

           // Should print {a:1, b:["x", "y"]}
           //Function fn = (Function) ScriptableObject.getProperty(obj, "toString");
           //System.out.println(fn.call(cx, scope, obj, new Object[0]));
       } finally {
           Context.exit();
       }
   }

}
