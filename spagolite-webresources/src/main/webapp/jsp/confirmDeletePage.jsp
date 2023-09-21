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
