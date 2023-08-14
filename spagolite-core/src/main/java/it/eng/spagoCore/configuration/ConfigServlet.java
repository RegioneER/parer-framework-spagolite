package it.eng.spagoCore.configuration;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet di configurazione di SpagoLite.
 *
 * Supporta i parametri di configurazione definiti dalle costanti sottostanti.
 */
public class ConfigServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServlet.class.getName());

    // cache
    private final Map<String, String> configCache = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ConfigSingleton configSingleton = ConfigSingleton.getInstance();
        configSingleton.setContextPath(config.getServletContext().getContextPath());
        final String appPrefix = config.getServletContext().getContextPath().replace("/", "");

        // fill cache standard properties
        Stream.of(ConfigProperties.StandardProperty.values()).forEach(p -> {
            configCache.put(p.name(), getValue(config, appPrefix, p.getPropName(), p.getPropDefaultValue()));
        });

        // fill cache URI properties
        Stream.of(ConfigProperties.URIProperty.values()).forEach(p -> {

            // Imposto i parametri per la visualizzazione dei loghi (risorse URI)
            final String absolutePath = getValue(config, appPrefix, p.getPropAbsolutePathName(),
                    p.getPropAbsoluteDefaultValue());
            String relativePath = getValue(config, appPrefix, p.getPropRelativePathName(),
                    p.getPropRelativeDefaultValue());
            // L'absolutePath ha precedenza su tutto
            if (StringUtils.isNotBlank(absolutePath)) {
                relativePath = getRelativePathFromExternalResource(absolutePath, p.getDestination(),
                        p.getPropRelativeDefaultValue());
            }
            configCache.put(p.name(), relativePath);
        });

        // set cache
        configSingleton.setConfigCache(configCache);

        // Load MANIFEST.MF, necessary for startup
        Properties prop = new Properties();

        try (InputStream in = getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");) {
            prop.load(in);
            if (!prop.containsKey("App-Version") || !prop.containsKey("App-BuildDate")
                    || !prop.containsKey("App-Name")) {
                throw new ServletException(
                        "File /META-INF/MANIFEST.MF exists, but it doesn't contains necessary metadata");
            }
        } catch (IOException e) {
            throw new ServletException("Error reading /META-INF/MANIFEST.MF file", e);
        }

        String appVersion = prop.getProperty("App-Version");
        String appBuildDate = prop.getProperty("App-BuildDate");
        String appName = prop.getProperty("App-Name");

        configSingleton.setAppVersion(appVersion);
        configSingleton.setAppBuildDate(appBuildDate);
        configSingleton.setAppName(appName);

        LOGGER.debug("ConfigServlet::init: Init param [APP_VERSION] ottenuto dal MANIFEST.MF con il valore di [{}]",
                appVersion);
        LOGGER.debug("ConfigServlet::init: Init param [APP_BUILD_DATE] ottenuto dal MANIFEST.MF con il valore di [{}]",
                appBuildDate);
        LOGGER.debug("ConfigServlet::init: Init param [APP_NAME] ottenuto dal MANIFEST.MF con il valore di [{}]",
                appName);

        Properties props = System.getProperties();
        Enumeration<?> propNames = props.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = (String) propNames.nextElement();
            LOGGER.trace("ConfigServlet::init: SysProp [{}] con valore [{}]", name, props.getProperty(name));
        }
        handleNotSupportedConfig(config);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("ConfigServlet::init: ConfigSingleton: {}", configSingleton.toString());
        }
    }

    /**
     * Gestione delle configurazioni di inizializzazione. L'ordine con cui viene scelto il valore del parametro è il
     * seguente:
     * <ol>
     * <li>il parametro è presente sul web.xml;</li>
     * <li>E' presente un parametro dal nome <em>applicazione-nome_parametro</em> sulle proprietà  di sistema;</li>
     * <li>E' presente un parametro dal nome <em>nome_parametro</em> sulle proprietà di sistema;</li>
     * <li>viene utilizzato un valore predefinito per il parametro.</li>
     * </ol>
     *
     * @param config
     *            ServletConfig, configurazioni della servlet definite su web.xml
     * @param applicationPrefix
     *            contesto dell'applicazione senza "/" per esempio <em>saceriam</em>
     * @param configName
     *            nome della proprietà  supportata dallla Servlet
     * 
     * @return valore della configurazione.
     */
    private String getValue(ServletConfig config, String applicationPrefix, String configName, String defaultValue) {

        // web.xml personalizzazione su questo specifico pacchetto
        String configValue = config.getInitParameter(configName);
        if (configValue != null && !configValue.isEmpty()) {
            LOGGER.debug("ConfigServlet::init: Init param [{}] definito in web.xml con il valore di [{}]", configName,
                    configValue);
            return configValue;
        }

        // system property specifica per applicationPrefix per esempio il logo
        // dell'applicazione
        configValue = System.getProperty(applicationPrefix + "-" + configName);
        if (configValue != null) {
            LOGGER.debug(
                    "ConfigServlet::init: Init param [{}] definito tramite system property specifica per l'applicazione [{}] (ottenuto come [{}-{}] con il valore di [{}]",
                    configName, applicationPrefix, applicationPrefix, configName, configValue);
            return configValue;
        }

        // system property generica (per esempio "ambiente_deploy")
        configValue = System.getProperty(configName);
        if (configValue != null) {
            LOGGER.debug(
                    "ConfigServlet::init: Init param [{}}] definito tramite system property generica (ottenuto come [{}]) con il valore di [{}].",
                    configName, configName, configValue);
            return configValue;
        }
        // valore predefinito
        configValue = defaultValue;
        LOGGER.debug("ConfigServlet::init: Init param [{}] non definito. Utilizzo il valore predefinito [{}].",
                configName, defaultValue);

        return configValue;
    }

    /**
     * Carica, internamente all'applicazione in fase di deploy, la risorsa esterna. Per l'applicazione la risorsa sarà,
     * a tutti gli effetti, interna.
     * 
     * @param externalResource
     *            percorso della risorsa esterna all'applicazione.
     * @param destinationPath
     *            percorso <em>relativo</em> della risorsa all'interno dell'applicazione
     * @param defaultRelativePath
     *            percorso della risorsa predefinita
     * 
     * @return percorso relativo dalle risorsa
     */
    private String getRelativePathFromExternalResource(String externalResource, String destinationPath,
            String defaultRelativePath) {
        try {
            loadResource(externalResource, destinationPath);
            LOGGER.debug("ConfigServlet::init: Risorsa {} correttamente caricata su {}.", externalResource,
                    destinationPath);
            return destinationPath;

        } catch (IOException e) {
            LOGGER.warn("ConfigServlet::init: Impossibile caricare la risorsa {}. Utilizzo il valore predefinito {}.",
                    externalResource, defaultRelativePath, e);

            return defaultRelativePath;
        }
    }

    /**
     * Carica una risorsa e la mette a disposizione della webapp. Attualmente sono supportati i seguenti schema:
     * <ul>
     * <li>file://</li></li> http(s)://</li>
     * </ul>
     * 
     * Esempio di chiamata:
     * <ul>
     * <li>loadResource("file:///opt/jboss/logo1_snap.png", "/img/logo1.png")</li>
     * <li>loadResource("https:///my.beautiful.css/overlay.css", "/css/overlay.css")</li>
     * </ul>
     * 
     * @param resourceURL
     *            URL della risorsa da caricare
     * @param destination
     *            percorso <em>relativo</em> della risorsa su cui copiare
     */
    private void loadResource(String resourceURL, String destinationPath) throws IOException {
        URL url = new URL(resourceURL);
        String applicationRealPath = getServletContext().getRealPath("/");
        Path newFile = Paths.get(applicationRealPath + destinationPath);
        URLConnection connection = url.openConnection();
        Files.copy(connection.getInputStream(), newFile, REPLACE_EXISTING);
    }

    /**
     * Stampa la lista dei parametri forniti ma non supportati dall'applicazione.
     *
     * @param config
     *            configurazione della servlet
     */
    private void handleNotSupportedConfig(ServletConfig config) {
        Enumeration<String> initParameterNames = config.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String name = initParameterNames.nextElement();
            if (!Arrays.asList(ConfigProperties.StandardProperty.toList()).contains(name)
                    && !Arrays.asList(ConfigProperties.URIProperty.toList()).contains(name)) {
                LOGGER.warn("ConfigServlet::init: Init param [{}] con valore [{}] non supportato", name,
                        config.getInitParameter(name));
            }
        }
    }

    @Override
    public String getServletInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("La servlet ").append(getClass().getName())
                .append(" supporta la seguente lista di parametri di inizializzazione:\n");
        for (String parametro : ConfigProperties.StandardProperty.toList()) {
            sb.append(" - ").append(parametro).append("\n");
        }
        for (String parametro : ConfigProperties.URIProperty.toList()) {
            sb.append(" - ").append(parametro).append("\n");
        }
        return sb.toString();

    }

}
