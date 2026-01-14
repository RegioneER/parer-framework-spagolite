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

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package it.eng.spagoCore;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class ConfigProperties {

    // Valori predefiniti.

    private static final String LOGOS_BASE_DIR = "/img/logos";
    private static final String IMG_EXT_BASE_DIR = "/img/external";
    private static final String ONEGB = "1000000000";
    private static final String ONEMB = "10000000";
    private static final String TWENTYMB = "20000000";

    // Percorsi interni su cui verranno mappate le risorse esterne

    private static final String LOGO_APP_PATH_FROM_EXT = IMG_EXT_BASE_DIR + "/logo_app.png";
    private static final String LOGO_1_PATH_FROM_EXT = IMG_EXT_BASE_DIR + "/logo1.png";
    private static final String LOGO_2_PATH_FROM_EXT = IMG_EXT_BASE_DIR + "/logo2.png";
    private static final String LOGO_3_PATH_FROM_EXT = IMG_EXT_BASE_DIR + "/logo3.png";
    private static final String FAV_ICON_PATH_FROM_EXT = IMG_EXT_BASE_DIR + "/favicon.ico";
    private static final String CSS_OVER_PATH_FROM_EXT = "/css/external/slForms-over.css";

    public enum StandardProperty {

        DISABLE_SECURITY("disableSecurity", Boolean.FALSE.toString()),
        ENABLE_LAZY_SORT("enableLazySort", Boolean.TRUE.toString()),
        DEBUG_AUTHORIZATION("debugAuthorization", Boolean.FALSE.toString()),
        LOGO_APP_ALT("logoApp_alt", StringUtils.EMPTY),
        LOGO_APP_URL("logoApp_url", StringUtils.EMPTY),
        LOGO_APP_TITLE("logoApp_title", StringUtils.EMPTY),
        LOGO_1_ALT("logo1_alt", StringUtils.EMPTY), LOGO_1_URL("logo1_url", StringUtils.EMPTY),
        LOGO_1_TITLE("logo1_title", StringUtils.EMPTY), LOGO_2_ALT("logo2_alt", StringUtils.EMPTY),
        LOGO_2_URL("logo2_url", StringUtils.EMPTY), LOGO_2_TITLE("logo2_title", StringUtils.EMPTY),
        LOGO_3_ALT("logo3_alt", StringUtils.EMPTY), LOGO_3_URL("logo3_url", StringUtils.EMPTY),
        LOGO_3_TITLE("logo3_title", StringUtils.EMPTY),
        TITOLO_APPLICATIVO("titolo_applicativo", "Titolo Applicazione"),
        AMBIENTE("ambiente_deploy", StringUtils.EMPTY),
        ENABLE_HELP_ONLINE("enableHelpOnline", Boolean.FALSE.toString()),
        WS_INSTANCE_NAME("ws.instanceName", "minefield"),
        WS_STAGING_UPLOAD_DIR("ws.upload.directory", System.getProperty("java.io.tmpdir")),
        VERSAMENTO_SYNC_SAVE_LOG_SESSION("versamentoSync.saveLogSession", Boolean.TRUE.toString()),
        VERSAMENTO_SYNC_MAX_REQUEST_SIZE("versamentoSync.maxRequestSize", ONEGB),
        VERSAMENTO_SYNC_MAX_FILE_SIZE("versamentoSync.maxFileSize", ONEGB),
        AGG_ALLEGATI_SAVE_LOG_SESSION("aggAllegati.saveLogSession", Boolean.TRUE.toString()),
        AGG_ALLEGATI_MAX_REQUEST_SIZE("aggAllegati.maxRequestSize", ONEGB),
        AGG_ALLEGATI_MAX_FILE_SIZE("aggAllegati.maxFileSize", ONEGB),
        PROFILER_APP_UPLOAD_DIR("profilerApp.upload.directory",
                System.getProperty("java.io.tmpdir")),
        PROFILER_APP_MAX_REQUEST_SIZE("profilerApp.maxRequestSize", ONEGB),
        PROFILER_APP_MAX_FILE_SIZE("profilerApp.maxFileSize", ONEMB),
        PROFILER_APP_CHARSET("profilerApp.charset", StandardCharsets.UTF_8.name()),
        RECUPERO_SYNC_SAVE_LOG_SESSION("recuperoSync.saveLogSession", Boolean.FALSE.toString()),
        RECUPERO_SYNC_MAX_RESPONSE_SIZE("recuperoSync.maxResponseSize", TWENTYMB),
        RECUPERO_SYNC_MAX_FILE_SIZE("recuperoSync.maxFileSize", TWENTYMB),
        LOAD_XSD_APP_UPLOAD_DIR("loadXsdApp.upload.directory",
                System.getProperty("java.io.tmpdir")),
        LOAD_XSD_APP_MAX_REQUEST_SIZE("loadXsdApp.maxRequestSize", ONEGB),
        LOAD_XSD_APP_MAX_FILE_SIZE("loadXsdApp.maxFileSize", ONEMB),
        LOAD_XSD_APP_CHARSET("loadXsdApp.charset", StandardCharsets.UTF_8.name()),
        HELP_ONLINE_MAX_FILE_SIZE("helpOnline.upload.maxFileSize", ONEMB),
        MODULO_INFORMAZIONI_MAX_FILE_SIZE("moduloInformazioni.upload.maxFileSize", ONEMB),
        VARIAZIONE_ACCORDO_MAX_FILE_SIZE("variazioneAccordo.upload.maxFileSize", ONEMB),
        DISCIPLINARE_TECNICO_MAX_FILE_SIZE("disciplinareTecnico.upload.maxFileSize", ONEMB),
        DOC_PROCESSO_CONSERVAZIONE_MAX_FILE_SIZE("docProcessoConservazione.upload.maxFileSize",
                ONEMB),
        IMPORT_VERSATORE_MAX_FILE_SIZE("importVersatore.upload.maxFileSize", ONEMB),
        PARAMS_CSV_MAX_FILE_SIZE("parameters.csv.maxFileSize", ONEMB),
        SERVER_NAME_PROPERTY("serverName.property", "jboss.node.name"),
        URI_VERSMENTO_SYNC("uri.versamentoSync", StringUtils.EMPTY);

        private final String propName;
        private final String propDefaultValue;

        private StandardProperty(String propName, String propDefaultValue) {
            this.propName = propName;
            this.propDefaultValue = propDefaultValue;
        }

        public String getPropName() {
            return propName;
        }

        public String getPropDefaultValue() {
            return propDefaultValue;
        }

        public static String[] toList() {
            return Stream.of(ConfigProperties.StandardProperty.values())
                    .map(StandardProperty::getPropName).toArray(String[]::new);
        }

        @Override
        public String toString() {
            return Stream.of(toList()).collect(Collectors.joining(","));
        }

    }

    public enum URIProperty {

        LOGO_APP_RELATIVE("logoApp_absolutePath", StringUtils.EMPTY, "logoApp_relativePath",
                LOGOS_BASE_DIR + "/logo_app.png", LOGO_APP_PATH_FROM_EXT),
        LOGO_1_RELATIVE("logo1_absolutePath", StringUtils.EMPTY, "logo1_relativePath",
                LOGOS_BASE_DIR + "/logo1.png", LOGO_1_PATH_FROM_EXT),
        LOGO_2_RELATIVE("logo2_absolutePath", StringUtils.EMPTY, "logo2_relativePath",
                LOGOS_BASE_DIR + "/logo2.png", LOGO_2_PATH_FROM_EXT),
        LOGO_3_RELATIVE("logo3_absolutePath", StringUtils.EMPTY, "logo3_relativePath",
                LOGOS_BASE_DIR + "/logo3.png", LOGO_3_PATH_FROM_EXT),
        FAV_ICON_RELATIVE("favicon_absolutePath", StringUtils.EMPTY, "favicon_relativePath",
                "/img/favicon.ico", FAV_ICON_PATH_FROM_EXT),
        CSS_OVER_RELATIVE("cssover_absolutePath", StringUtils.EMPTY, "cssover_relativePath",
                "/css/slForms-over.css", CSS_OVER_PATH_FROM_EXT);

        private final String propAbsolutePathName;
        private final String propAbsoluteDefaultValue;
        private final String propRelativePathName;
        private final String propRelativeDefaultValue;
        private final String destination;

        private URIProperty(String propAbsolutePathName, String propAbsoluteDefaultValue,
                String propRelativePathName, String propRelativeDefaultValue, String destination) {
            this.propAbsolutePathName = propAbsolutePathName;
            this.propAbsoluteDefaultValue = propAbsoluteDefaultValue;
            this.propRelativePathName = propRelativePathName;
            this.propRelativeDefaultValue = propRelativeDefaultValue;
            this.destination = destination;
        }

        public String getPropAbsolutePathName() {
            return propAbsolutePathName;
        }

        public String getPropAbsoluteDefaultValue() {
            return propAbsoluteDefaultValue;
        }

        public String getPropRelativePathName() {
            return propRelativePathName;
        }

        public String getPropRelativeDefaultValue() {
            return propRelativeDefaultValue;
        }

        public String getDestination() {
            return destination;
        }

        public static String[] toList() {
            return Stream.concat(
                    Arrays.asList(URIProperty.values()).stream()
                            .map(URIProperty::getPropAbsolutePathName),
                    Arrays.asList(URIProperty.values()).stream()
                            .map(URIProperty::getPropRelativePathName))
                    .toArray(String[]::new);
        }

        @Override
        public String toString() {
            return Stream.of(toList()).collect(Collectors.joining(","));

        }

    }
}
