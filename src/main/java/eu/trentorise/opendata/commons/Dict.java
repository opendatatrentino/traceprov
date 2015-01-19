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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import java.util.List;
import java.util.Locale;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Represents a dictionary of a string that may have translations in several
 * languages. There can also be multiple strings for the same language, in this
 * case the first one is the preferred. Never pass null objects to methods, use
 * empty objects (such as empty String "", unknown Locale {@link Locale#ROOT},
 * etc...) instead.
 *
 * @author David Leoni <david.leoni@unitn.it>
 */
@ParametersAreNonnullByDefault
@Immutable
public final class Dict {

    private static final Dict INSTANCE = new Dict();

    private ImmutableListMultimap<Locale, String> strings;

    private Dict() {
        strings = ImmutableListMultimap.of();
    }

    /**
     * Creates an empty dictionary
     */
    public static Dict of() {
        return INSTANCE;
    }

    /**
     * Constructs a Dict with with the provided string(s) in the given locale
     *
     * @param strings a non-null string
     * @param locale if locale is unknown use {@link Locale#ROOT}
     */
    public static Dict of(Locale locale, String... strings) {
        Dict ret = new Dict();
        ret.strings = ImmutableListMultimap.<Locale, String>builder().putAll(locale, strings).build();
        return ret;
    }

    /**
     * Constructs a Dict with with the provided string(s). Strings will be under
     * unknown locale {@link Locale#ROOT}.
     *
     * @param strings a non-null string
     */
    public static Dict of(String... strings) {
        return of(Locale.ROOT, strings);
    }

    /**
     * Constructs a Dict with the provided strings. Strings will be under
     * unknown locale {@link Locale#ROOT}
     *
     * @param strings a non-null list of non-null strings
     */
    public static Dict of(List<String> strings) {
        return of(Locale.ROOT, strings);
    }

    /**
     * Constructs a Dict with the strings in the provided locale
     *
     * @param strings a non-null list of non-null strings
     * @param locale if locale is unknown use {@link Locale#ROOT}
     */
    public static Dict of(Locale locale, List<String> strings) {
        Dict ret = new Dict();
        ret.strings = ImmutableListMultimap.<Locale, String>builder().putAll(locale, strings).build();
        return ret;
    }

    private Dict(Builder dictBuilder) {
        this.strings = dictBuilder.stringsBuilder.build();
    }

    /**
     * Gets the translations in the given locale.
     *
     * @param locale the language of the desired translation
     * @return the strings in the given locale if present. If no string is
     * present an empty list is returned.
     *
     * @see #string(java.util.Locale)
     */
    public ImmutableList<String> strings(Locale locale) {
        Preconditions.checkNotNull(locale);
        if (strings.containsKey(locale)) {
            return strings.get(locale);
        } else {
            return ImmutableList.of();
        }
    }

    /**
     * Gets the first translation in the given locale.
     *
     * @param locale the language of the desired translation
     * @return the string in the given locale if present. If no string is
     * present the empty string is returned.
     */
    public String string(Locale locale) {
        List<String> rets = strings(locale);
        if (rets.isEmpty()) {
            return "";
        } else {
            return rets.get(0);
        }
    }

    /**
     * Returns true if there is at least one non-empty translation, otherwise
     * returns false
     */
    public boolean isEmpty() {
        for (Locale locale : locales()) {
            String t = nonEmptyString(locale);
            if (!t.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the locales for which translations are present in the dict
     *
     * @return the available locales
     */
    public ImmutableSet<Locale> locales() {
        return strings.keySet();
    }

    /**
     * Checks if provided text is contained in any of the provided translations.
     * Both text and translations to check are lowercased according to their
     * locale.
     *
     * @param text the text to search for
     * @return true if text is contained in any of the translations, false
     * otherwise
     */
    public boolean contains(String text) {
        Preconditions.checkNotNull(text);
        for (Locale loc : locales()) {
            String lowText = text.toLowerCase(loc);
            for (String t : strings.get(loc)) {
                if (t.toLowerCase(loc).contains(lowText)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Returns the first non empty string in the
     * given locale. If it can't find it, an empty string is returned.
     */
    public String nonEmptyString(Locale locale) {
        Preconditions.checkNotNull(locale);
        if (strings.containsKey(locale)) {
            List<String> as = strings(locale);

            if (as.isEmpty()) {
                return "";
            } else {
                for (String s : strings.get(locale)) {
                    if (!s.isEmpty()) {
                        return s;
                    }
                }
            }
        }
        return "";
    }

    /**
     *
     * Tries its best to produce a meaningful string in one of the provided
     * languages
     *
     * @return A string in the first available language from the list of
     * provided locales. If no translation is available, in order, defaults to
     * English and then whatever it can find in the list of translations. Empty
     * strings are discarded. If no valid translation is available at all,
     * returns {@link LocalizedString#of()}.
     */
    public LocalizedString prettyString(Locale... locales) {
        Preconditions.checkNotNull(locales);

        for (Locale loc : locales) {
            Preconditions.checkNotNull(loc);
            String t = nonEmptyString(loc);
            if (!t.isEmpty()) {
                return LocalizedString.of(loc, t);
            }
        }
        String t = nonEmptyString(Locale.ENGLISH);
        if (!t.isEmpty()) {
            return LocalizedString.of(Locale.ENGLISH, t);
        }

        for (Locale loc : locales()) {
            String other = nonEmptyString(loc);
            if (!other.isEmpty()) {
                return LocalizedString.of(loc, other);
            }
        }
        return LocalizedString.of();
    }

    /**
     *
     * Returns a new dictionary with provided array of strings with the same
     * locale.
     *
     * @param locale the locale of the strings
     * @param strings the strings in the given locale
     *
     */
    public Dict with(Locale locale, String... strings) {
        return Dict.builder().putAll(this).putAll(locale, strings).build();
    }
    
    /**
     *
     * Returns a new dictionary with provided array of strings with the same
     * locale unknown locale {@link Locale#ROOT}.
     *
     * @param strings the strings in the given locale
     *
     */
    public Dict with(String... strings) {
        return Dict.builder().putAll(this).putAll(Locale.ROOT, strings).build();
    }    

    /**
     *
     * Returns a new dictionary with provided array of strings with the same
     * locale.
     *
     * @param locale the locale of the strings
     * @param strings the strings in the given locale
     *
     */
    public Dict with(Locale locale, Iterable<String> strings) {
        return Dict.builder().putAll(this).putAll(locale, strings).build();
    }
    
    /**
     *
     * Returns a new dictionary which is the result of merging this dictionary with the provided one..
     * New locales and strings follow any existing locales and strings.
     * 
     */
    public Dict with(Dict dict) {
        return Dict.builder().putAll(this).putAll(dict).build();
    }    

    /**
     * Returns a string with msg padded with white spaces from the left until
     * maxLength is reached
     *
     * @param msg the message to pad with spaces
     * @param maxLength length after which msg is truncated
     * @return the padded msg
     */
    private static String padLeft(String msg, int maxLength) {
        Preconditions.checkNotNull(msg);

        String nmot;
        if (msg.length() > maxLength) {
            nmot = msg.substring(0, msg.length() - 3) + "...";
        } else {
            nmot = String.format("%" + maxLength + "s", msg);
        }
        return nmot;
    }

    /**
     * Builds instances of {@link Dict}. Initialized attributes and then invoke
     * {@link #build()} method to create immutable instance.
     * <p>
     * <em>Builder is not thread safe and generally should not be stored in
     * field or collection, but used immediately to create instances.</em>
     */
    @NotThreadSafe
    public static final class Builder {

        private Builder() {
        }

        private ImmutableListMultimap.Builder<Locale, String> stringsBuilder = ImmutableListMultimap.<Locale, String>builder();

        /**
         * Stores an array of values with the same locale in the built
         * dictionary.
         *
         * @param locale the locale of the string
         * @param strings the string in the given locale
         * @return {@code this} builder for chained invocation
         */
        public Builder putAll(Locale locale, String... strings) {
            stringsBuilder.putAll(locale, strings);
            return this;
        }

        public Dict build() {
            return new Dict(this);
        }

        /*
         * Stores a collection of values with the same locale in the built dictionary.
         *
         * @param locale the locale of the string
         * @param string the string in the given locale
         * @return {@code this} builder for chained invocation
         */
        public Builder putAll(Locale locale, Iterable<String> strings) {
            stringsBuilder.putAll(locale, strings);
            return this;
        }

        /**
         * Stores a collection of values with default locale {@link Locale#ROOT}
         * in the built dictionary.
         *
         * @return {@code this} builder for chained invocation
         */
        public Builder putAll(String... strings) {
            stringsBuilder.putAll(Locale.ROOT, strings);
            return this;
        }

        /**
         * Stores another dictionary's entries in the built dictionary. New
         * locales and strings follow any existing locales and strings.
         */
        public Builder putAll(Dict dict) {
            stringsBuilder.putAll(dict.strings);
            return this;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.strings != null ? this.strings.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dict other = (Dict) obj;
        if (this.strings != other.strings && (this.strings == null || !this.strings.equals(other.strings))) {
            return false;
        }
        return true;
    }

    /**
     * Creates builder for {@link Dict}.
     *
     * @return new Dict builder
     */
    public static Dict.Builder builder() {
        return new Dict.Builder();
    }

    /**
     * Returns all the strings in the dictionary in a nicely formatted way.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("{\n");
        for (Locale loc : strings.keySet()) {
            sb.append(padLeft(loc.toString(), 10))
                    .append(": [");
            boolean first = true;
            for (String t : strings.get(loc)) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(t);
            }
            sb.append("]\n");

        }
        sb.append("}\n");
        sb.append("\n");
        return sb.toString();
    }

}
