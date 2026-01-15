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

package it.eng.spagoCore;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

/**
 * Bean di configurazione dell'applicazione. Lo stato del bean può essere modificato solamente
 * all'interno di questo package.
 */
public final class ConfigSingleton implements Serializable {

    private static final ConfigSingleton INSTANCE = new ConfigSingleton();
    private static final long serialVersionUID = 5441406425769570888L;

    // cache
    private Map<String, String> configCache;

    private String contextPath = "/applicazione";
    private String appName = "Applicazione";
    private String appVersion = "SNAPSHOT";
    private String appBuildDate = "today";

    private ConfigSingleton() {

    }

    public static ConfigSingleton getInstance() {
        return INSTANCE;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setConfigCache(Map<String, String> configCache) {
        this.configCache = configCache;
    }

    public URI getUriValue(String propName) {
        return URI.create(configCache.get(propName));
    }

    public long getLongValue(String propName) {
        return Long.parseLong(configCache.get(propName));
    }

    public boolean getBooleanValue(String propName) {
        return Boolean.parseBoolean(configCache.get(propName));
    }

    public String getStringValue(String propName) {
        return configCache.get(propName);
    }

    public int getIntValue(String propName) {
        return Integer.parseInt(configCache.get(propName));
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppBuildDate() {
        return appBuildDate;
    }

    public void setAppBuildDate(String appBuildDate) {
        this.appBuildDate = appBuildDate;
    }

}
