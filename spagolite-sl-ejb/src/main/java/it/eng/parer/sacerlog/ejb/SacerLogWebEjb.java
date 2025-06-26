/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.parer.sacerlog.ejb;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.criteria.CriteriaQuery;

import it.eng.paginator.helper.LazyListHelper;
import it.eng.parer.sacerlog.common.SacerLogEjbType;
import it.eng.parer.sacerlog.ejb.common.helper.ParamApplicHelper;
import it.eng.parer.sacerlog.ejb.helper.SacerLogHelper;
import it.eng.parer.sacerlog.slite.gen.viewbean.AplVLogTiOggRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.AplVLogTiOggTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVLisAsserzioniDatiRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVLisAsserzioniDatiTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVLisEventoOggettoRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVLisEventoOggettoTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVRicEventiRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVRicEventiTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVUsrAbilOrganizRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVUsrAbilOrganizTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVVisEventoPrincTxRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVVisOggettoRowBean;
import it.eng.parer.sacerlog.viewEntity.AplVLogTiOgg;
import it.eng.parer.sacerlog.viewEntity.ILogVRicEventi;
import it.eng.parer.sacerlog.viewEntity.LogVLisAsserzioniDati;
import it.eng.parer.sacerlog.viewEntity.LogVLisEventoOggetto;
import it.eng.parer.sacerlog.viewEntity.LogVRicEventi;
import it.eng.parer.sacerlog.viewEntity.LogVUsrAbilOrganiz;
import it.eng.parer.sacerlog.viewEntity.LogVVisEventoPrincTx;
import it.eng.parer.sacerlog.viewEntity.LogVVisOggetto;

/**
 *
 * @author Iacolucci_M
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SacerLogEjbType
public class SacerLogWebEjb {

    @EJB
    private SacerLogHelper sacerLogHelper;
    @EJB
    ParamApplicHelper paramApplicHelper;
    @EJB(mappedName = "java:app/paginator/LazyListHelper")
    protected LazyListHelper lazyListHelper;

    /*
     * Estrae il dettaglio da visualizzare nella form di visualizzazione log per oggetto
     */
    public LogVVisOggettoRowBean getDettOggettoByAppTipoOggettoId(String nomeApplicazione, String nomeTipoOggetto,
            BigDecimal idOggetto) {
        LogVVisOggetto ogg = sacerLogHelper.getDettOggettoByAppTipoOggettoId(nomeApplicazione, nomeTipoOggetto,
                idOggetto);
        if (ogg == null) {
            return null;
        } else {
            LogVVisOggettoRowBean bean = new LogVVisOggettoRowBean();
            bean.entityToRowBean(ogg);

            return bean;
        }
    }

    /*
     * Estrae il dettaglio da visualizzare nella form di visualizzazione log per oggetto
     */
    public LogVLisEventoOggettoTableBean getEventsByAppTipoOggettoId(String nomeApplicazione, String nomeTipoOggetto,
            BigDecimal idOggetto) {
        List<LogVLisEventoOggetto> lista = sacerLogHelper.getEventsByAppTipoOggettoId(nomeApplicazione, nomeTipoOggetto,
                idOggetto);
        LogVLisEventoOggettoTableBean tabella = new LogVLisEventoOggettoTableBean();
        for (LogVLisEventoOggetto ogg : lista) {
            LogVLisEventoOggettoRowBean riga = new LogVLisEventoOggettoRowBean();
            riga.setDsAzione(ogg.getDsAzione());
            riga.setDsGeneratoreAzione(ogg.getDsGeneratoreAzione());
            riga.setDtRegEvento(ogg.getDtRegEvento());
            riga.setIdAgente(ogg.getIdAgente());
            riga.setIdAgenteEvento(ogg.getIdAgenteEvento());
            riga.setIdApplic(ogg.getIdApplic());
            riga.setIdEvento(ogg.getIdEvento());
            riga.setIdOggetto(ogg.getIdOggetto());
            riga.setIdOggettoEvento(ogg.getIdOggettoEvento());
            riga.setIdTipoEvento(ogg.getIdTipoEvento());
            riga.setIdTipoOggetto(ogg.getIdTipoOggetto());
            riga.setNmAgente(ogg.getNmAgente());
            riga.setNmApplic(ogg.getNmApplic());
            riga.setNmTipoEvento(ogg.getNmTipoEvento());
            riga.setNmTipoOggetto(ogg.getNmTipoOggetto());
            riga.setTiRuoloAgenteEvento(ogg.getTiRuoloAgenteEvento());
            riga.setTipoClasseEvento(ogg.getTipoClasseEvento());
            riga.setTipoOrigineAgente(ogg.getTipoOrigineAgente());
            riga.setTipoOrigineEvento(ogg.getTipoOrigineEvento());
            riga.setString("calc_azione",
                    getStringAzioneCalcolata(ogg.getDsGeneratoreAzione(), ogg.getDsAzione(), ogg.getDsMotivoScript()));
            riga.setIdTransazione(ogg.getIdTransazione());
            tabella.add(riga);
        }
        return tabella;
    }

    // Compone il campo calcolato dell'azione di log
    public String getStringAzioneCalcolata(String dsGeneratoreAzione, String nmAzione, String dsMotivazione) {
        if (dsGeneratoreAzione == null || nmAzione == null) {
            return "Azione scaturita da altra applicazione";
        } else {
            return dsGeneratoreAzione + "/" + nmAzione + (dsMotivazione != null ? " (" + dsMotivazione + ")" : "");
        }
    }

    /*
     * Estrae il dettaglio da visualizzare nella form di visualizzazione log per oggetto
     */
    public LogVLisAsserzioniDatiTableBean getAsserzioniDatiByIdOggettoEvento(BigDecimal idOggettoEvento) {
        List<LogVLisAsserzioniDati> lista = sacerLogHelper.getAsserzioniDatiByIdOggettoEvento(idOggettoEvento);
        LogVLisAsserzioniDatiTableBean tabella = new LogVLisAsserzioniDatiTableBean();
        for (LogVLisAsserzioniDati ogg : lista) {
            LogVLisAsserzioniDatiRowBean riga = new LogVLisAsserzioniDatiRowBean();
            riga.setDsChiaveTipoRecord(ogg.getDsChiaveTipoRecord());
            riga.setDsModifica(ogg.getDsModifica());
            riga.setIdAsserzioneDeltaFoto(ogg.getIdAsserzioneDeltaFoto());
            riga.setIdOggettoEvento(ogg.getIdOggettoEvento());
            riga.setIdRecord(ogg.getIdRecord());
            riga.setIdRecordPadre(ogg.getIdRecordPadre());
            riga.setLabelCampo(ogg.getLabelCampo());
            riga.setNmTipoRecord(ogg.getNmTipoRecord());
            riga.setPath(ogg.getPath());
            riga.setPathKey(ogg.getPathKey());
            riga.setTipoModifica(ogg.getTipoModifica());
            riga.setTipoValore(ogg.getTipoValore());
            String flg1 = "0";
            String flg2 = "0";
            /*
             * La logica di apparizione dei link per i valori troppo grandi è la seguente:
             *
             * Dalla vista se tipo _valore = 0 allora il valore è < 254 caratteri e quindi è un campo normale. Se tipo
             * valore = 1 il valore <=4000 caratteri ed è ancora un campo normale. Se tipo valore = 2 il valore > 4000
             * caratteri ed un CLOB.
             *
             * Se però nel caso in cui nel tipo_valore = 1 è presente una stringa contenente un XML allora il sistema
             * deve copiare la stringa dal campo caratteri normale a quello contenente il CLOB. Se invece non c'è un xml
             * allora il valore è considerato ancora un tipo caratteri normale ed appare il primo link.
             */
            if (ogg.getTipoValore() != null && ogg.getTipoValore().equals("1")) {
                if ((ogg.getDsValoreNewCampo() != null && ogg.getDsValoreNewCampo().contains("<?xml"))
                        || (ogg.getDsValoreOldCampo() != null && ogg.getDsValoreOldCampo().contains("<?xml"))) {
                    riga.setBlValoreNewCampo(ogg.getDsValoreNewCampo());
                    riga.setBlValoreOldCampo(ogg.getDsValoreOldCampo());
                    flg2 = "1";
                } else {
                    riga.setDsValoreNewCampo(ogg.getDsValoreNewCampo());
                    riga.setDsValoreOldCampo(ogg.getDsValoreOldCampo());
                    flg1 = "1";
                }
            } else if (ogg.getTipoValore() != null && ogg.getTipoValore().equals("2")) {
                riga.setBlValoreNewCampo(ogg.getBlValoreNewCampo());
                riga.setBlValoreOldCampo(ogg.getBlValoreOldCampo());
                flg2 = "1";
            } else {
                riga.setDsValoreNewCampo(ogg.getDsValoreNewCampo());
                riga.setDsValoreOldCampo(ogg.getDsValoreOldCampo());
            }
            riga.setString("fl_link_valore_tipo_1", flg1);
            riga.setString("fl_link_valore_tipo_2", flg2);
            riga.setString("mostraValoriTipo1", "visualizza");
            riga.setString("mostraValoriTipo2", "visualizza");
            tabella.add(riga);
        }
        return tabella;
    }

    /*
     * Estrae la lista dei tipi oggetto per nome applicazione in formato tableBean
     */
    public AplVLogTiOggTableBean getTipiOggettoByAppName(String nomeApplicazione) {
        return getTipiOggettoByAppName(nomeApplicazione, null);
    }

    /*
     * Estrae la lista dei tipi oggetto per nome applicazione in formato tableBean
     */
    public AplVLogTiOggTableBean getTipiOggettoByAppName(String nomeApplicazione, String nomeTipoOggettoDaEscludere) {
        List<AplVLogTiOgg> lista = sacerLogHelper.findAplVLogTiOggByAppName(nomeApplicazione,
                nomeTipoOggettoDaEscludere);
        if (lista == null || lista.isEmpty()) {
            return null;
        } else {
            AplVLogTiOggTableBean tabella = new AplVLogTiOggTableBean();

            for (AplVLogTiOgg aplVLogTiOgg : lista) {
                AplVLogTiOggRowBean riga = new AplVLogTiOggRowBean();
                riga.entityToRowBean(aplVLogTiOgg);
                tabella.add(riga);
            }
            return tabella;
        }
    }

    /*
     * Estrae la lista dei tipi oggetto per nome applicazione in formato tableBean
     */
    public LogVVisEventoPrincTxRowBean getLogVVisEventoPrincTxById(BigDecimal idTransazione) {
        LogVVisEventoPrincTx rec = sacerLogHelper.findTransazioneById(idTransazione);
        if (rec == null) {
            return null;
        } else {
            LogVVisEventoPrincTxRowBean row = new LogVVisEventoPrincTxRowBean();
            row.entityToRowBean(rec);
            row.setString("azione_composita",
                    getStringAzioneCalcolata(rec.getDsGeneratoreAzione(), rec.getDsAzione(), rec.getDsMotivoScript()));
            return row;
        }
    }

    /*
     * Estrae la lista dei tipi oggetto per nome applicazione in formato tableBean
     */
    public LogVUsrAbilOrganizTableBean getOrganizzazioniByApplicAndUsrId(String nomeApplicazione,
            BigDecimal idUserIam) {
        List<LogVUsrAbilOrganiz> lista = sacerLogHelper.findUsrVAbilOrganizByApplicAndUser(nomeApplicazione, idUserIam);
        if (lista == null || lista.isEmpty()) {
            return null;
        } else {
            LogVUsrAbilOrganizTableBean tabella = new LogVUsrAbilOrganizTableBean();
            for (LogVUsrAbilOrganiz rec : lista) {
                LogVUsrAbilOrganizRowBean riga = new LogVUsrAbilOrganizRowBean();
                riga.entityToRowBean(rec);
                tabella.add(riga);
            }
            return tabella;
        }
    }

    /*
     * Estrae la lista degli eventi di log secondo i parametri impostatati sull'interfaccia in formato tableBean
     */
    public LogVRicEventiTableBean getEventiByOrgTipoClasseDates(String nmApplicazione, String nmOggetto,
            BigDecimal idOrganizzazione, BigDecimal idTipoOggetto, String classeEvento, Date DataDa, Date DataA,
            Integer maxResults) {
        CriteriaQuery cq = sacerLogHelper.findEventiByOrgTipoClasseDates(nmApplicazione, nmOggetto, idOrganizzazione,
                idTipoOggetto, classeEvento, DataDa, DataA);
        return lazyListHelper.getTableBean(cq, maxResults, this::getTableBeanFrom);
    }

    public LogVRicEventiTableBean getTableBeanFrom(List<ILogVRicEventi> lista) {
        if (lista == null || lista.isEmpty()) {
            return null;
        } else {
            LogVRicEventiTableBean tabella = new LogVRicEventiTableBean();
            for (ILogVRicEventi rec : lista) {
                LogVRicEventiRowBean riga = new LogVRicEventiRowBean();
                riga.entityToRowBean(rec);
                if (rec.getNmAmbiente() != null) {
                    riga.setString("nm_organizzazione", rec.getNmAmbiente());
                }
                if (rec.getNmAmbiente() != null && rec.getNmEnte() != null && rec.getNmStruttura() != null) {
                    riga.setString("nm_organizzazione",
                            rec.getNmAmbiente() + "/" + rec.getNmEnte() + "/" + rec.getNmStruttura());
                } else if (rec.getNmAmbiente() != null && rec.getNmEnte() != null) {
                    riga.setString("nm_organizzazione", rec.getNmAmbiente() + "/" + rec.getNmEnte());
                } else {
                    if (rec.getNmVersatore() != null && rec.getNmAmbiente() != null) {
                        riga.setString("nm_organizzazione", rec.getNmAmbiente() + "/" + rec.getNmVersatore());
                    }
                }
                riga.setString("azione_composita", getStringAzioneCalcolata(rec.getNmGeneratoreAzione(),
                        rec.getNmAzione(), rec.getDsMotivoScript()));
                tabella.add(riga);
            }
            return tabella;
        }
    }

    public LogVRicEventiTableBean getOggettiByIdEvento(BigDecimal idEvento) {
        List<LogVRicEventi> lista = sacerLogHelper.findOggettiByIdEvento(idEvento);
        if (lista == null || lista.isEmpty()) {
            return null;
        } else {
            LogVRicEventiTableBean tabella = new LogVRicEventiTableBean();
            for (LogVRicEventi rec : lista) {
                LogVRicEventiRowBean riga = new LogVRicEventiRowBean();
                riga.entityToRowBean(rec);

                if (rec.getNmAmbiente() != null) {
                    riga.setString("nm_organizzazione", rec.getNmAmbiente());
                }
                if (rec.getNmAmbiente() != null && rec.getNmEnte() != null && rec.getNmStruttura() != null) {
                    riga.setString("nm_organizzazione",
                            rec.getNmAmbiente() + "/" + rec.getNmEnte() + "/" + rec.getNmStruttura());
                } else if (rec.getNmAmbiente() != null && rec.getNmEnte() != null) {
                    riga.setString("nm_organizzazione", rec.getNmAmbiente() + "/" + rec.getNmEnte());
                } else {
                    if (rec.getNmVersatore() != null && rec.getNmAmbiente() != null) {
                        riga.setString("nm_organizzazione", rec.getNmAmbiente() + "/" + rec.getNmVersatore());
                    }
                }
                riga.setString("azione_composita", getStringAzioneCalcolata(rec.getNmGeneratoreAzione(),
                        rec.getNmAzione(), rec.getDsMotivoScript()));
                tabella.add(riga);
            }
            return tabella;
        }
    }

    public LogVRicEventiTableBean getEventiByIdtransazioneExcludingEvento(BigDecimal idTransazione,
            BigDecimal idEvento) {
        List<LogVRicEventi> lista = sacerLogHelper.findEventiByIdTransazioneExcludingIdEvento(idTransazione, idEvento);
        if (lista == null || lista.isEmpty()) {
            return null;
        } else {
            LogVRicEventiTableBean tabella = new LogVRicEventiTableBean();
            for (LogVRicEventi rec : lista) {
                LogVRicEventiRowBean riga = new LogVRicEventiRowBean();
                riga.setString("azione_composita", getStringAzioneCalcolata(rec.getNmGeneratoreAzione(),
                        rec.getNmAzione(), rec.getDsMotivoScript()));
                riga.entityToRowBean(rec);
                tabella.add(riga);
            }
            return tabella;
        }
    }

}
