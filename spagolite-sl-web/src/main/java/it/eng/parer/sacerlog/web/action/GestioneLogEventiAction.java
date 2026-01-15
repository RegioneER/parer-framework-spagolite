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
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.sacerlog.web.action;

import it.eng.parer.sacerlog.common.Constants;
import it.eng.parer.sacerlog.ejb.SacerLogWebEjb;
import it.eng.parer.sacerlog.ejb.common.helper.ParamApplicHelper;
import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.security.Secure;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.ejb.EJB;
import it.eng.parer.sacerlog.ejb.helper.SacerLogHelper;
import it.eng.parer.sacerlog.ejb.util.PremisEnums;
import it.eng.parer.sacerlog.slite.gen.Application;
import it.eng.parer.sacerlog.slite.gen.action.GestioneLogEventiAbstractAction;
import it.eng.parer.sacerlog.slite.gen.form.GestioneLogEventiForm;
import it.eng.parer.sacerlog.slite.gen.viewbean.AplVLogTiOggTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVLisAsserzioniDatiTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVLisEventoOggettoTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVRicEventiTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVUsrAbilOrganizTableBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVLisAsserzioniDatiRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVRicEventiRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVVisEventoPrincTxRowBean;
import it.eng.parer.sacerlog.slite.gen.viewbean.LogVVisOggettoRowBean;
import it.eng.parer.sacerlog.web.validator.TypeValidator;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.row.BaseRow;
import it.eng.spagoLite.db.base.table.BaseTable;
import it.eng.spagoLite.db.oracle.decode.DecodeMap;
import it.eng.spagoLite.form.list.List;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Iacolucci_M
 */
@Controller
public class GestioneLogEventiAction extends GestioneLogEventiAbstractAction {

    private static Logger log = LoggerFactory.getLogger(GestioneLogEventiAction.class);
    /*
     * Costante per memorizare nella sessione l'idEventoOggetto che poi servirà nella
     * visualizzazione foto che è richiamabile da diversi percorsi
     */
    private static final String EVENTO_OGGETTO_PER_LOG = "idEventoOggettoPerLog";
    /*
     * Costante per memorizare nella sessione l'idEventoOggetto che poi servirà nella
     * visualizzazione foto che è richiamabile da diversi percorsi
     */
    private static final String OGGETTO_LISTA_NAVIGAZIONE = "oggListaNav";

    private static final String IS_APPLICAZIONE_IAM = "isAppIam";
    private static final String MOSTRA_AGENTE = "mostraAgente";

    @EJB(mappedName = "java:app/sacerlog-ejb/SacerLogWebEjb")
    private SacerLogWebEjb sacerLogWebEjb;
    @EJB(mappedName = "java:app/sacerlog-ejb/SacerLogHelper")
    private SacerLogHelper sacerLogHelper;
    @EJB(mappedName = "java:app/sacerlog-ejb/ParamApplicHelper")
    private ParamApplicHelper paramApplicHelper;

    @Override
    public void initOnClick() throws EMFError {
        log.debug("**************** initOnClick ******************");
    }

    @Override
    public void loadDettaglio() throws EMFError {
        log.debug("**************** loadDettaglio ******************");
        if (getNavigationEvent().equals(ListAction.NE_DETTAGLIO_VIEW)
                || getNavigationEvent().equals(ListAction.NE_PREV)
                || getNavigationEvent().equals(ListAction.NE_NEXT)) {
            if (getTableName().equals(getForm().getListaEventi().getName())) {
                log.debug("SI PROVIENE DALLA LISTA EVENTI");
                int riga = getForm().getListaEventi().getTable().getCurrentRowIndex();
                String valore = null;
                Timestamp data = getForm().getListaEventi().getTable().getRow(riga)
                        .getTimestamp("dt_reg_evento");
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                getForm().getEventoDetail().getDt_reg_evento().setValue(df.format(data));
                valore = getForm().getListaEventi().getTable().getRow(riga)
                        .getString("nm_tipo_evento");
                getForm().getEventoDetail().getNm_tipo_evento().setValue(valore);
                valore = getForm().getListaEventi().getTable().getRow(riga)
                        .getString("calc_azione");
                getForm().getEventoDetail().getCalc_azione().setValue(valore);
                valore = getForm().getListaEventi().getTable().getRow(riga).getString("nm_agente");
                getForm().getEventoDetail().getNm_agente().setValue(valore);
                BigDecimal valoreId = getForm().getListaEventi().getTable().getRow(riga)
                        .getBigDecimal("id_transazione");
                getForm().getEventoDetail().getId_transazione().setValue(valoreId.toPlainString());

                // Legge i ruoli che aveva l'utente che ha generato l'evento che si sta
                // visualizzando
                cercaRuoliUtente(Constants.nmApplic.SACER_IAM.name(),
                        getForm().getListaEventi().getTable().getRow(riga).getString("nm_applic"),
                        getForm().getListaEventi().getTable().getRow(riga).getString("nm_agente"),
                        data);
                BigDecimal record = getForm().getListaEventi().getTable().getRow(riga)
                        .getBigDecimal("id_oggetto_evento");
                LogVLisAsserzioniDatiTableBean table = sacerLogWebEjb
                        .getAsserzioniDatiByIdOggettoEvento(record);
                getForm().getListaModifiche().setTable(table);
                getForm().getListaModifiche().getTable().setPageSize(10);
                getForm().getListaModifiche().getTable().first();
                getForm().getEventoDetail().getVisualizzaFoto().setEditMode();
                getForm().getEventoDetail().getVisualizzaDatiCompleti().setEditMode();
                getForm().getValoreXml().getScarica_xml().setEditMode();
                /* Memorizza l'idEgentoOggetto per la futura visualizzazione foto */
                getSession().setAttribute(EVENTO_OGGETTO_PER_LOG, record);
                // memorizza il nome della lista che servirà alla JSP per visualizzare le frecce
                // di navigazione
                // opportune
                getSession().setAttribute(OGGETTO_LISTA_NAVIGAZIONE, getTableName());
            } else if (getTableName().equals(getForm().getRicercaEventiList().getName())) {
                log.debug("SI PROVIENE DALLA LISTA Ricerca EVENTI");
                popolaDettaglioEventoOggettoDaRicercaEventi(getForm().getRicercaEventiList());
                /* Memorizza l'idEgentoOggetto per la futura visualizzazione foto */
                LogVRicEventiRowBean row = (LogVRicEventiRowBean) getForm().getRicercaEventiList()
                        .getTable().getCurrentRow();
                getSession().setAttribute(EVENTO_OGGETTO_PER_LOG, row.getIdOggettoEvento());
                // memorizza il nome della lista che servirà alla JSP per visualizzare le frecce
                // di navigazione
                // opportune
                getSession().setAttribute(OGGETTO_LISTA_NAVIGAZIONE, getTableName());
            } else if (getTableName()
                    .equals(getForm().getOggettiEventoPrincipaleList().getName())) {
                log.debug("SI PROVIENE DALLA LISTA Oggetti evento principale");
                popolaDettaglioEventoOggettoDaRicercaEventi(
                        getForm().getOggettiEventoPrincipaleList());
                /* Memorizza l'idEgentoOggetto per la futura visualizzazione foto */
                LogVRicEventiRowBean row = (LogVRicEventiRowBean) getForm()
                        .getOggettiEventoPrincipaleList().getTable().getCurrentRow();
                getSession().setAttribute(EVENTO_OGGETTO_PER_LOG, row.getIdOggettoEvento());
                // memorizza il nome della lista che servirà alla JSP per visualizzare le frecce
                // di navigazione
                // opportune
                getSession().setAttribute(OGGETTO_LISTA_NAVIGAZIONE, getTableName());
            } else if (getTableName().equals(getForm().getEventiSuccessiviList().getName())) {
                log.debug("SI PROVIENE DALLA LISTA eventi successivi");
                popolaDettaglioEventoOggettoDaRicercaEventi(getForm().getEventiSuccessiviList());
                /* Memorizza l'idEgentoOggetto per la futura visualizzazione foto */
                LogVRicEventiRowBean row = (LogVRicEventiRowBean) getForm()
                        .getEventiSuccessiviList().getTable().getCurrentRow();
                getSession().setAttribute(EVENTO_OGGETTO_PER_LOG, row.getIdOggettoEvento());
                // memorizza il nome della lista che servirà alla JSP per visualizzare le frecce
                // di navigazione
                // opportune
                getSession().setAttribute(OGGETTO_LISTA_NAVIGAZIONE, getTableName());
            } else {
                getSession().setAttribute(EVENTO_OGGETTO_PER_LOG, new BigDecimal(0));
            }
        }
    }

    @Override
    public void undoDettaglio() throws EMFError {
        log.debug("**************** undoDettaglio ******************");
    }

    @Override
    public void insertDettaglio() throws EMFError {
        log.debug("**************** insertDettaglio ******************");
    }

    @Override
    public void saveDettaglio() throws EMFError {
        log.debug("**************** saveDettaglio ******************");
    }

    @Override
    public void dettaglioOnClick() throws EMFError {
        log.debug("**************** dettaglioOnClick ******************");
        forwardToPublisher(Application.Publisher.DETTAGLIO_EVENTO_OGGETTO);
    }

    @Override
    public void elencoOnClick() throws EMFError {
        log.debug("**************** elencoOnClick ******************");
        goBack();
    }

    @Override
    protected String getDefaultPublsherName() {
        return Application.Publisher.GESTIONE_LOG_EVENTI;
    }

    @Override
    public void reloadAfterGoBack(String string) {
        log.debug("**************** reloadAfterGoBack ******************");
    }

    @Override
    public String getControllerName() {
        return Application.Actions.GESTIONE_LOG_EVENTI;
    }

    // @Secure(action = "Menu.UnitaDocumentarie.UnitaDocumentarieRicercaAvanzata")
    public void inizializzaLogEventi() throws EMFError {
        log.debug("**************** inizializzaLogEventi ******************");
        GestioneLogEventiForm form = getForm();
        LogVVisOggettoRowBean bean = sacerLogWebEjb.getDettOggettoByAppTipoOggettoId(
                form.getOggettoDetail().getNmApp().getValue(),
                form.getOggettoDetail().getNm_tipo_oggetto().getValue(),
                form.getOggettoDetail().getIdOggetto().parse());
        if (bean != null) {
            form.getOggettoDetail().getNm_ambiente().setValue(bean.getNmAmbiente());

            String str = bean.getNmAmbiente();
            if (str == null) {
                form.getOggettoDetail().getNm_ambiente().setHidden(true);
            } else {
                form.getOggettoDetail().getNm_ambiente().setValue(str);
            }
            str = bean.getNmVersatore();
            if (str == null) {
                form.getOggettoDetail().getNm_versatore().setHidden(true);
            } else {
                form.getOggettoDetail().getNm_versatore().setValue(str);
            }
            str = bean.getNmEnte();
            if (str == null) {
                form.getOggettoDetail().getNm_ente().setHidden(true);
            } else {
                form.getOggettoDetail().getNm_ente().setValue(str);
            }
            str = bean.getNmStruttura();
            if (str == null) {
                form.getOggettoDetail().getNm_strut().setHidden(true);
            } else {
                form.getOggettoDetail().getNm_strut().setValue(str);
            }
            form.getOggettoDetail().getNm_oggetto().setValue(bean.getNmOggetto());
            LogVLisEventoOggettoTableBean tabella = sacerLogWebEjb.getEventsByAppTipoOggettoId(
                    form.getOggettoDetail().getNmApp().getValue(),
                    form.getOggettoDetail().getNm_tipo_oggetto().getValue(),
                    form.getOggettoDetail().getIdOggetto().parse());
            form.getListaEventi().setTable(tabella);
            form.getListaEventi().getTable().setPageSize(10);
            form.getListaEventi().getTable().first();
            forwardToPublisher(Application.Publisher.GESTIONE_LOG_EVENTI);
        } else {
            getMessageBox().addWarning("Attenzione: non esistono eventi per l'oggetto "
                    + form.getOggettoDetail().getNm_tipo_oggetto().getValue());
            goBack();
        }
    }

    @Secure(action = "detail/GestioneLogEventiForm#ListaModifiche/visualizzaValoriGrandi")
    public void visualizzaValoriGrandi() throws EMFError {
        setTableName(getRequest().getParameter("table"));
        setRiga(getRequest().getParameter("riga"));
        getForm().getListaModifiche().getTable().setCurrentRowIndex(new Integer(getRiga()));
        LogVLisAsserzioniDatiRowBean riga = (LogVLisAsserzioniDatiRowBean) getForm()
                .getListaModifiche().getTable().getCurrentRow();

        if (riga.getString("fl_link_valore_tipo_1").equalsIgnoreCase("1")) {
            getForm().getValoreDetail().getDs_vecchio_valore().setHidden(false);
            getForm().getValoreDetail().getDs_nuovo_valore().setHidden(false);
            getForm().getValoreDetail().getDs_vecchio_valore().setValue(riga.getDsValoreOldCampo());
            getForm().getValoreDetail().getDs_nuovo_valore().setValue(riga.getDsValoreNewCampo());
            getForm().getValoreDetail().getCl_vecchio_valore().setHidden(true);
            getForm().getValoreDetail().getCl_nuovo_valore().setHidden(true);
        } else if (riga.getString("fl_link_valore_tipo_2").equalsIgnoreCase("1")) {
            getForm().getValoreDetail().getCl_vecchio_valore().setHidden(false);
            getForm().getValoreDetail().getCl_nuovo_valore().setHidden(false);
            getForm().getValoreDetail().getCl_vecchio_valore().setValue(riga.getBlValoreOldCampo());
            getForm().getValoreDetail().getCl_nuovo_valore().setValue(riga.getBlValoreNewCampo());
            getForm().getValoreDetail().getDs_vecchio_valore().setHidden(true);
            getForm().getValoreDetail().getDs_nuovo_valore().setHidden(true);
        }

        if (!getMessageBox().isEmpty()) {
            forwardToPublisher(getLastPublisher());
        } else {
            forwardToPublisher(Application.Publisher.DETTAGLIO_VALORI);
        }
    }

    @Secure(action = "detail/GestioneLogEventiForm#ListaModifiche/visualizzaValoriClob")
    public void visualizzaValoriClob() throws EMFError {
        visualizzaValoriGrandi();
    }

    @Secure(action = "detail/GestioneLogEventiForm#ListaEventi/visualizzaDettaglioTransazioneDaListaEventi")
    public void visualizzaDettaglioTransazioneDaListaEventi() throws EMFError {
        visualizzaDettaglioTransazioneCommon();
    }

    @Secure(action = "detail/GestioneLogEventiForm#RicercaEventiList/visualizzaDettaglioTransazione")
    public void visualizzaDettaglioTransazione() throws EMFError {
        visualizzaDettaglioTransazioneCommon();
    }

    private void visualizzaDettaglioTransazioneCommon() throws EMFError {
        setTableName(getRequest().getParameter("table"));
        setRiga(getRequest().getParameter("riga"));
        List lista = (List) getForm().getComponent(getTableName());
        BaseTableInterface tabella = lista.getTable();
        BaseRowInterface riga = tabella.getRow(Integer.parseInt(getRiga()));
        BigDecimal idTransazione = riga.getBigDecimal("id_transazione");
        LogVVisEventoPrincTxRowBean record = sacerLogWebEjb
                .getLogVVisEventoPrincTxById(idTransazione);
        if (record != null) {
            getForm().getEventoPrincipale().getId_transazione()
                    .setValue(record.getIdTransazione().toPlainString());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            getForm().getEventoPrincipale().getDt_reg_evento()
                    .setValue(df.format(record.getDtRegEvento()));
            getForm().getEventoPrincipale().getNm_tipo_evento().setValue(record.getNmTipoEvento());
            // getForm().getEventoPrincipale().getAzione_composita().setValue(record.getDsGeneratoreAzione()
            // + "/" +
            // record.getDsAzione());
            getForm().getEventoPrincipale().getAzione_composita()
                    .setValue(record.getString("azione_composita"));
            getForm().getEventoPrincipale().getNm_agente().setValue(record.getNmAgente());
            BigDecimal idEvento = riga.getBigDecimal("id_evento");
            LogVRicEventiTableBean tabella2 = sacerLogWebEjb.getOggettiByIdEvento(idEvento);
            if (tabella2 != null) {
                getForm().getOggettiEventoPrincipaleList().setTable(tabella2);
                getForm().getOggettiEventoPrincipaleList().getTable().setPageSize(10);
                getForm().getOggettiEventoPrincipaleList().getTable().first();
            }
            LogVRicEventiTableBean tabella3 = sacerLogWebEjb
                    .getEventiByIdtransazioneExcludingEvento(idTransazione, idEvento);
            if (tabella3 != null) {
                getForm().getEventiSuccessiviList().setTable(tabella3);
                getForm().getEventiSuccessiviList().getTable().setPageSize(10);
                getForm().getEventiSuccessiviList().getTable().first();
            }
            postLoad();
            forwardToPublisher(Application.Publisher.DETTAGLIO_TRANSAZIONE);
        } else {
            getMessageBox().addWarning(
                    "Attenzione: non esistono dettagli per la transazione [" + idTransazione + "]");
            goBack();
        }
    }

    @Override
    public void visualizzaFoto() throws EMFError {
        BigDecimal idOggettoEvento = null;
        idOggettoEvento = (BigDecimal) getSession().getAttribute(EVENTO_OGGETTO_PER_LOG);
        String foto = sacerLogHelper.getXmlFotoByIdEventoOggettoAsString(idOggettoEvento);
        // FORWARD verso la pagina del dettaglio xml
        getForm().getValoreXml().getCl_xml().setValue(foto);
        forwardToPublisher(Application.Publisher.DETTAGLIO_XML);
    }

    @Override
    public void scarica_xml() throws EMFError {
        BigDecimal idOggettoEvento = null;
        idOggettoEvento = (BigDecimal) getSession().getAttribute(EVENTO_OGGETTO_PER_LOG);
        String foto = sacerLogHelper.getXmlFotoByIdEventoOggettoAsString(idOggettoEvento);
        try {
            String filename = "LogEventi_Evento_"
                    + getForm().getEventoDetail().getNm_tipo_evento().parse().replace(" ", "_")
                    + "_IdTransazione_" + getForm().getEventoDetail().getId_transazione().parse();

            byte[] fotoByte = foto.getBytes(StandardCharsets.UTF_8);

            getResponse().setContentType("application/zip");
            getResponse().setHeader("Content-Disposition",
                    "attachment; filename=\"" + filename + ".xml");

            OutputStream out = getServletOutputStream();
            if (fotoByte != null) {
                out.write(fotoByte);
            }

            out.flush();
            out.close();
            freeze();

        } catch (Exception e) {
            getMessageBox().addError("Errore durante il download del file XML nel Log eventi");
            log.error("Errore durante il download del file XML nel Log eventi", e);
        }
    }

    public void loadRicercaEventi() throws EMFError {
        log.debug("**************** loadRicercaEventi ******************");
        getUser().getMenu().reset();
        getUser().getMenu().select("Menu.Logging.RicercaEventi");

        getForm().getFiltriRicercaEventi().clear();
        getForm().getRicercaEventiList().clear();
        String nmApplic = paramApplicHelper.getApplicationName().getDsValoreParamApplic();
        if (!nmApplic.equalsIgnoreCase(Constants.nmApplic.SACER_IAM.name())) {
            getSession().setAttribute(IS_APPLICAZIONE_IAM, "false");
            LogVUsrAbilOrganizTableBean usr = sacerLogWebEjb.getOrganizzazioniByApplicAndUsrId(
                    nmApplic, new BigDecimal(getUser().getIdUtente()));
            if (usr != null) {
                getForm().getFiltriRicercaEventi().getOrganizzazione()
                        .setDecodeMap(DecodeMap.Factory.newInstance(usr, "id_organiz_applic",
                                "dl_composito_organiz"));
            }
        } else {
            getSession().setAttribute(IS_APPLICAZIONE_IAM, "true");
        }
        AplVLogTiOggTableBean apl = null;
        if (nmApplic.equalsIgnoreCase(Constants.nmApplic.SACER.name())) {
            // Esclude Ambiente dalla lista dei tipi oggetto
            apl = sacerLogWebEjb.getTipiOggettoByAppName(nmApplic, "Ambiente");
        } else {
            apl = sacerLogWebEjb.getTipiOggettoByAppName(nmApplic);
        }
        if (apl != null) {
            getForm().getFiltriRicercaEventi().getTipoOggetto().setDecodeMap(
                    DecodeMap.Factory.newInstance(apl, "id_tipo_oggetto", "nm_tipo_oggetto"));
        }
        getForm().getFiltriRicercaEventi().getTipoClasseEvento()
                .setDecodeMap(getMappaOrdinalGenericEnum("tipo_classe_evento",
                        PremisEnums.TipoClasseEvento.values()));
        postLoad();
        forwardToPublisher(Application.Publisher.RICERCA_EVENTI);
    }

    @Override
    public void ricercaEventi() throws EMFError {
        getForm().getFiltriRicercaEventi().post(getRequest());
        if (getForm().getFiltriRicercaEventi().validate(getMessageBox())) {
            String nmApplic = paramApplicHelper.getApplicationName().getDsValoreParamApplic();
            String nmOggetto = getForm().getFiltriRicercaEventi().getNm_oggetto_ricerca().parse();
            BigDecimal idOrganizzazione = getForm().getFiltriRicercaEventi().getOrganizzazione()
                    .parse();
            BigDecimal idTipoOggetto = getForm().getFiltriRicercaEventi().getTipoOggetto().parse();
            String classeEvento = getForm().getFiltriRicercaEventi().getTipoClasseEvento().parse();
            Date dataDa = getForm().getFiltriRicercaEventi().getDt_rif_da().parse();
            Date dataA = getForm().getFiltriRicercaEventi().getDt_rif_a().parse();
            BigDecimal oraRifDa = getForm().getFiltriRicercaEventi().getOra_rif_da().parse();
            BigDecimal oraRifA = getForm().getFiltriRicercaEventi().getOra_rif_a().parse();
            BigDecimal minRifDa = getForm().getFiltriRicercaEventi().getMin_rif_da().parse();
            BigDecimal minRifA = getForm().getFiltriRicercaEventi().getMin_rif_a().parse();

            TypeValidator valid = new TypeValidator(getMessageBox());
            valid.isOrganizzazioneObbligatoria(nmApplic,
                    getForm().getFiltriRicercaEventi().getOrganizzazione().getName(),
                    getForm().getFiltriRicercaEventi().getOrganizzazione().getValue(),
                    getForm().getFiltriRicercaEventi().getTipoOggetto().getDecodedValue());
            Date[] date = valid.validaDate(dataDa, oraRifDa, minRifDa, dataA, oraRifA, minRifA,
                    "Data riferimento iniziale", "Data riferimento finale");
            if (getMessageBox().isEmpty()) {
                getForm().getRicercaEventiList().clear();
                LogVRicEventiTableBean tableBean = sacerLogWebEjb.getEventiByOrgTipoClasseDates(
                        nmApplic, nmOggetto, idOrganizzazione, idTipoOggetto, classeEvento,
                        dataDa != null ? date[0] : null, dataA != null ? date[1] : null,
                        paramApplicHelper.getMaxResultRicercaLogEventiValue());
                if (tableBean != null && tableBean.isEmpty() == false) {
                    getForm().getRicercaEventiList().setTable(tableBean);
                    getForm().getRicercaEventiList().getTable().setPageSize(10);
                    getForm().getRicercaEventiList().getTable().first();
                } else {
                    getMessageBox().addWarning(
                            "Attenzione: non esistono eventi per i criteri selezionati");
                }
            }
        }
        forwardToPublisher(Application.Publisher.RICERCA_EVENTI);
    }

    @Override
    protected void postLoad() {
        super.postLoad();
        if (getForm() instanceof GestioneLogEventiForm) {
            getForm().getFiltriRicercaEventi().setEditMode();
            getForm().getEventoPrincipale().setViewMode();
            getForm().getEventoPrincipale().getVisualizzaDatiCompletiTransazione().setEditMode();
        }
    }

    /*
     * questi due metodi verranno tolti quando il combogetter verra' spostato come util su spagolite
     */
    public static <T extends Enum<?>> DecodeMap getMappaOrdinalGenericEnum(String key,
            T... enumerator) {
        BaseTable bt = new BaseTable();
        DecodeMap mappa = new DecodeMap();
        for (T mod : enumerator) {
            BaseRow br = new BaseRow();
            br.setString(key, mod.name());
            bt.add(br);
        }
        mappa.populatedMap(bt, key, key);
        return mappa;
    }

    private void popolaIntestazioneDettaglioEvento(String ambiente, String ente, String struttura,
            String versatore, String oggetto, String tipoOggetto) {
        GestioneLogEventiForm form = getForm();

        form.getOggettoDetail().getNm_tipo_oggetto().setValue(tipoOggetto);

        if (ambiente == null) {
            form.getOggettoDetail().getNm_ambiente().setHidden(true);
        } else {
            form.getOggettoDetail().getNm_ambiente().setValue(ambiente);
        }
        if (versatore == null) {
            form.getOggettoDetail().getNm_versatore().setHidden(true);
        } else {
            form.getOggettoDetail().getNm_versatore().setValue(versatore);
        }
        if (ente == null) {
            form.getOggettoDetail().getNm_ente().setHidden(true);
        } else {
            form.getOggettoDetail().getNm_ente().setValue(ente);
        }
        if (struttura == null) {
            form.getOggettoDetail().getNm_strut().setHidden(true);
        } else {
            form.getOggettoDetail().getNm_strut().setValue(struttura);
        }
        form.getOggettoDetail().getNm_oggetto().setValue(oggetto);
    }

    private void popolaDettaglioEventoOggettoDaRicercaEventi(List lista) {
        int riga = lista.getTable().getCurrentRowIndex();
        LogVRicEventiRowBean row = (LogVRicEventiRowBean) lista.getTable().getRow(riga);

        popolaIntestazioneDettaglioEvento(row.getNmAmbiente(), row.getNmEnte(),
                row.getNmStruttura(), row.getNmVersatore(), row.getNmOggetto(),
                row.getNmTipoOggetto());

        Timestamp data = row.getDtRegEvento();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        getForm().getEventoDetail().getDt_reg_evento().setValue(df.format(data));
        getForm().getEventoDetail().getNm_tipo_evento().setValue(row.getNmTipoEvento());
        getForm().getEventoDetail().getCalc_azione().setValue(row.getString("azione_composita"));
        getForm().getEventoDetail().getNm_agente().setValue(row.getNmAgente());
        getForm().getEventoDetail().getId_transazione()
                .setValue(row.getIdTransazione().toPlainString());

        // Legge i ruoli che aveva l'utente che ha generato l'evento che si sta
        // visualizzando
        cercaRuoliUtente(Constants.nmApplic.SACER_IAM.name(), row.getNmApplic(), row.getNmAgente(),
                data);

        BigDecimal record = row.getIdOggettoEvento();
        LogVLisAsserzioniDatiTableBean table = sacerLogWebEjb
                .getAsserzioniDatiByIdOggettoEvento(record);
        getForm().getListaModifiche().setTable(table);
        getForm().getListaModifiche().getTable().setPageSize(10);
        getForm().getListaModifiche().getTable().first();
        getForm().getEventoDetail().getVisualizzaFoto().setEditMode();
        getForm().getEventoDetail().getVisualizzaDatiCompleti().setEditMode();
        getForm().getValoreXml().getScarica_xml().setEditMode();
    }

    @Override
    public void visualizzaDatiCompleti() throws EMFError {
        getRequest().setAttribute(MOSTRA_AGENTE, "true");
        getForm().getFiltriRicercaEventi().post(getRequest());
        forwardToPublisher(Application.Publisher.DETTAGLIO_EVENTO_OGGETTO);
    }

    @Override
    public void visualizzaDatiCompletiTransazione() throws EMFError {
        getRequest().setAttribute(MOSTRA_AGENTE, "true");
        getForm().getFiltriRicercaEventi().post(getRequest());
        forwardToPublisher(Application.Publisher.DETTAGLIO_TRANSAZIONE);
    }

    private void cercaRuoliUtente(String nmApplicIam, String nmApplic, String nmAgente,
            Date dataRiferimento) {
        // Legge i ruoli che aveva l'utente che ha generato l'evento che si sta
        // visualizzando
        java.util.List<String> ruoli = sacerLogHelper.findRuoliUtenteAllaData(nmApplicIam, nmApplic,
                nmAgente, dataRiferimento);
        if (ruoli != null && ruoli.size() > 0) {
            if (ruoli.size() == 1) {
                getForm().getEventoDetail().getCalc_ruoli().setValue(ruoli.get(0));
            } else {
                StringBuilder sb = new StringBuilder();
                for (String string : ruoli) {
                    sb.append(string).append(", ");
                }
                sb.setLength(sb.length() - 2);
                getForm().getEventoDetail().getCalc_ruoli().setValue(sb.toString());
            }
        } else {
            getForm().getEventoDetail().getCalc_ruoli().clear();
        }

    }
}
