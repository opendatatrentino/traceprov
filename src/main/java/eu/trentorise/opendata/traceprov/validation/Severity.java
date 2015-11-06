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
package eu.trentorise.opendata.traceprov.validation;

import javax.validation.Payload;

/**
 * The level of an error occurred during validation.
 */
public class Severity {

    /**
     * User should be informed that some non ideal situation was found and for
     * example some automation has been done which is probably what the user
     * wants.
     */
    public interface Info extends Payload {
    }

    /**
     * Some unusual situation happened and for example some possibly undesirable
     * automation has been done to fix the issue.
     */
    public interface Warning extends Payload {
    }

    /**
     * An unrecoverable error occurred.
     */
    public interface Severe extends Payload {
    }

}
