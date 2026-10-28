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
    <sl:head title="Errore generico" />
    <sl:body>
        <sl:header description="Errore generico" showHomeBtn="false"/>
        <div class="newLine "></div>

        <div id="menu">&nbsp;</div>
        <div id="content">
            <!-- Messaggio di errore (se presente) -->
            <c:set var="message" value='${requestScope["errorMessage"]}' />
            <!-- Message Box -->
            <div class="messages  plainError ">
                <ul>
                    <li class="message  error ">Si sono verificati dei problemi tecnici <strong>${requestScope["javax.servlet.error.request_uri"]}</strong>. <a href="./Home.html" title="Home">Torna alla home</a> </li>
                    <c:if test="${ not empty message }">
                        <li class="message  error ">${message}</li>
                    </c:if>
                </ul>
            </div>
        </div>
        <!--Footer-->
        <sl:footer />
    </sl:body>
</sl:html>
