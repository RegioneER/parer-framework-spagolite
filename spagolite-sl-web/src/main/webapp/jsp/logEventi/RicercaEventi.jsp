<%@ page import="it.eng.parer.sacerlog.slite.gen.form.GestioneLogEventiForm" pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Ricerca eventi" />
    <sl:body>
        <sl:header showChangeOrganizationBtn="false" />
        <sl:menu showChangePasswordBtn="true" />
        <sl:content>
            <slf:messageBox />
            <sl:newLine skipLine="true"/>
            <sl:contentTitle title="Ricerca eventi" />
            <slf:fieldSet  borderHidden="false">
                <!-- Per tutte le app che non siano IAM inserisco l'organizzazione -->
                <c:if test="${isAppIam == 'false'}">
                    <slf:lblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.ORGANIZZAZIONE%>" colSpan="2" />
                    <sl:newLine />
                </c:if>
                <slf:lblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.TIPO_OGGETTO%>" colSpan="3" />
                <slf:lblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.TIPO_CLASSE_EVENTO%>" colSpan="3" />
                <slf:lblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.NM_OGGETTO_RICERCA%>" colSpan="3" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.DT_RIF_DA%>" colSpan="1" controlWidth="w0" />
                <slf:doubleLblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.ORA_RIF_DA%>" name2="<%=GestioneLogEventiForm.FiltriRicercaEventi.MIN_RIF_DA%>" controlWidth="w20" controlWidth2="w20" labelWidth="w5" colSpan="1" />
                <slf:lblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.DT_RIF_A%>" colSpan="1" controlWidth="w0"/>
                <slf:doubleLblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.ORA_RIF_A%>" name2="<%=GestioneLogEventiForm.FiltriRicercaEventi.MIN_RIF_A%>" controlWidth="w20" controlWidth2="w20" labelWidth="w5" colSpan="1" />
            </slf:fieldSet>
            <sl:newLine skipLine="true" />

            <sl:pulsantiera>
                <slf:lblField name="<%=GestioneLogEventiForm.FiltriRicercaEventi.RICERCA_EVENTI%>" width="w25" />
            </sl:pulsantiera>
            <sl:newLine skipLine="true"/>

            <sl:newLine skipLine="true"/>
            <!--  piazzo la lista con i risultati -->
            <slf:list name="<%=GestioneLogEventiForm.RicercaEventiList.NAME%>" />
            <slf:listNavBar  name="<%=GestioneLogEventiForm.RicercaEventiList.NAME%>" />

        </sl:content>
        <sl:footer />
    </sl:body>
</sl:html>
