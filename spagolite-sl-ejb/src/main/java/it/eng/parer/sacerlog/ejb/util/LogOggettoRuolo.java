package it.eng.parer.sacerlog.ejb.util;

import java.math.BigDecimal;

/**
 *
 * @author Iacolucci_M
 */
public class LogOggettoRuolo {
    private String nmTipoOggetto;
    private String tiRuoloPremis;
    private BigDecimal idOggetto;

    public String getNmTipoOggetto() {
        return nmTipoOggetto;
    }

    public void setNmTipoOggetto(String nmTipoOggetto) {
        this.nmTipoOggetto = nmTipoOggetto;
    }

    public String getTiRuoloPremis() {
        return tiRuoloPremis;
    }

    public void setTiRuoloPremis(String tiRuoloPremis) {
        this.tiRuoloPremis = tiRuoloPremis;
    }

    public BigDecimal getIdOggetto() {
        return idOggetto;
    }

    public void setIdOggetto(BigDecimal idOggetto) {
        this.idOggetto = idOggetto;
    }

    public LogOggettoRuolo(String nmTipoOggetto, BigDecimal idOggetto, String tiRuoloPremis) {
        this.nmTipoOggetto = nmTipoOggetto;
        this.tiRuoloPremis = tiRuoloPremis;
        this.idOggetto = idOggetto;
    }

    public LogOggettoRuolo(String nmTipoOggetto, BigDecimal idOggetto) {
        this.nmTipoOggetto = nmTipoOggetto;
        this.idOggetto = idOggetto;
    }

}
