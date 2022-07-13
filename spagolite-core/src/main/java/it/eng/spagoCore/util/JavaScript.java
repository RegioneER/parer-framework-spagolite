/**

    Copyright 2004, 2007 Engineering Ingegneria Informatica S.p.A.

    This file is part of Spago.

    Spago is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 2.1 of the License, or
    any later version.

    Spago is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Spago; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.spagoCore.util;

/**
 * DATE            CONTRIBUTOR/DEVELOPER    NOTE
 * 13-12-2004		  Butano           SourceBean ora restituisce ArrayList
 * 										    
 **/

/**
 * La classe <code>JavaScript</code> è una classe di utility che mette a disposizione dei metodi statici che
 * implementano le stesse funzionalità di alcune funzioni in javascript.
 * 
 * @author Luigi Bellio
 */

public class JavaScript {
    /**
     * Questo metodo implementa la stessa logica della funzione javascript <em>escape</em>.
     * 
     * @param input
     *            stringa da manipolare.
     * 
     * @return restituisce risultato dell'escaping
     */
    public static String escape(String input) {
        input = replace(input, "%", "%25");
        input = replace(input, " ", "%20");
        input = replace(input, "\"", "%22");
        input = replace(input, "'", "%27");
        input = replace(input, "<", "%3C");
        input = replace(input, ">", "%3E");
        input = replace(input, "?", "%3F");
        input = replace(input, "&", "%26");
        return input;
    } // public static String escape(String input)

    /**
     * This method implements the same logic of the javascript <em>unescape</em> function.
     * 
     * @param input
     *            the string to manage.
     * 
     * @return restituisce risultato di unescape
     */
    public static String unescape(String input) {
        input = replace(input, "%25", "%");
        input = replace(input, "%20", " ");
        input = replace(input, "%22", "\"");
        input = replace(input, "%27", "'");
        input = replace(input, "%3C", "<");
        input = replace(input, "%3E", ">");
        input = replace(input, "%3F", "?");
        input = replace(input, "%26", "&");
        return input;
    } // public static String unescape(String input)

    /**
     * Questo metodo manipola la stringa in input permettendo le seguenti sostituzioni: da <em>"\n"</em> a
     * <em>"\\n"</em>, da <em>"\""</em> a <em>"'"</em>
     * 
     * @param input
     *            stringa da manipolare.
     * 
     * @return restituisce risultato di escapeText
     */
    public static String escapeText(String input) {
        input = replace(input, "\n", "\\n");
        input = replace(input, "\"", "'");
        return input;
    } // public static String escapeCarrigeReturn(String input)

    /**
     * Questo metodo permette di sostituire una parte di una stringa con un'altra.
     * 
     * @param toParse
     *            stringa da manipolare.
     * @param replacing
     *            parte di stringa da sostituire.
     * @param replaced
     *            stringa nuova.
     * 
     * @return restituisce risultato della replace
     */
    public static String replace(String toParse, String replacing, String replaced) {
        if (toParse == null) {
            return toParse;
        } // if (toParse == null)
        if (replacing == null) {
            return toParse;
        } // if (replacing == null)
        if (replaced != null) {
            int parameterIndex = toParse.indexOf(replacing);
            while (parameterIndex != -1) {
                String newToParse = toParse.substring(0, parameterIndex);
                newToParse += replaced;
                newToParse += toParse.substring(parameterIndex + replacing.length(), toParse.length());
                toParse = newToParse;
                parameterIndex = toParse.indexOf(replacing, parameterIndex + replaced.length());
            } // while (parameterIndex != -1)
        } // if (replaced != null)
        return toParse;
    } // public static String replace(String toParse, String replacing, String
      // replaced)

    public static String stringToHTMLString(String string) {
        if (string == null)
            return "";

        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\n')
                    // Handle Newline
                    sb.append("&lt;br/&gt;");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

} // public class JavaScript
