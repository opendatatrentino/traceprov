package eu.trentorise.opendata.traceprov.engine;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static eu.trentorise.opendata.commons.validation.Preconditions.checkNotEmpty;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import eu.trentorise.opendata.traceprov.exceptions.EngineException;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvException;
import eu.trentorise.opendata.traceprov.exceptions.TraceProvNotFoundException;
import eu.trentorise.opendata.traceprov.tracel.java.PropertyPath;

/**
 * A Javascript execution engine. Current implementation uses Rhino interpreter.
 * 
 * <p>
 * All engine executions share a common environment (known as <i>scope</i> in
 * Rhino) plus a new environment created for each execution. Variables and
 * methods defined in the common environment (which are in
 * src/main/typescript/... /tracel) must not be changed by scripts (todo is it
 * possible to enforce this??).
 * </p>
 * 
 * <pre>
 *  TODO: define EcmaScript target (5 or 6?)  -  assume 5 
 *  is TypeScript supported?  assume no
 *  is Multithreaded?  assume no
 * </pre>
 */
public class Engine {

    private static final Logger LOG = Logger.getLogger(Engine.class.getSimpleName());

    private static final String TRACEL_FILE_PATH = "typescript/eu/trentorise/opendata/traceprov/tracel/";
    private static final String DEV_TRACEL_FILE_PATH = "src/main/" + TRACEL_FILE_PATH;

    /**
     * Shared Rhino scope.
     */
    @Nullable
    private static Scriptable sharedScope;

    private Engine() {
    }

    /**
     * Creates a new Engine. See {@link Engine class documentation} to see how
     * engines share environments.
     * 
     */
    public static Engine of() {
        return new Engine();
    }
   
    
    /**
     * Locates a tracel file, looking first in the dev directory
     *
     * @param path
     *            a file in the tracel directory, i.e. "js/myfile.js"
     *
     * @throws TraceProvNotFoundException
     *             if file is not found.
     */
    private File getTracelFile(String path) {
        checkNotEmpty(path, "Invalid path!");

        File ret = new File(DEV_TRACEL_FILE_PATH + path);
        if (ret.exists()) {
            return ret;
        }
        
        URL url = Engine.class.getResource("/" + TRACEL_FILE_PATH + path);
        
        try {
          ret = new File(url.toURI());
        } catch(URISyntaxException e) {
          ret= new File(url.getPath());
        }
                
        if (!ret.exists()){
            throw new TraceProvNotFoundException("Can't find tracel path " + path + "!");
        } else {
            return ret;
        }

    }

    /**
     * 
     * @param scope
     * @param code
     * @param scriptName
     * @throws eu.trentorise.opendata.traceprov.exceptions.EngineException
     */
    private Object execute(Scriptable scope, String code, String scriptName) {
        checkNotNull(scope);
        checkNotEmpty(scriptName, "Invalid script name!");
        checkNotEmpty("Invalid script code!", code);

        Context cx = Context.enter();

        try {

            // so we don't get stupid 64kb function limit error. todo find way
            // to
            // reenable opt
            cx.setOptimizationLevel(-1);

            cx.setLanguageVersion(Context.VERSION_1_8);

            try {
                return cx.evaluateString(scope, code, scriptName, 1, null);
            } catch (Exception ex) {
                throw new EngineException("Error while evaluating script " + scriptName + "!", ex);
            }

        } finally {
            Context.exit();
        }

    }

    /**
     * Executes a file in a given scope.
     *
     * @param a
     *            file to execute
     * 
     * @param scope
     *            todo
     * @throws TraceProvNotFoundException
     *             if file is not found.
     * @throws eu.trentorise.opendata.traceprov.exceptions.EngineException
     */
    private Object execute(Scriptable scope, File file) {
        checkNotNull(scope);
        checkNotNull(file);

        FileReader reader;
        String code;

        try {
            code = FileUtils.readFileToString(file);
        } catch (IOException ex) {
            throw new TraceProvException("Error while reading script in file " + file.getAbsolutePath(), ex);
        }
        return execute(scope, code, file.getAbsolutePath());
    }

    /**
     * Executes provided javascript code and returns the result.
     * 
     * @return the result of the Javascript computation.
     * @throws eu.trentorise.opendata.traceprov.exceptions.EngineException
     */
    public Object execute(String code) {
        Context cx = Context.enter();

        try {

            // so we don't get stupid 64kb function limit error. todo find wayto
            // reenable opt
            cx.setOptimizationLevel(-1);

            cx.setLanguageVersion(Context.VERSION_1_8);

            if (sharedScope == null) {

                LOG.info("Initializing shared Javascript scope ...");
                // todo we might want to seal the sharedScope, see
                // https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino/Scopes_and_Contexts#Sealed_shared_scopes
                Global global = new Global(cx); // todo we need a custom scope,
                                                // this
                // one has too much stuff other than
                // print
                sharedScope = cx.initStandardObjects(global);

                execute(sharedScope, getTracelFile("engine-init.js"));
                execute(sharedScope, getTracelFile("node_modules/typescript/lib/typescriptServices.js"));
                execute(sharedScope, getTracelFile("js/tracel.js"));

                LOG.info("Done initializing shared Javascript scope.");
            }

            Scriptable newScope = cx.newObject(sharedScope);
            newScope.setPrototype(sharedScope);
            newScope.setParentScope(null);

            return execute(newScope, code, "temporary-script.js");

        } finally {
            Context.exit();
        }
    }

    public static boolean isValidId(String id) {
        checkNotNull(id);
        return !id.isEmpty() && !id.contains(" ") && !Character.isDigit(id.charAt(0));
    }

    public static void checkValidId(String id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid identifier! Found " + id);
        }

    }

    /**
     * 
     * Checks provided path can start from given {@code rootClass}.
     * 
     * @param propertyPath
     *            a path of property names like dataset, themes, uri,
     *            "my Absurdly Named Property". Root field of the path must be
     *            among the fields or {@code rootClass}
     * 
     * @throws IllegalArgumentException
     *             if {@code propertyPath} does not correspond to actual fields
     *             in the java classes.
     * 
     */
    public static PropertyPath checkPathFromClass(Class rootClass, PropertyPath propertyPath) {

        checkNotNull(rootClass);

        Class curClass = rootClass;

        Iterator<String> iter = propertyPath.labels()
                                            .iterator();

        int i = 0;
        while (iter.hasNext()) {

            String property = iter.next();
            try {
                BeanInfo info = Introspector.getBeanInfo(curClass);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();

                boolean foundProperty = false;
                for (PropertyDescriptor pd : pds) {

                    if (pd.getName()
                          .equals(property)) {
                        foundProperty = true;
                        Class candidateClass = pd.getReadMethod()
                                                 .getReturnType();

                        if (Collection.class.isAssignableFrom(candidateClass)) {
                            try {
                                curClass = getCollectionType(pd.getReadMethod());
                                break;
                            } catch (Exception ex) {

                                LOG.log(Level.WARNING,
                                        "Error while parsing method types, accepting path as it is " + propertyPath,
                                        ex);
                                return propertyPath;
                            }
                        }
                        curClass = candidateClass;
                        break;
                    }
                }
                if (!foundProperty) {
                    throw new IllegalArgumentException(
                            "Couldn't find property " + property + " in property path " + propertyPath);
                }
            } catch (IntrospectionException ex) {
                throw new RuntimeException("Couldn't read bean properties from class " + curClass, ex);
            }
            i++;
        }
        return propertyPath;
    }

    /**
     * Looks in the return type of provided method and gives back the type of a
     * collection, like String in {@code List<String>}
     *
     * @throws IllegalArgumentException
     *             if method doesn't return generics
     */
    private static Class getCollectionType(Method method) {

        Type[] genericParameterTypes = method.getGenericParameterTypes();

        for (Type genericParameterType : genericParameterTypes) {
            if (genericParameterType instanceof ParameterizedType) {
                ParameterizedType aType = (ParameterizedType) genericParameterType;
                Type[] parameterArgTypes = aType.getActualTypeArguments();
                for (Type parameterArgType : parameterArgTypes) {
                    Class parameterArgClass = (Class) parameterArgType;
                    return parameterArgClass;
                }
            }
        }
        throw new IllegalArgumentException("Couldn't find generic type argument in method " + method);
    }

    /**
     * Returns the json path for cells in a table without headers. See
     * {@link eu.trentorise.opendata.traceprov.validation.CsvValidator} for
     * examples of tabular models.
     *
     * @param root
     *            the
     * @param rowIndex
     *            the row index, starting from 0. To select all rows, use -1
     * @param columnIndex
     *            the column index, starting from 0. To select all columns, use
     *            -1
     */
    public static PropertyPath tablePath(PropertyPath rootPath, long rowIndex, long colIndex) {
        checkArgument(rowIndex >= -1, "row index must be >= -1, found instead %s", rowIndex);
        checkArgument(colIndex >= -1, "col index must be >= -1, found instead %s", colIndex);
        String r = rowIndex == -1 ? "ALL" : Long.toString(rowIndex);
        String c = colIndex == -1 ? "ALL" : Long.toString(colIndex);

        return rootPath.appendProperties(r, c);
    }

    /**
     * Returns the json path for cells in a table with headers. See
     * {@link eu.trentorise.opendata.traceprov.services.CsvValidator} for
     * examples of tabular models.
     *
     * @param rowIndex
     *            the row index, starting from 0. To select all rows, use -1
     * @param header.
     *            To select all headers, use ALL
     */
    public static PropertyPath tablePath(PropertyPath rootPath, long rowIndex, String header) {
        checkArgument(rowIndex >= -1, "row index must be >= -1, found instead %s", rowIndex);
        checkNotEmpty(header, "Invalid header! To select all headers use ALL", "");

        String r = rowIndex == -1 ? "ALL" : Long.toString(rowIndex);

        return rootPath.appendProperties(r, header);
    }

}
