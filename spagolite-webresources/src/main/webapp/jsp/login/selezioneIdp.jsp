<%@ page import="it.eng.spagoCore.ConfigSingleton"%>
<%@ page import="it.eng.spagoLite.spring.ParerSecurityConfiguration"%>
<%@ page import="java.util.Set" %>
<%@ include file="../../include.jsp"%>
        
<jsp:useBean id="idps" class="java.util.List" scope="request" />
<%
   pageContext.setAttribute("spidProfessionaleEnabled",System.getProperty("spid-professionale-enabled"));
%>

<sl:html>
    <sl:head title="Accedi con" /> 
     <sl:body>
       <sl:header  showHomeBtn="false"/>
        <div id="loginPage" class="center">
            <form action="" method="GET">
                <fieldset>
                    <div class="wizardTitle">
                        <span>Accedi con</span>
                        <table align="center">
                            <c:forEach items="${idps}" var="b">
                                <tr>
                                    <td align="center" >
                                        <BR/>
                                        <c:if test="${fn:contains(b.idpEntityId, '.lepida.it')}">
                                            <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                <a href="<c:url value="${b.link}" />">
                                                    <img src="<%=request.getContextPath()%>/img/EntraConSpid.png" alt="Logo" width="340px" height="85px"/>
                                                </a>
                                            </div>
                                        </c:if>
                                        <BR/>
                                        <c:if test="${spidProfessionaleEnabled == 'true'}">
                                            <c:if test="${fn:contains(b.idpEntityId, '.lepida.it')}">
                                                <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                    <a href="<c:url value="${b.link}"/>?<%=ParerSecurityConfiguration.PARAMETRO_MODALITA_SPID_PROFESSIONALE%>=<%=ParerSecurityConfiguration.MODALITA_SPID_PROFESSIONALE%>">
                                                        <img src="<%=request.getContextPath()%>/img/EntraConSpid-PX.png" alt="Logo" width="340px" height="85px"/>
                                                    </a>
                                                </div>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${not fn:contains(b.idpEntityId, '.lepida.it') and (not fn:contains(b.idpEntityId, 'localhost:')) }">
                                            <div style="border: 1px solid grey; border-radius: 5px; border-opacity: 0.6;">
                                                <a href="<c:url value="${b.link}"/>">
                                                    <img src="<%=request.getContextPath()%>/img/LogoIdp.png" alt="Logo" width="340px" height="85px"/>
                                                </a>
                                            </div>
                                        </c:if>
                                        <c:if test="${not fn:contains(b.idpEntityId, '.lepida.it') and fn:contains(b.idpEntityId, 'localhost:')}">
                                            <a href="<c:url value="${b.link}"/>">
                                                <c:out value="${b.linkDescription}"/></a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                    <div class="newLine skipLine"></div>
                </fieldset>
                <div class="newLine skipLine"></div>
                <div class="newLine skipLine"></div>
            </form>
        </div>
        <div class="newLine skipLine"></div>
        <div class="newLine skipLine"></div>
	        
        <sl:footer />
    </sl:body>
</sl:html>