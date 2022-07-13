<%@ page import="it.eng.parer.sacerlog.slite.gen.form.GestioneLogEventiForm" pageEncoding="UTF-8"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Dettaglio transazione" />
    <sl:body>
        <sl:header showChangeOrganizationBtn="false" />
        <sl:menu showChangePasswordBtn="true" />
        <sl:content>
            <slf:messageBox />
            <sl:newLine skipLine="true"/>
            <slf:fieldBarDetailTag name="<%=GestioneLogEventiForm.OggettoDetail.NAME%>" hideBackButton="false" />
            <sl:newLine skipLine="true"/>
            <sl:contentTitle title="Dettaglio transazione" />
            <slf:fieldSet  borderHidden="false">
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.ID_TRANSAZIONE%>" colSpan="2" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.DT_REG_EVENTO%>" colSpan="2" />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.NM_TIPO_EVENTO%>" colSpan="2" />
                <sl:newLine />
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.AZIONE_COMPOSITA%>" colSpan="2" />
                <c:if test="${mostraAgente == 'true'}">
                    <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.NM_AGENTE%>" colSpan="2" />
                </c:if>
                <sl:newLine />
            </slf:fieldSet>
            <sl:pulsantiera>
                <slf:lblField name="<%=GestioneLogEventiForm.EventoPrincipale.VISUALIZZA_DATI_COMPLETI_TRANSAZIONE%>" colSpan="2" position="left" />
            </sl:pulsantiera>
            <sl:newLine skipLine="true" />

            <sl:contentTitle title="Lista oggetti evento principale" />
            <slf:list name="<%=GestioneLogEventiForm.OggettiEventoPrincipaleList.NAME%>" />
            <slf:listNavBar  name="<%=GestioneLogEventiForm.OggettiEventoPrincipaleList.NAME%>" />
            <sl:newLine skipLine="true" />

            <sl:contentTitle title="Lista eventi successivi" />
            <slf:list name="<%=GestioneLogEventiForm.EventiSuccessiviList.NAME%>" />
            <slf:listNavBar  name="<%=GestioneLogEventiForm.EventiSuccessiviList.NAME%>" />
            <sl:newLine skipLine="true" />

        </sl:content>
        <sl:footer />
    </sl:body>
</sl:html>
