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
package eu.trentorise.opendata.traceprov.services;

import java.io.OutputStream;

/**
 * <h3>Example</h3>
 * Suppose we have an original CSV table like this:
 * <pre>
 *      h1,h2
 *      aa,ab
 *      ba,bb
 * </pre> its JSON view format is supposed to be like this:
 *
 * <pre>
 *  [
 *      ["h1","h2"],
 *      ["aa","ab"],
 *      ["ba","bb"]
 *  ]
 * </pre>
 * <p>
 * We don't use an array of records as original header names may be empty or
 * duplicated. Thus cell "ba" can be pinpointed with the JsonPath expression
 * $[2][0] <br/>
 * The first column can be selected with JsonPath expression $[*][0]
 * </p>
 * <p>
 * Once CSV is correctly loaded and transformed in a proper common tree
 * representation, then we can afford to have a more user friendly version with
 * records like this:
 *
 * <pre>
 *      [
 *          {
 *              "h1":"aa",
 *              "h2":"ab"
 *          },
 *           {
 *              "h1":"ba",
 *              "h2":"bb"
 *          }
 *      ]
 * </pre> First column can be selected with JsonPath $[*].h1
 * </p>
 *
 * @author David Leoni
 */
// todo implement
public class CsvLoader implements ILoader {

    @Override
    public void load(String url, OutputStream output, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
