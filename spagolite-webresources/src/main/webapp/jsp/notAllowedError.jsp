<%@page import="it.eng.spagoCore.configuration.ConfigSingleton"%>
<%@ include file="../../include.jsp"%>

<sl:html>
    <sl:head title="Azione non permessa" />
    <sl:body>
        <sl:header description="Azione non permessa" showHomeBtn="false"/>
        <div class="newLine "></div>

        <div id="menu">&nbsp;</div>
        <div id="content">
            <!-- Message Box -->
            <div class="messages  plainError ">
                <ul>
                    <li class="message  error ">Azione non permessa su <strong>${requestScope["javax.servlet.error.request_uri"]}</strong>. <a href="./Home.html" title="Home">Torna alla home</a> </li>
                </ul>
            </div>
        </div>
        <!--Footer-->
        <sl:footer />
    </sl:body>
</sl:html>
