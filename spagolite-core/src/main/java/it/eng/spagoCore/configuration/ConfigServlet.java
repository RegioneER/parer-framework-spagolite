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
package it.eng.spagoCore.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/*
 * DATE            CONTRIBUTOR/DEVELOPER    NOTE
 * 13-12-2004              Butano   Se afRootPath == null chiamta a getResourcesAsStream()										    
 **/
public class ConfigServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Oggetto che permette di leggere la configurazione tramite il contesto della servlet, indispensabile se
     * l'applicazione viene rilasciata sotto forma di WAR che non viene espanso.
     */
    // class StreamCreatorConfiguration implements IConfigurationCreator {
    //
    // public InputStream getInputStream(String resourceName) {
    // ServletContext context = getServletConfig().getServletContext();
    // InputStream resourcesStream = context.getResourceAsStream(resourceName);
    // return resourcesStream;
    // }
    //
    // public SourceBean createConfiguration(String configName) throws SourceBeanException {
    // SourceBean result = null;
    // InputStream resourcesStream = getInputStream(configName);
    //
    // try {
    // result = SourceBean.fromXMLStream(new InputSource(resourcesStream));
    // } finally {
    // if (resourcesStream != null) {
    // try {
    // resourcesStream.close();
    // } catch (Exception ex) {
    // throw new SourceBeanException(ex.getMessage());
    // }
    // }
    // }
    // return result;
    // }
    //
    // }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigSingleton.set_contextRoot(config.getServletContext().getContextPath());
        // String afRootPath = config.getInitParameter("AF_ROOT_PATH");
        String afConfigFile = config.getInitParameter("AF_CONFIG_FILE");
        // Setto il parametro per l'abilitazione dell'help online
        if (config.getInitParameter("enableHelpOnline") != null) {
            Boolean enableHelpOnline = new Boolean(config.getInitParameter("enableHelpOnline"));
            ConfigSingleton.setEnableHelpOnline(enableHelpOnline);
            // Setto il parametro con l'URI dove sono deployate le pagine html dell'help
            if (config.getInitParameter("helpServerURI") != null) {
                ConfigSingleton.setHelpServerURI(config.getInitParameter("helpServerURI"));
            }
        }
        // Setto il parametro per l'abilitazione dell'help online
        if (config.getInitParameter("disableSecurity") != null) {
            Boolean disableSecurity = new Boolean(config.getInitParameter("disableSecurity"));
            ConfigSingleton.setDisableSecurity(disableSecurity);
        }

        if (config.getInitParameter("enableLazySort") != null) {
            Boolean enableLazySort = new Boolean(config.getInitParameter("enableLazySort"));
            ConfigSingleton.setEnableLazySort(enableLazySort);
        }

        // Setto il parametro per l'abilitazione dell'help online
        if (config.getInitParameter("debugAuthorization") != null) {
            Boolean debugAuthorization = new Boolean(config.getInitParameter("debugAuthorization"));
            ConfigSingleton.setDebugAuthorization(debugAuthorization);
        }

        // Setto i parametri per la visualizzazione dei loghi
        if (config.getInitParameter("logo1_relativePath") != null) {
            ConfigSingleton.setLogo1_relativePath(config.getInitParameter("logo1_relativePath"));
        }
        if (config.getInitParameter("logo1_alt") != null) {
            ConfigSingleton.setLogo1_alt(config.getInitParameter("logo1_alt"));
        }
        if (config.getInitParameter("logo1_url") != null) {
            ConfigSingleton.setLogo1_url(config.getInitParameter("logo1_url"));
        }
        if (config.getInitParameter("logo1_title") != null) {
            ConfigSingleton.setLogo1_title(config.getInitParameter("logo1_title"));
        }
        if (config.getInitParameter("logo2_relativePath") != null) {
            ConfigSingleton.setLogo2_relativePath(config.getInitParameter("logo2_relativePath"));
        }
        if (config.getInitParameter("logo2_alt") != null) {
            ConfigSingleton.setLogo2_alt(config.getInitParameter("logo2_alt"));
        }
        if (config.getInitParameter("logo2_url") != null) {
            ConfigSingleton.setLogo2_url(config.getInitParameter("logo2_url"));
        }
        if (config.getInitParameter("logo2_title") != null) {
            ConfigSingleton.setLogo2_title(config.getInitParameter("logo2_title"));
        }
        if (config.getInitParameter("logo3_relativePath") != null) {
            ConfigSingleton.setLogo3_relativePath(config.getInitParameter("logo3_relativePath"));
        }
        if (config.getInitParameter("logo3_alt") != null) {
            ConfigSingleton.setLogo3_alt(config.getInitParameter("logo3_alt"));
        }
        if (config.getInitParameter("logo3_url") != null) {
            ConfigSingleton.setLogo3_url(config.getInitParameter("logo3_url"));
        }
        if (config.getInitParameter("logo3_title") != null) {
            ConfigSingleton.setLogo3_title(config.getInitParameter("logo3_title"));
        }
        if (config.getInitParameter("titolo_applicativo") != null) {
            ConfigSingleton.setTitolo_applicativo(config.getInitParameter("titolo_applicativo"));
        }

        // ConfigSingleton.setConfigFileName(afConfigFile);
        // if (afRootPath == null) {
        // ConfigSingleton.setConfigurationCreation(new StreamCreatorConfiguration());
        // ConfigSingleton.setRootPath(config.getServletContext().getRealPath(""));
        // } else {
        // ConfigSingleton.setConfigurationCreation(new FileCreatorConfiguration(afRootPath));
        // ConfigSingleton.setRootPath(afRootPath);
        // }

        // Load MANIFEST.MF, necessary for startup
        Properties prop = new Properties();
        try {
            InputStream in = getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");
            if (in != null) {
                prop.load(in);
            } else {
                throw new FileNotFoundException("Unable to find /META-INF/MANIFEST.MF file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Error reading /META-INF/MANIFEST.MF file", e);
        }
        if (!prop.containsKey("App-Version") || !prop.containsKey("App-BuildDate") || !prop.containsKey("App-Name")) {
            throw new ServletException("File /META-INF/MANIFEST.MF exists, but it doesn't contains necessary metadata");
        }

        ConfigSingleton.set_appVersion(prop.getProperty("App-Version"));
        ConfigSingleton.set_appBuildDate(prop.getProperty("App-BuildDate"));
        ConfigSingleton.set_appName(prop.getProperty("App-Name"));

        // Print system properties
        // String startupConsoleStr = (String) ConfigSingleton.getInstance().getAttribute("COMMON.startup_console");
        // if ((startupConsoleStr == null) || (!startupConsoleStr.equalsIgnoreCase("FALSE"))) {
        // System.out.println("ConfigServlet::init: AF_ROOT_PATH = " + afRootPath);
        System.out.println("ConfigServlet::init: AF_CONFIG_FILE = " + afConfigFile);
        System.out.println("ConfigServlet::init: APP_VERSIONE = " + ConfigSingleton.get_appVersion());
        System.out.println("ConfigServlet::init: APP_BUILD_DATE = " + ConfigSingleton.get_appBuildDate());
        Properties props = System.getProperties();
        Enumeration propNames = props.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = (String) propNames.nextElement();
            System.out.println("ConfigServlet::init: " + name + " [" + System.getProperty(name) + "]");
        }
        // }

        // InitializerManager.init();
    }

}
