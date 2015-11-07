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
import eu.trentorise.opendata.commons.Dict;
import eu.trentorise.opendata.commons.TodConfig;
import eu.trentorise.opendata.traceprov.engine.CastResult;
import eu.trentorise.opendata.traceprov.engine.Converter;
import eu.trentorise.opendata.traceprov.engine.WeightedResult;
import eu.trentorise.opendata.traceprov.types.DictType;
import eu.trentorise.opendata.traceprov.types.JavaDateType;
import eu.trentorise.opendata.traceprov.types.ListType;
import eu.trentorise.opendata.traceprov.types.StringType;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO - converters so far are a much a fuzzy thing
 * @author David Leoni
 */
public class ConverterTest {

    @BeforeClass
    public static void setUpClass() {
        TodConfig.init(ConverterTest.class);
    }

    private static class MyDateConverter extends Converter<StringType, JavaDateType> {

        @Override
        public boolean isLongRunning() {
            return false;
        }

        @Override
        public StringType getSourceType() {
            return StringType.of();
        }

        @Override
        public JavaDateType getTargetType() {
            return JavaDateType.of();
        }

        @Override
        public CastResult cast(Object source, Locale sourceLocale) {
            if (source instanceof String) {
                String s = (String) source;
                try {
                    return CastResult.builder()
                            .setAccuracy(0.6)
                            .addCandidates(WeightedResult.of(0.9, DateFormat.getInstance().parse(s)))
                            .build();
                }
                catch (ParseException ex) {
                    return CastResult.of();
                }
            } else {
                return CastResult.builder()
                        .setAccuracy(0.0)
                        .build();
            }
        }

        @Override
        public boolean isLossy() {
            return false;
        }


    }

    private static class MyStringListToDictConverter extends Converter<ListType, DictType> {

        private static final MyStringListToDictConverter INSTANCE = new MyStringListToDictConverter();
        
        private MyStringListToDictConverter() {
        }

        
        @Override
        public boolean isLongRunning() {
            return false;
        }

        @Override
        public ListType getSourceType() {
            return ListType.of(StringType.of());
        }

        @Override
        public DictType getTargetType() {
            return DictType.of();
        }

        @Override
        public CastResult cast(Object source, Locale sourceLocale) {
            Dict.Builder builder = Dict.builder();
            if (source instanceof Iterable) {
                Iterable so = (Iterable) source;
                for (Object obj : so) {
                    if (obj instanceof String) {
                        builder.put((String) obj);
                    }
                }
                return CastResult.of(builder.build(), 1.0);
            } else {
                return CastResult.of(); // zero accuracy...
            }
        }

        @Override
        public boolean isLossy() {
            return false;
        }

        public static MyStringListToDictConverter of(){
            return INSTANCE;
        }
        
    }

    @Test
    @Ignore
    public void testConverter() {
        CastResult convert = MyStringListToDictConverter.of().cast(ImmutableList.of("a","b"), Locale.ROOT);
        
        assertTrue(convert.getAccuracy()>= 1.0);
        assertEquals(Dict.of("a", "b"), convert.value());
        assertEquals(1, convert.getCandidates().size());
        assertTrue(convert.completelySucceded());

    }

}
