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

<!-- Pagina JSP utilizzata per gestire il logout SAML2 secondo le nuove specifiche -->
<!-- che vogliono che all'interno dell'applicazione venga fatta una chiamata POST (e non più GET) -->
<!-- a {contextPath}/saml2/logout altrimenti non scatta la generazione della richiesta SAML2 verso -->
<!-- l'IDP. In sostanza è una pagina vuota che fa automaticamente un POST all'atto del load della pagina. -->
<!-- -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="it">
    <form id="myForm" action="${pageContext.servletContext.contextPath}/logout" method="post">
        <noscript>	
        <input type="submit" value="Clicca qui per eseguire il logout."/>
        </noscript>
    </form>
    <script type="text/javascript">
        document.getElementById('myForm').submit();
    </script>
</html>
