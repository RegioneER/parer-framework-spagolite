/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoCore.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * This class filters user http-request URI econding UTF-8 character on HTTP query string
 */
public class UriEcondingFilter implements Filter {

    /*
     * The encoding.
     */
    private String encoding;
    /*
     * Field split
     */
    private String fieldSplit;
    /*
     * Value split
     */
    private String fieldValueSplit;

    /*
     * Multi Value separator
     */
    private String valueSeparator;

    private String[] multiValueSeparator;

    /*
     * (non-Javadoc) @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("uriEncoding");
        fieldSplit = config.getInitParameter("fieldSplit");
        fieldValueSplit = config.getInitParameter("fieldValueSplit");
        valueSeparator = config.getInitParameter("valueSeparator");

        if (encoding == null) {
            encoding = "UTF-8";
        }

        if (fieldSplit == null) {
            fieldSplit = "&";
        }

        if (fieldValueSplit == null) {
            fieldValueSplit = "=";
        }

        if (valueSeparator == null) {
            multiValueSeparator = new String[] {
                    ",", "\\+", "\\|" };// default
        } else {
            multiValueSeparator = valueSeparator.split("\\|");
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        // build wrapper
        EncodedHttpWrapper wrapper = new EncodedHttpWrapper((HttpServletRequest) request);
        // build URI decoded Map
        wrapper.buildURIEncodParameterMap();
        // go next
        next.doFilter(wrapper, response);
    }

    public class EncodedHttpWrapper extends HttpServletRequestWrapper {
        private Map<String, String[]> paramsEncoded;

        public EncodedHttpWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            // return super.getParameter(name); //To change body of generated methods,
            // choose Tools | Templates.
            if (checkParam(name)) {
                return super.getParameter(name);
            } else {
                return this.paramsEncoded.get(name)[0]; // get single value
            }
        }

        @Override
        public String[] getParameterValues(String name) {
            if (checkParam(name)) {
                return super.getParameterValues(name);
            } else {
                return this.paramsEncoded.get(name);
            }
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return this.paramsEncoded.isEmpty() ? super.getParameterMap() : this.paramsEncoded;
        }

        private void buildURIEncodParameterMap() throws UnsupportedEncodingException {
            final int PARAM = 0;
            final int VALUE = 1;
            Map<String, String[]> localEncoded = new HashMap<>();
            //
            String queryString = super.getQueryString();
            if (!StringUtils.isEmpty(queryString)) {
                String decoded = URLDecoder.decode(queryString,
                        super.getCharacterEncoding() == null ? encoding
                                : super.getCharacterEncoding());
                String[] params = decoded.split(fieldSplit);
                for (String param : params) {
                    String[] paramAndValue = param.split(fieldValueSplit);
                    String[] values = null;
                    // has values
                    if (paramAndValue.length > 1) {
                        values = valuesAsList(paramAndValue[VALUE]);
                    } else {
                        // no value
                        values = new String[] {
                                "" }; // empty

                    }
                    // if param exists on the map replacing with old list and new one
                    if (localEncoded.get(paramAndValue[PARAM]) != null) {
                        String[] get = localEncoded.get(paramAndValue[PARAM]);
                        List<String> parameterValues = new ArrayList<>();
                        // add elements
                        Collections.addAll(parameterValues, get);
                        Collections.addAll(parameterValues, values);
                        // add on map
                        localEncoded.put(paramAndValue[PARAM],
                                parameterValues.toArray(new String[] {}));
                    } else {
                        localEncoded.put(paramAndValue[PARAM], values);
                    }
                } // pare
            } // queryString
              // not modifiable
            this.paramsEncoded = Collections.unmodifiableMap(localEncoded);
        }

        private boolean checkParam(String name) {
            return this.paramsEncoded.isEmpty() || !this.paramsEncoded.containsKey(name);
        }

        /**
         * Check if value is a valid JSON or a list of elements separated by one of the separator
         * defined in multiValueSeparator. It manages also array of multiple JSON Object like
         * [{json1},{json2},{json3}].
         *
         * @param value valore atteso
         *
         * @return array di string verificate
         *
         * @throws JsonParseException
         * @throws IOException
         */
        private String[] valuesAsList(String value) {
            String[] values = new String[] {
                    value }; // init
            // try to extract as list
            try {
                // check if values is valid json
                JsonNode json = new ObjectMapper().readTree(value);
                // if valid json return as string
                values = new String[] {
                        json.toString() };
            } catch (Exception e) {
                // if list of values try to manage them with separator (check on
                // multiValueSeparator)
                for (String separator : multiValueSeparator) {
                    if (value.split(separator).length > 1) {
                        values = value.split(separator);
                    }
                }
            }
            return values;
        }
    }

    /*
     * (non-Javadoc) @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }
}
