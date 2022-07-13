/**
 *
 * Copyright 2004, 2007 Engineering Ingegneria Informatica S.p.A.
 *
 * This file is part of Spago.
 *
 * Spago is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Spago is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spago; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * */
package it.eng.spagoCore.configuration;

/**
 * DATE CONTRIBUTOR/DEVELOPER NOTE 13-12-2004 Butano Passaggio ad ArrayList in SourceBean Introdotto
 * nuovo costruttore (vedi getResourcesAsStream in ConfigServlet) 24-01-2005 Bernabei Aggiunto
 * l'impostazione a null dell'attributo _resources nel metodo reset()
 *
 */
/**
 * Questa classe offre i servizi per recuperare da tutti i files di configurazione XML dell'applicazione il valore dei
 * parametri in esso contenuti.
 *
 * @author Luigi Bellio from it.eng.spagoCore.base.sourceBean
 */
public final class ConfigSingleton {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static String _rootPath = null;

    private static String _engConfigFile = null;

    // private static IConfigurationCreator _configurationCreator = null;

    private static ConfigSingleton _instance = null;

    private static final String AF_ROOT_PATH = "";

    private static final String AF_CONFIG_FILE = "/WEB-INF/conf/spago/master.xml";

    public static final String CONFIGURATOR = "CONFIGURATOR";

    public static final String CONFIGURATOR_PATH = "PATH";

    public static final String CONFIGURATOR_MASTER = "MASTER";

    private static String _contextRoot;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static String _appVersion;

    private static String _appBuildDate;

    private static String _appName;

    private static Boolean enableHelpOnline = false;

    private static Boolean debugAuthorization = false;

    private static String helpServerURI = "";

    private static Boolean disableSecurity = false;

    private static Boolean enableLazySort = false;

    private static String logo1_relativePath = "/img/regione/LogoParer.png";
    private static String logo1_url = "https://poloarchivistico.regione.emilia-romagna.it/";
    private static String logo1_title = "ParER - Polo Archivistico Regionale dell'Emilia-Romagna";
    private static String logo1_alt = "Logo";

    private static String logo2_relativePath = "/img/regione/LogoER.png";
    private static String logo2_url = "http://www.regione.emilia-romagna.it";
    private static String logo2_title = "Regione Emilia-Romagna";
    private static String logo2_alt = "Regione Emilia-Romagna";

    private static String logo3_relativePath = ""; // "/img/regione/LogoIbc.png";
    private static String logo3_url = ""; // "http://www.ibc.regione.emilia-romagna.it";
    private static String logo3_title = ""; // "Istituto per i beni artistici culturali e naturali della Regione
                                            // Emilia-Romagna";
    private static String logo3_alt = ""; // "Logo IBC";

    private static String titolo_applicativo = "";

    // patch per il funzionamento del framework con junit o vm esterne all'app
    // server.
    // occorre settare la propriet� AF_ROOT_PATH come parametro della vm:
    static {
        // if (System.getProperty("AF_ROOT_PATH") != null) {
        // _configurationCreator = new FileCreatorConfiguration(getRootPath());
        // }
    }

    /**
     * @param creation
     *            The _configurationCreation to set.
     */
    // public static void setConfigurationCreation(IConfigurationCreator creator) {
    // _configurationCreator = creator;
    // }
    //
    // public static IConfigurationCreator getConfigurationCreator() {
    // return _configurationCreator;
    // }

    /**
     * In questo costruttore tutti i files XML di configurazione vengono letti e con le informazioni in essi contenute
     * vengono create delle istanze di SourceBean; questo permette di accedere alle informazioni utilizzando i metodi
     * della classe SourceBean.
     *
     * @param engConfigFile
     *            percorso e nome del file XML da leggere.
     */
    private ConfigSingleton(String engConfigFile) throws Exception {
        // Leggo il file principale di configurazione

        // super(_configurationCreator.createConfiguration(engConfigFile));
        //
        // List configuratorsList = getAttributeAsList(CONFIGURATOR);

        // configuratorsObject � una lista di nomi di file di configurazione
        // // che devo elaborare uno alla volta.
        // for (int i = 0; i < configuratorsList.size(); i++) {
        // Object configuratorObject = configuratorsList.get(i);
        // if (configuratorObject instanceof SourceBean) {
        // SourceBean sbConfigurator = (SourceBean) configuratorObject;
        // String configuratorName = (String) sbConfigurator.getAttribute(CONFIGURATOR_PATH);
        // if (configuratorName == null) {
        // continue;
        // }
        // try {
        // // Leggo il singolo file
        // ConfigSingleton singleConfiguration = new ConfigSingleton(configuratorName);
        //
        // // Reperisco il root del file
        // String singleFileRootName = singleConfiguration.getName();
        //
        // if (singleFileRootName.equalsIgnoreCase(CONFIGURATOR_MASTER)) {
        // // Il file di configurazione � un MASTER: mi interessa
        // // solo il suo contenuto,
        // // non l'intera struttura <MASTER>...</MASTER>
        // List currentConfigurationAttributes = getContainedAttributes();
        // List singleFileAttributes = singleConfiguration.getContainedAttributes();
        //
        // // Faccio il merge delle due configurazioni e Imposto la
        // // nuova configurazione
        // currentConfigurationAttributes.addAll(singleFileAttributes);
        // setContainedAttributes(currentConfigurationAttributes);
        // } else {
        //
        // // Verifico se esiste gi� una busta con lo stesso nome
        // // della configurazione corrente:
        // // ad esempio "ACTIONS", nel qual caso sto leggendo una
        // // configurazione
        // // splittata in pi� file
        // Object rootAttribute = getAttribute(singleFileRootName);
        // if (rootAttribute != null) {
        // if (rootAttribute instanceof SourceBean) {
        // // Esiste gi�, reperisco le sottobuste: ad
        // // esempio
        // // le buste "ACTION"
        // List currentConfigurationAttributes = ((SourceBean) rootAttribute).getContainedAttributes();
        // List singleFileAttributes = singleConfiguration.getContainedAttributes();
        // // Faccio il merge delle due configurazioni
        // currentConfigurationAttributes.addAll(singleFileAttributes);
        //
        // // Imposto la nuova configurazione
        // ((SourceBean) rootAttribute).setContainedAttributes(currentConfigurationAttributes);
        // } else {
        // System.out.println("ConfigSingleton::ConfigSingleton: errore creazione configuratore [" + singleFileRootName
        // + "]");
        // return;
        // }
        // } else {
        // // Non esiste, nessun problema
        // setAttribute(singleConfiguration);
        // }
        //
        // }
        //
        // } // try
        // catch (Exception ex) {
        // System.out.println("ConfigSingleton::ConfigSingleton: errore creazione configuratore [" + configuratorName +
        // "]");
        // ex.printStackTrace();
        // } // catch(Exception ex) try
        // } // if (configuratorObject instanceof SourceBean)
        // } // for (int i = 0; i < configuratorsVector.size(); i++)

    } // private ConfigSingleton(String engConfigFile) throws Exception

    public static ConfigSingleton getInstance() {
        if (_instance == null) {
            synchronized (ConfigSingleton.class) {
                if (_instance == null) {
                    try {
                        _instance = new ConfigSingleton(getConfigFileName());
                    } // try
                    catch (Exception ex) {
                        System.out.println("ConfigSingleton::getInstance: errore creazione configuratore ["
                                + getConfigFileName() + "]");
                        ex.printStackTrace();
                    } // catch(Exception ex) try
                } // if (_instance == null)
            } // synchronized(ConfigSingleton.class)
        } // if (_instance == null)
        return _instance;
    } // public static ConfigSingleton getInstance()

    public static void release() {
        if (_instance != null) {
            synchronized (ConfigSingleton.class) {
                _instance = null;
            } // synchronized (ConfigSingleton.class)
        } // if (_instance != null)
    } // public static void release()

    /**
     * Ritorna il rootPath dell'installazione dell'applicativo. Questo percorso viene utilizzato dal framework per
     * recuperare le risorse(esempio i files XML).L'informazione del rootPath pu� essere resa disponibile al framework
     * anche impostando una variabile di ambiente con nome "AF_ROOT_PATH".
     *
     * @return String specifica il rootPath.
     */
    public static String getRootPath() {
        if (_rootPath == null) {
            synchronized (ConfigSingleton.class) {
                if (_rootPath == null) {
                    _rootPath = System.getProperty("AF_ROOT_PATH");
                }
                if (_rootPath == null) {
                    _rootPath = AF_ROOT_PATH;
                }
            } // synchronized(ConfigSingleton.class)
        } // if (_rootPath == null)
        return _rootPath;
    } // public static String getRootPath()

    /**
     * Questo metodo permette di impostare il rootPath.
     *
     * @param rootPath
     *            stringa che rappresenta il rootpath.
     */
    public static void setRootPath(String rootPath) {
        synchronized (ConfigSingleton.class) {
            _rootPath = rootPath;
        } // synchronized(ConfigSingleton.class)
    } // protected static void setRootPath(String rootPath)

    /**
     * Ritorna il nome del file XML contenente i riferimenti agli altri files XML.Il nome del file XML master pu�
     * essere resa disponibile al framework anche impostando una variabile di ambiente con nome "AF_CONFIG_FILE".
     *
     * @return String specifica il nome del file XML master.
     */
    public static String getConfigFileName() {
        if (_engConfigFile == null) {
            synchronized (ConfigSingleton.class) {
                if (_engConfigFile == null) {
                    _engConfigFile = System.getProperty("AF_CONFIG_FILE");
                }
                if (_engConfigFile == null) {
                    _engConfigFile = AF_CONFIG_FILE;
                }
            } // synchronized(ConfigSingleton.class)
        } // if (_engConfigFile == null)
        return _engConfigFile;
    } // public static String getConfigFileName()

    /**
     * Questo metodo permette di impostare il nome del file XML master.
     *
     * @param engConfigFile
     *            specifica il nome del file XML master.
     */
    public static void setConfigFileName(String engConfigFile) {
        synchronized (ConfigSingleton.class) {
            _engConfigFile = engConfigFile;
        } // synchronized(ConfigSingleton.class)
    } // public static void setConfigFileName(String engConfigFile)

    public static String get_appVersion() {
        return _appVersion;
    }

    public static void set_appVersion(String _appVersion) {
        ConfigSingleton._appVersion = _appVersion;
    }

    public static String get_appBuildDate() {
        return _appBuildDate;
    }

    public static void set_appBuildDate(String _appBuildDate) {
        ConfigSingleton._appBuildDate = _appBuildDate;
    }

    public static String get_appName() {
        return _appName;
    }

    public static void set_appName(String _appName) {
        ConfigSingleton._appName = _appName;
    }

    public static Boolean getEnableHelpOnline() {
        return enableHelpOnline;
    }

    public static void setEnableHelpOnline(Boolean enableHelpOnline) {
        ConfigSingleton.enableHelpOnline = enableHelpOnline;
    }

    public static String getHelpServerURI() {
        return helpServerURI;
    }

    public static void setHelpServerURI(String helpServerURI) {
        ConfigSingleton.helpServerURI = helpServerURI;
    }

    public static void setDisableSecurity(Boolean disableSecurity) {
        ConfigSingleton.disableSecurity = disableSecurity;

    }

    public static Boolean getDisableSecurity() {
        return disableSecurity;
    }

    public static String get_contextRoot() {
        return _contextRoot;
    }

    public static void set_contextRoot(String _contextRoot) {
        ConfigSingleton._contextRoot = _contextRoot;
    }

    public static Boolean getDebugAuthorization() {
        return debugAuthorization;
    }

    public static void setDebugAuthorization(Boolean debugAuthorization) {
        ConfigSingleton.debugAuthorization = debugAuthorization;
    }

    public static Boolean getEnableLazySort() {
        return enableLazySort;
    }

    public static void setEnableLazySort(Boolean enableLazySort) {
        ConfigSingleton.enableLazySort = enableLazySort;
    }

    public static String getLogo1_relativePath() {
        return logo1_relativePath;
    }

    public static void setLogo1_relativePath(String logo1_relativePath) {
        ConfigSingleton.logo1_relativePath = logo1_relativePath;
    }

    public static String getLogo1_url() {
        return logo1_url;
    }

    public static void setLogo1_url(String logo1_url) {
        ConfigSingleton.logo1_url = logo1_url;
    }

    public static String getLogo1_title() {
        return logo1_title;
    }

    public static void setLogo1_title(String logo1_title) {
        ConfigSingleton.logo1_title = logo1_title;
    }

    public static String getLogo1_alt() {
        return logo1_alt;
    }

    public static void setLogo1_alt(String logo1_alt) {
        ConfigSingleton.logo1_alt = logo1_alt;
    }

    public static String getLogo2_relativePath() {
        return logo2_relativePath;
    }

    public static void setLogo2_relativePath(String logo2_relativePath) {
        ConfigSingleton.logo2_relativePath = logo2_relativePath;
    }

    public static String getLogo2_url() {
        return logo2_url;
    }

    public static void setLogo2_url(String logo2_url) {
        ConfigSingleton.logo2_url = logo2_url;
    }

    public static String getLogo2_title() {
        return logo2_title;
    }

    public static void setLogo2_title(String logo2_title) {
        ConfigSingleton.logo2_title = logo2_title;
    }

    public static String getLogo2_alt() {
        return logo2_alt;
    }

    public static void setLogo2_alt(String logo2_alt) {
        ConfigSingleton.logo2_alt = logo2_alt;
    }

    public static String getLogo3_relativePath() {
        return logo3_relativePath;
    }

    public static void setLogo3_relativePath(String logo3_relativePath) {
        ConfigSingleton.logo3_relativePath = logo3_relativePath;
    }

    public static String getLogo3_url() {
        return logo3_url;
    }

    public static void setLogo3_url(String logo3_url) {
        ConfigSingleton.logo3_url = logo3_url;
    }

    public static String getLogo3_title() {
        return logo3_title;
    }

    public static void setLogo3_title(String logo3_title) {
        ConfigSingleton.logo3_title = logo3_title;
    }

    public static String getLogo3_alt() {
        return logo3_alt;
    }

    public static void setLogo3_alt(String logo3_alt) {
        ConfigSingleton.logo3_alt = logo3_alt;
    }

    public static String getTitolo_applicativo() {
        return titolo_applicativo;
    }

    public static void setTitolo_applicativo(String titolo_applicativo) {
        ConfigSingleton.titolo_applicativo = titolo_applicativo;
    }

} // public final class ConfigSingleton extends SourceBean
