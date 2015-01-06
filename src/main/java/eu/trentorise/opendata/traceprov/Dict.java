package eu.trentorise.opendata.traceprov;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;

/**
 * *****************************************************************************
 * Copyright 2013-2014 Trento Rise (www.trentorise.eu/)
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License (LGPL)
 * version 2.1 which accompanies this distribution, and is available at
 *
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************
 */
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
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

    private ImmutableMap<Locale, ImmutableList<String>> _strings;

    private Dict() {
        _strings = ImmutableMap.of();
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
        Preconditions.checkNotNull(locale);
        Preconditions.checkNotNull(strings);

        Dict ret = new Dict();
        ret._strings = ImmutableMap.of(locale, ImmutableList.copyOf(strings));
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
     * @param string a non-null list of non-null strings
     */
    public static Dict of(List<String> strings) {
        return of(Locale.ROOT, strings);
    }

    /**
     * Constructs a Dict with the strings in the provided locale
     *
     * @param string a non-null list of non-null strings
     * @param locale if locale is unknown use {@link Locale#ROOT}
     */
    public static Dict of(Locale locale, List<String> strings) {
        Preconditions.checkNotNull(locale);
        Preconditions.checkNotNull(strings);

        Dict ret = new Dict();
        ret._strings = ImmutableMap.of(locale, ImmutableList.copyOf(strings));
        return ret;
    }

    private Dict(Builder dictBuilder) {
        ImmutableMap.Builder<Locale, ImmutableList<String>> mapBuilder = ImmutableMap.builder();

        for (Entry<Locale, ImmutableList.Builder<String>> entry : dictBuilder.stringsBuilder.entrySet()) {
            mapBuilder.put(entry.getKey(), entry.getValue().build());
        }
        this._strings = mapBuilder.build();
    }

    /**
     * Gets the translations in the given locale.
     *
     * @param locale the language of the desired translation
     * @return the strings in the given locale if present. If no string is
     * present an empty list is returned.
     *
     * @see #getTranslation
     */
    public ImmutableList<String> strings(Locale locale) {
        Preconditions.checkNotNull(locale);
        if (_strings.containsKey(locale)) {
            return _strings.get(locale);
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
        return _strings.keySet();
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
            for (String t : _strings.get(loc)) {
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
        if (_strings.containsKey(locale)) {
            List<String> as = strings(locale);

            if (as.isEmpty()) {
                return "";
            } else {
                for (String s : _strings.get(locale)) {
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

        private Map<Locale, ImmutableList.Builder<String>> stringsBuilder
                = new LinkedHashMap(); // hashmap, otherwise I can't get contents

        /**
         * Adds provided string(s) to the list of already inserted translations
         * for the given locale.
         *
         * @param locale the locale of the string
         * @param string the string in the given locale
         * @return {@code this} builder for chained invocation
         */
        public final Builder addStrings(Locale locale, String... string) {
            Preconditions.checkNotNull(locale);

            ImmutableList.Builder<String> listBuilder = stringsBuilder.get(locale);

            if (listBuilder == null) {
                stringsBuilder.put(locale, ImmutableList.<String>builder().add(string));
            } else {
                listBuilder.addAll(ImmutableList.copyOf(string));
            }
            return this;
        }

        public Dict build() {
            return new Dict(this);
        }

        /*
         * Adds provided strings to the list of already inserted translations
         * for the given locale.
         *
         * @param locale the locale of the string
         * @param string the string in the given locale
         * @return {@code this} builder for chained invocation
         */
        public final Builder addAllStrings(Locale locale, Iterable<String> elements) {
            Preconditions.checkNotNull(locale);

            ImmutableList.Builder<String> listBuilder = stringsBuilder.get(locale);

            if (listBuilder == null) {
                stringsBuilder.put(locale, ImmutableList.<String>builder().addAll(elements));
            } else {
                listBuilder.addAll(elements);
            }
            return this;
        }

        /**
         * Adds string to the list of already inserted translations using the
         * default locale {@link Locale#ROOT}
         *
         * @return {@code this} builder for chained invocation
         */
        public final Builder addString(String string) {
            Preconditions.checkNotNull(string);

            addStrings(Locale.ROOT, string);
            return this;
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this._strings != null ? this._strings.hashCode() : 0);
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
        if (this._strings != other._strings && (this._strings == null || !this._strings.equals(other._strings))) {
            return false;
        }
        return true;
    }

    /**
     * Merges dictionary with provided one to create a new dictionary.
     * Duplicates are eliminated. Translations of the second dictionary will be
     * appended after the ones from this dictionary.
     *
     * @param dict
     * @return a new dictionary resulting from the merge.
     */
    public Dict merge(Dict dict) {
        Preconditions.checkNotNull(dict);
        
        Dict.Builder db = Dict.builder();

        for (Locale localeA : _strings.keySet()) {
            ImmutableSet.Builder<String> setBuilder_1 = ImmutableSet.<String>builder();

            setBuilder_1.addAll(_strings.get(localeA));
            if (dict.locales().contains(localeA)) {
                setBuilder_1.addAll(dict.strings(localeA));

            }
            db.addAllStrings(localeA, setBuilder_1.build());
        }
        for (Locale localeB : dict.locales()) {
            if (!_strings.keySet().contains(localeB)) {
                ImmutableSet.Builder<String> setBuilder_2 = ImmutableSet.<String>builder();
                setBuilder_2.addAll(dict.strings(localeB));
                db.addAllStrings(localeB, setBuilder_2.build());
            }
        }

        return db.build();
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
        for (Locale loc : _strings.keySet()) {
            sb.append(padLeft(loc.toString(), 10))
                    .append(": [");
            boolean first = true;
            for (String t : _strings.get(loc)) {
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
