/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.jcr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Repository;

import org.apache.jackrabbit.name.QName;
import org.apache.xerces.util.XMLChar;

public class JCRUtil
{

    private static final String TRUE = "true";

    public static boolean supportsLevel2(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.LEVEL_2_SUPPORTED));
    }

    public static boolean supportsTransactions(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.OPTION_TRANSACTIONS_SUPPORTED));
    }

    public static boolean supportsVersioning(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.OPTION_VERSIONING_SUPPORTED));
    }

    public static boolean supportsObservation(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.OPTION_OBSERVATION_SUPPORTED));
    }

    public static boolean supportsLocking(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.OPTION_LOCKING_SUPPORTED));
    }

    public static boolean supportsSQLQuery(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.OPTION_QUERY_SQL_SUPPORTED));
    }

    public static boolean supportsXPathPosIndex(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.QUERY_XPATH_POS_INDEX));
    }

    public static boolean supportsXPathDocOrder(Repository repository)
    {
        return TRUE.equals(repository
                .getDescriptor(Repository.QUERY_XPATH_DOC_ORDER));
    }

    // encode/decode by ISO9075

    private static ISO9075 escaper = new ISO9075();

    /**
     * Escapes the Jcr names using ISO 9075 encoding.
     *
     * @param unescaped
     * @return
     */
    public static String encode(String decoded)
    {
        return escaper.encode(decoded);
    }

    /**
     * Decodes the Jcr names using ISO 9075 decoding.
     *
     * @param escaped
     * @return
     */
    public static String decode(String encoded)
    {
        return escaper.decode(encoded);
    }

    // ISO9075 class comes from Jackrabbit..

    /**
     * Implements the encode and decode routines as specified for XML name to SQL
     * identifier conversion in ISO 9075-14:2003.<br/>
     * If a character <code>c</code> is not valid at a certain position in an XML 1.0
     * NCName it is encoded in the form: '_x' + hexValueOf(c) + '_'
     * <p/>
     * Note that only the local part of a {@link org.apache.jackrabbit.name.QName}
     * is encoded / decoded. A URI namespace will always be valid and does not
     * need encoding.
     */
    protected static class ISO9075
    {

        /** Hidden constructor. */
        public ISO9075()
        {
        }

        /** Pattern on an encoded character */
        private Pattern ENCODE_PATTERN = Pattern.compile("_x\\p{XDigit}{4}_");

        /** Padding characters */
        private char[] PADDING = new char[] { '0', '0', '0' };

        /** All the possible hex digits */
        private String HEX_DIGITS = "0123456789abcdefABCDEF";

        /**
         * Encodes the local part of <code>name</code> as specified in ISO 9075.
         * @param name the <code>QName</code> to encode.
         * @return the encoded <code>QName</code> or <code>name</code> if it does
         *   not need encoding.
         */
        public QName encode(QName name)
        {
            String encoded = encode(name.getLocalName());
            if (encoded == name.getLocalName())
            {
                return name;
            }
            else
            {
                return new QName(name.getNamespaceURI(), encoded);
            }
        }

        /**
         * Encodes <code>name</code> as specified in ISO 9075.
         * @param name the <code>String</code> to encode.
         * @return the encoded <code>String</code> or <code>name</code> if it does
         *   not need encoding.
         */
        public String encode(String name)
        {
            // quick check for root node name
            if (name.length() == 0)
            {
                return name;
            }
            if (XMLChar.isValidName(name) && name.indexOf("_x") < 0)
            {
                // already valid
                return name;
            }
            else
            {
                // encode
                StringBuffer encoded = new StringBuffer();
                for (int i = 0; i < name.length(); i++)
                {
                    if (i == 0)
                    {
                        // first character of name
                        if (XMLChar.isNameStart(name.charAt(i)))
                        {
                            if (needsEscaping(name, i))
                            {
                                // '_x' must be encoded
                                encode('_', encoded);
                            }
                            else
                            {
                                encoded.append(name.charAt(i));
                            }
                        }
                        else
                        {
                            // not valid as first character -> encode
                            encode(name.charAt(i), encoded);
                        }
                    }
                    else if (!XMLChar.isName(name.charAt(i)))
                    {
                        encode(name.charAt(i), encoded);
                    }
                    else
                    {
                        if (needsEscaping(name, i))
                        {
                            // '_x' must be encoded
                            encode('_', encoded);
                        }
                        else
                        {
                            encoded.append(name.charAt(i));
                        }
                    }
                }
                return encoded.toString();
            }
        }

        /**
         * Decodes the <code>name</code>.
         * @param name the <code>QName</code> to decode.
         * @return the decoded <code>QName</code>.
         */
        public QName decode(QName name)
        {
            String decoded = decode(name.getLocalName());
            if (decoded == name.getLocalName())
            {
                return name;
            }
            else
            {
                return new QName(name.getNamespaceURI(), decoded.toString());
            }
        }

        /**
         * Decodes the <code>name</code>.
         * @param name the <code>String</code> to decode.
         * @return the decoded <code>String</code>.
         */
        public String decode(String name)
        {
            // quick check
            if (name.indexOf("_x") < 0)
            {
                // not encoded
                return name;
            }
            StringBuffer decoded = new StringBuffer();
            Matcher m = ENCODE_PATTERN.matcher(name);
            while (m.find())
            {
                m.appendReplacement(decoded, Character.toString((char) Integer
                        .parseInt(m.group().substring(2, 6), 16)));
            }
            m.appendTail(decoded);
            return decoded.toString();
        }

        //-------------------------< internal >-------------------------------------

        /**
         * Encodes the character <code>c</code> as a String in the following form:
         * <code>"_x" + hex value of c + "_"</code>. Where the hex value has
         * four digits if the character with possibly leading zeros.
         * <p/>
         * Example: ' ' (the space character) is encoded to: _x0020_
         * @param c the character to encode
         * @param b the encoded character is appended to <code>StringBuffer</code>
         *  <code>b</code>.
         */
        private void encode(char c, StringBuffer b)
        {
            b.append("_x");
            String hex = Integer.toHexString(c);
            b.append(PADDING, 0, 4 - hex.length());
            b.append(hex);
            b.append("_");
        }

        /**
         * Returns true if <code>name.charAt(location)</code> is the underscore
         * character and the following character sequence is 'xHHHH_' where H
         * is a hex digit.
         * @param name the name to check.
         * @param location the location to look at.
         * @throws ArrayIndexOutOfBoundsException if location > name.length()
         */
        private boolean needsEscaping(String name, int location)
                throws ArrayIndexOutOfBoundsException
        {
            if (name.charAt(location) == '_' && name.length() >= location + 6)
            {
                return name.charAt(location + 1) == 'x'
                        && HEX_DIGITS.indexOf(name.charAt(location + 2)) != -1
                        && HEX_DIGITS.indexOf(name.charAt(location + 3)) != -1
                        && HEX_DIGITS.indexOf(name.charAt(location + 4)) != -1
                        && HEX_DIGITS.indexOf(name.charAt(location + 5)) != -1;
            }
            else
            {
                return false;
            }
        }
    }
}
