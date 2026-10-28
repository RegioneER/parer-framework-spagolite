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
<sl:head title="Informativa dei cookie" />
<sl:body>
	<sl:header showHomeBtn="true" showChangeOrganizationBtn="false"
		description="" />
	<sl:menu />
	<sl:content>
		<div id="informativa">TESTO INFORMATIVA COOKIES</div>
		<div class="pulsantiera">
			<div class="containerLeft w25">
				<input class=" pulsante " type="submit" value="Indietro"
					name="operation__elencoOnClick">
			</div>
	</sl:content>
</sl:body>
</sl:html>
