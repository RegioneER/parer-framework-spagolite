package it.eng.spagoLite.security;

import it.eng.spagoLite.security.profile.Profile;
import java.math.BigDecimal;
import java.util.Map;

public class User extends BaseUser<Profile> {

    private static final long serialVersionUID = 1L;
    private BigDecimal idOrganizzazioneFoglia;
    private String nomeStruttura;
    private String sistemaVersante;
    private Map<String, String> configurazione;
    private Long idApplicazione;
    private Map<String, String> organizzazioneMap;
    private boolean utenteDaAssociare;
    private String codiceFiscale;

    public User() {
        super("Menu", "Menu principale");
    }

    public BigDecimal getIdOrganizzazioneFoglia() {
        return idOrganizzazioneFoglia;
    }

    public void setIdOrganizzazioneFoglia(BigDecimal idOrganizzazioneFoglia) {
        this.idOrganizzazioneFoglia = idOrganizzazioneFoglia;
    }

    public String getNomeStruttura() {
        return nomeStruttura;
    }

    public void setNomeStruttura(String nomeStruttura) {
        this.nomeStruttura = nomeStruttura;
    }

    public Map<String, String> getConfigurazione() {
        return configurazione;
    }

    public void setConfigurazione(Map<String, String> configurazione) {
        this.configurazione = configurazione;
    }

    public Long getIdApplicazione() {
        return idApplicazione;
    }

    public void setIdApplicazione(Long idApplicazione) {
        this.idApplicazione = idApplicazione;
    }

    @Override
    public Map<String, String> getOrganizzazioneMap() {
        return organizzazioneMap;
    }

    public void setOrganizzazioneMap(Map<String, String> organizzazioneMap) {
        this.organizzazioneMap = organizzazioneMap;
    }

    public boolean isUtenteDaAssociare() {
        return utenteDaAssociare;
    }

    public void setUtenteDaAssociare(boolean utenteDaAssociare) {
        this.utenteDaAssociare = utenteDaAssociare;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getSistemaVersante() {
        return sistemaVersante;
    }

    public void setSistemaVersante(String sistemaVersante) {
        this.sistemaVersante = sistemaVersante;
    }

}
