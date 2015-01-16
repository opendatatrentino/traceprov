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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/**
 * Class to configuration and initialize logging.
 *
 * @author David Leoni
 */
public abstract class OdtConfig {

    public static String LOG_PROPERTIES_PATH = "META-INF/odt-commons-log.properties";

    public static String BUILD_PROPERTIES_PATH = "META-INF/odt-commons-build.properties";
    
    protected Logger logger;

    protected boolean loggingConfigured;

    @Nullable
    private BuildInfo buildInfo;

    protected OdtConfig() {
        loggingConfigured = false;
        logger = Logger.getLogger(this.getClass().getName());    
    }

        
    /**
     * Returns build information. In case it is not available, returns
     * {@link BuildInfo#of()}.
     */
    public BuildInfo getBuildInfo() {
        if (buildInfo == null) {
            try {
                buildInfo = OdtUtils.readBuildInfo(this.getClass());
            }
            catch (Exception ex) {
                logger.log(Level.SEVERE, "COULD NOT LOAD BUILD INFORMATION! DEFAULTING TO EMPTY BUILD INFO!", ex);
                buildInfo = BuildInfo.of();
            }
        }
        return buildInfo;
    }

    /**
     * Configure logging by reading properties in {@link #LOG_PROPERTIES_PATH}
     * file. If you're using the library in your application and you have your
     * own logger system, don't call this method and route JUL to your logger
     * API instead.
     */
    public void loadLogConfig() {
        if (loggingConfigured) {
            logger.finest("Trying to reload twice logger properties!");
        } else {
            System.out.println(this.getClass().getSimpleName() + ": Going to initialize logging...");
            
            URL url = getClass().getResource("/" + LOG_PROPERTIES_PATH);
                        
            try {
                if (url == null){                                                        
                    throw new IOException("ERROR! COULDN'T FIND LOG CONFIGURATION FILE: " + LOG_PROPERTIES_PATH + ". To see debug logging messages during testing copy src/test/resources/META-INF-TEMPLATE content into src/test/resources/META-INF");
                }
                String path = url.toURI().getPath();         
                InputStream inputStream = this.getClass().getResourceAsStream("/" + LOG_PROPERTIES_PATH);
                LogManager.getLogManager().readConfiguration(inputStream);
                
                // IMPORTANT!!!! Due to a JDK bug, we need to create another useless logger to refresh actually ALL loggers (sic) . See https://www.java.net/forum/topic/jdk/java-se-snapshots-project-feedback/jdk-70-doesnt-refresh-handler-specific-logger                
                Logger loggerWorkaround  = Logger.getLogger(this.getClass().getName() + ".workaround"); 
                
                loggingConfigured = true;
                
                logger.log(Level.INFO, "Configured logging with file: {0}", path);              
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "ERROR - COULDN'T LOAD LOGGING PROPERTIES!", e);
            }
        }
    }


}
