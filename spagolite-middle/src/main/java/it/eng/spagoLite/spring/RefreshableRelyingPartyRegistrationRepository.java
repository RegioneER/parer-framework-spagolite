package it.eng.spagoLite.spring;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Marco Iacolucci
 */
@Component
public class RefreshableRelyingPartyRegistrationRepository
        implements RelyingPartyRegistrationRepository, Iterable<RelyingPartyRegistration>, Runnable {

    private final Map<String, RelyingPartyRegistration> relyingPartyRegistrations = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshableRelyingPartyRegistrationRepository.class);
    private static final String FIRMA_NON_VALIDA = "Firma non valida!";

    /*
     * Certificato e chiave privata estratti dal jks dell'applicazione specifica
     */
    private X509Certificate certificato;
    private RSAPrivateKey chiavePrivata;

    private String nomeApplicazione;

    public RefreshableRelyingPartyRegistrationRepository(String nomeApplicazione) {
        this.nomeApplicazione = nomeApplicazione;
        caricaCertificato();
        // feccia subito il federation metadata alla costruzione dell'oggetto
        fetchMetadata();
    }

    @Override
    public RelyingPartyRegistration findByRegistrationId(String registrationId) {
        return this.relyingPartyRegistrations.get(registrationId);
    }

    @Override
    public Iterator<RelyingPartyRegistration> iterator() {
        return this.relyingPartyRegistrations.values().iterator();
    }

    /*
     * Effettua il fetch del federationMetadata dall'URL configurato, poi ne verifica l'integritÃ  e la firma e poi
     * carica tutti gli IDP trovati nello stesso.
     */
    void fetchMetadata() {
        LOGGER.info("Fetching metadata da Keycloak...");
        this.relyingPartyRegistrations.clear();
        String fedMetadata = fetchAndCacheMetadata();
        LOGGER.debug("FederationMetadata: {}", fedMetadata);

        String applicationEntityId = System.getProperty(nomeApplicazione + "-sp-identity-id");
        if (applicationEntityId == null || applicationEntityId.isEmpty()) {
            LOGGER.warn("Non è stato definito il parametro {}-sp-identity-id in JBoss!", nomeApplicazione);
        } else {
            LOGGER.info("L'entity ID configurato sul server nel parametro {}-sp-identity-id è [{}]", nomeApplicazione,
                    applicationEntityId);
        }

        // Tutti gli IdPs
        try (InputStream in = org.apache.commons.io.IOUtils.toInputStream(fedMetadata, StandardCharsets.UTF_8)) {
            String spEntityId = System.getProperty(nomeApplicazione + "-sp-identity-id");
            RelyingPartyRegistrations.collectionFromMetadata(in).forEach(builder -> {
                builder.entityId(spEntityId).registrationId(nomeApplicazione)
                        .decryptionX509Credentials(
                                c -> c.add(Saml2X509Credential.decryption(chiavePrivata, certificato)))
                        .signingX509Credentials(c -> c.add(Saml2X509Credential.signing(chiavePrivata, certificato)))
                        .singleLogoutServiceResponseLocation("{baseUrl}/saml/SingleLogout/alias/{registrationId}")
                        .assertionConsumerServiceLocation("{baseUrl}/saml/SSO/alias/{registrationId}");

                RelyingPartyRegistration idpTemp = builder.build();
                String idpFed = idpTemp.getAssertingPartyDetails().getEntityId();
                /*
                 * Deve generare un registrationId univoco per ogni associazione tra idp e sp. Quello con nome
                 * "saceriam" è quello dell'associazione con keycloak vero sui derver e non il locale e nemmeno gli SPID
                 */
                String registrationId = nomeApplicazione;
                if (idpFed.contains(".lepida")) {
                    registrationId += "Lepida";
                } else if (idpFed.contains("localhost")) {
                    registrationId += "Localhost";
                }
                builder.registrationId(registrationId);
                RelyingPartyRegistration idp = builder.build();
                this.relyingPartyRegistrations.put(idp.getRegistrationId(), idp);
                LOGGER.info("IdP registrato {} per {}.", idp.getRegistrationId(),
                        idp.getAssertingPartyDetails().getEntityId());
            });

        } catch (IOException ex) {
            LOGGER.error("Errore nella lettura del file locale del federationMetadata", ex);
        }
        LOGGER.info("IdPs registrati {} - {}.", this.relyingPartyRegistrations.size(), this.relyingPartyRegistrations);
    }

    /*
     * Effettua il fetch del federationMetadata dall'USL parametrizzato e se non può collegarsi utilizza l'XML tenuto in
     * cache scaricato in precedenza.
     */
    private String fetchAndCacheMetadata() {
        String metadataUrl = System.getProperty("fed-metadata-url", "[URL federationMetadata NON IMPOSTATO!]");
        String fileTmp = System.getProperty(nomeApplicazione + "-temp-file",
                "tmp-" + nomeApplicazione + "-federation.xml");
        LOGGER.info("URL federationMetadata: {}", metadataUrl);
        RestTemplate t = getRestTemplate();
        String metadata = null;
        try {
            metadata = t.getForObject(metadataUrl, String.class);
            LOGGER.info("Letto il federationMetadata da {}", metadataUrl);
            checkFirmaEIntegrita(metadata);
        } catch (RestClientException ex) {
            LOGGER.error("Errore nel raggiungere il federationMetadata.", ex);
        }
        if (metadata != null) {
            try {
                Files.writeString(Path.of(fileTmp), metadata, StandardOpenOption.CREATE);
                LOGGER.info("Scritto file del federationMetadata in {}", Path.of(fileTmp));
            } catch (IOException ex) {
                LOGGER.error("Errore nello scrivere il file temporaneo del federationMetadata sotto {}",
                        Path.of(fileTmp), ex);
            }
        } else {
            try {
                metadata = Files.readString(Path.of(fileTmp));
                LOGGER.info("federationMetadata letto da file {}", fileTmp);

            } catch (IOException ex) {
                LOGGER.error("Errore nel leggere il file temporaneo del federationMetadata sotto {}", Path.of(fileTmp),
                        ex);
            }
        }
        return metadata;
    }

    /*
     * Carica dal keystore dell'applicazione specifica certificato e chiave privata.
     */
    private void caricaCertificato() {
        String keystorePath = System.getProperty(nomeApplicazione + "-jks-path");
        var keystorePassword = System.getProperty(nomeApplicazione + "-key-manager-pass");
        try (FileInputStream keyStoreInputStream = new FileInputStream(keystorePath)) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(keyStoreInputStream, keystorePassword.toCharArray());
            // carica il certificato della CA
            String storeKeyName = System.getProperty(nomeApplicazione + "-store-key-name");
            certificato = (X509Certificate) keyStore.getCertificate(storeKeyName);
            chiavePrivata = (RSAPrivateKey) keyStore.getKey(storeKeyName, keystorePassword.toCharArray());
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException ex) {
            throw new RuntimeException("Errore nel caricamento dei ccertificati dell'applicazione!", ex);
        }
    }

    /*
     * Effettua il check della firma e della validità  dell'XML firmato utilizzando il certificato pubblico contenuto
     * nello stesso XML, e controlla che il certificato che ha firmato sia stato emesso dalla CA della federazione.
     */
    private boolean checkFirmaEIntegrita(String metadata) {
        boolean isValid = false;

        try (InputStream inputStream = new ByteArrayInputStream(metadata.getBytes(StandardCharsets.UTF_8));) {
            Init.init();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setNamespaceAware(true);
            Document doc = dbf.newDocumentBuilder().parse(inputStream);

            Element sigElement = (Element) doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "Signature").item(0);
            XMLSignature signature = new XMLSignature(sigElement, "");

            KeyInfo ki = signature.getKeyInfo();
            if (ki != null) {
                PublicKey pk = ki.getPublicKey();
                if (pk != null) {
                    isValid = signature.checkSignatureValue(pk);
                    LOGGER.info("Firma federationMetadata VALIDA.");
                    // Controlla validità  del certificato che ha firmato il metadata contro la CA che lo ha emesso
                    X509Certificate certificatoCheHaFirmatoLaFederazione = ki.getX509Certificate();
                    String keystorePath = System.getProperty(nomeApplicazione + "-jks-path");
                    var keystorePassword = System.getProperty(nomeApplicazione + "-key-manager-pass");
                    FileInputStream keyStoreInputStream = new FileInputStream(keystorePath);
                    KeyStore keyStore = KeyStore.getInstance("JKS");
                    keyStore.load(keyStoreInputStream, keystorePassword.toCharArray());
                    // carica il certificato della CA
                    Certificate caCert = keyStore.getCertificate("federation_ca");
                    PublicKey caPublicKey = caCert.getPublicKey();
                    certificatoCheHaFirmatoLaFederazione.verify(caPublicKey);
                    LOGGER.info("Certificato emesso dalla CA della federazione ed è VALIDO.");
                } else {
                    LOGGER.warn(
                            "Firma federationMetadata NON VALIDA perché non è stata trovata la chiave pubblica del firmatario.");
                    throw new RuntimeException(FIRMA_NON_VALIDA);
                }
            } else {
                LOGGER.warn("No KeyInfo found in Signature");
                LOGGER.warn(
                        "Firma federationMetadata NON VALIDA perché non è stata trovato l'elemento KeyInfo nella firma.");
                throw new RuntimeException(FIRMA_NON_VALIDA);
            }
        } catch (ParserConfigurationException | SAXException | IOException | SignatureException | XMLSecurityException
                | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | CertificateException
                | KeyStoreException ex) {
            throw new RuntimeException(FIRMA_NON_VALIDA, ex);

        }
        return isValid;
    }

    /*
     * Crea un RestTemplate ad-hoc per la lettura con il timeout specifico per il metadata
     */
    private RestTemplate getRestTemplate() {
        String timeout = System.getProperty(nomeApplicazione + "-timeout-metadata", "10000");
        LOGGER.info("Timeout per federationMetadata: {}", timeout);
        SimpleClientHttpRequestFactory c = new SimpleClientHttpRequestFactory();
        c.setReadTimeout(Integer.parseInt(timeout));
        c.setConnectTimeout(Integer.parseInt(timeout));
        return new RestTemplate(c);
    }

    /*
     * Metodo chiamato a tempo dallo scheduler.
     */
    @Override
    public void run() {
        LOGGER.info("Lettura schedulata del federationMetadata.");
        fetchMetadata();
    }
}
