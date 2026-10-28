<%--
 Engineering Ingegneria Informatica S.p.A.

 Copyright (C) 2023 Regione Emilia-Romagna
 <p/>
 This program is free software: you can redistribute it and/or modify it under the terms of
 the GNU Affero General Public License as published by the Free Software Foundation,
 either version 3 of the License, or (at your option) any later version.
 <p/>
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU Affero General Public License for more details.
 <p/>
 You should have received a copy of the GNU Affero General Public License along with this program.
 If not, see <https://www.gnu.org/licenses/>.
 --%>

<%@page import="it.eng.spagoCore.ConfigSingleton"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Accesso Negato" />
    <sl:body>
        <sl:header description="Accesso Negato" showHomeBtn="false"/>
        <div class="newLine "></div>

        <div id="menu">&nbsp;</div>
        <div id="content">
            <!-- Message Box -->
            <div class="messages  plainError ">
                <ul>
                    <!-- Cattura l'errore generato dalla UserNotFound del SAML user detail -->
                    <c:set var="message" value='${requestScope["javax.servlet.error.message"]}' />
                    <c:choose>
                        <c:when test="${ empty message }">
                            <li class="message  error ">Utente non autorizzato alla visualizzazione della risorsa ${requestScope.destination} richiesta. <a href="./Logout.html" title="Fai logout ">Effettua un logout</a> </li>
                        </c:when>
                        <c:otherwise>
                            <!-- Messaggio proveniente da errore provocato da SliteUserDetail -->    
                            <li class="message error"><c:out value="${ fn:substringAfter(message,':') }"/>&emsp;<a href="${pageContext.request.contextPath}/Login.html" title="Fai login ">Effettua di nuovo il login</a></li>                    
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
        
        <!--Footer-->
        <sl:footer />
    </sl:body>
</sl:html>
