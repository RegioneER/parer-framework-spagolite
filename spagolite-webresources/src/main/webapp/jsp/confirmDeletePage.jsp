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

<%@ include file="../include.jsp"%>
<sl:html>
    <sl:head title="Conferma cancellazione" excludeStdJs="true">
        <style>
            /*disable close cross dialog button*/
            .no-close .ui-dialog-titlebar-close {
                display: none;
            }
        </style>
        <!-- import custom js -->
        <script type="text/javascript" src="<c:url value='/js/sips/classes.js'/>" ></script>
        <script type="text/javascript" src="<c:url value='/js/sips/main.js'/>" ></script>

        <script>
            $(document).ready(function () {
                /*div render size*/
                $('[id="confirmdiagdel"]').dialog({
                    dialogClass: 'no-close',
                    width: 400,
                    height: 150,
                    position: {
                        my: 'center',
                        at: 'center',
                        of: window
                    }
                });

                $("form").on("submit", function () {
                    /* Bisogna disabilitare il tasto non in maniera sincrona ma asincrona per
                     * permettere alla submit di partire e immediatamente dopo disabilitare il tasto
                     * senno' la submit si blocca!
                     */
                    setTimeout(() => {
                        $('button[name="operation__confirmDelete"]').prop("disabled", true);
                      }, 0);
                });
                
            });
        </script>
    </sl:head>
    <sl:body>
        <noscript>
        <sl:header changeOrganizationBtnDescription="Cambia struttura" />
        <sl:menu />
        </noscript>
        <sl:content>
            <slf:messageBox />
            <slf:fieldSet>
                <h2>Conferma operazione di cancellazione</h2>
                <div class="pulsantiera">
                    <div class="containerLeft w50">
                        <button class="pulsantespin" type="submit" value="Conferma" name="operation__confirmDelete">Conferma</button>
                        <button class="pulsantespin" type="submit" value="Annulla" name="operation__cancelDelete">Annulla</button>
                    </div>
                </div>

                <input type="hidden" name="table" value="${requestScope.table}" />
                <input type="hidden" name="forceReload" value="${requestScope.forceReload}" />
                <input type="hidden" name="riga" value="${requestScope.riga}" />
                <input type="hidden" name="navigationEvent" value="delete" />

            </slf:fieldSet>
        </sl:content>
        <noscript>
        <sl:footer />
        </noscript>
    </sl:body>

</sl:html>
