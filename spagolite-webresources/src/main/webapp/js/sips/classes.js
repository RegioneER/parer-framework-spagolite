/*
 * Gestione delle interazioni Ajax. 
 * 
 */

var sPath = window.location.pathname;
var sPage = sPath.substring(sPath.lastIndexOf('/') + 1);

// oggetto che imposta le interazioni Ajax
function CAjaxData(actionName) {
	this.actionName = actionName;
	this.formWalk = CAjaxDataFormWalk;
	this.bindTriggers = CAjaxDataBindTriggers;
}

function CAjaxDataFormWalk(jsonData) {
	switch (jsonData.type) {
		case "Form":
		case "Fields":
			$.each(jsonData.map, function(property, value) {
				CAjaxDataFormWalk(value);
			});
			break;
		case "Input":
		case "TextArea":
			var obj = $('#' + jsonData.name);
			showHideElement(obj, jsonData.state);
			obj.val(jsonData.value);
			obj.text(jsonData.value);
			break;
		case "ComboBox":
			var obj = $('#' + jsonData.name);
			var prevCssWidth = obj.outerWidth(true);
			showHideElement(obj, jsonData.state);
			if (obj.is('select')) {
				$.each(jsonData.map, function(index, value) {
					obj.children().remove();
					if (jsonData.withEmptyVal) {
						obj.append("<option value=\"\"></option>");
					}
					$.each(value, function(key, val) {
						var selected = "";
						key = key.replace(/_/, "");
						if (key == jsonData.value) {
							selected = " selected=\"selected\"";
						}
						obj.append("<option " + selected + " value=\"" + key + "\">" + val + "</option>");
						if (prevCssWidth) {
							obj.css({
								width: prevCssWidth
							});
						}
					});
				});
			} else {
				if (jsonData.map && jsonData.map.length > 0) {
					obj.val(jsonData.map[0]['_' + jsonData.value]);
					obj.text(jsonData.map[0]['_' + jsonData.value]);
				}
				// Elimina la propagazione della post
				return false;
			}
			break;
		case "MultiSelect":
			var obj = $('#' + jsonData.name);
			var prevCssWidth = obj.outerWidth(true);
			showHideElement(obj, jsonData.state);
			if (obj.is('select')) {
				$.each(jsonData.map, function(index, value) {
					obj.children().remove();
					obj.append("<option value=\"\"></option>");
					$.each(value, function(key, val) {
						var selected = "";
						key = key.replace(/_/, "");
						if ($.inArray(key, jsonData.values) > -1) {
							selected = " selected=\"selected\"";
						}
						obj.append("<option " + selected + " value=\"" + key + "\">" + val + "</option>");
						if (prevCssWidth !== null) {
							obj.css({
								width: prevCssWidth
							});
						}
					});
				});
				obj.trigger("chosen:updated");
			} else if (obj.is('ul')) {
				$.each(jsonData.map, function(index, value) {
					obj.children().remove();
					$.each(value, function(key, val) {
						key = key.replace(/_/, "");
						if ($.inArray(key, jsonData.values) > -1) {
							obj.append("<li>" + val + "</li>");
						}
					});
				});
			}
			break;
		case "CheckBox":
			var obj = $('#' + jsonData.name);
			showHideElement(obj, jsonData.state);
			switch (jsonData.state) {
				case "readonly":
				case "view":
					if (jsonData.value == 1) {
						obj.find('img').attr('src', "./img/checkbox-field-on.png");
					} else {
						obj.find('img').attr('src', "./img/checkbox-field-off.png");
					}
					break;
				case "edit":
					if (obj.is('input[type="checkbox"]')) {
						obj.prop('checked', jsonData.value == 1);
					}
					break;
			}
			break;
		case "Radio":
			var obj = $('#' + jsonData.name);
			showHideElement(obj, jsonData.state);
			switch (jsonData.state) {
				case "readonly":
				case "view":
					if (jsonData.value == 1) {
						obj.find('img').attr('src', "./img/radiobutton-on.png");
					} else {
						obj.find('img').attr('src', "./img/radiobutton-off.png");
					}
					break;
				case "edit":
					if (obj.is('input[type="radio"]')) {
						obj.prop('checked', jsonData.value == 1);
					}
					break;
			}
			break;
		case "Button":
			var obj = $('input[type="submit"][name*="' + jsonData.name + '" i]');
			if (obj) {
				showHideElement(obj, jsonData.state);
				if (jsonData.state === 'readonly') {
					obj.attr('disabled', true);
				} else {
					obj.removeAttr('disabled');
				}
			}
			break;
	}
}

function showHideElement(element, state) {
	var id = element.attr('id');
	var parents = element.parents('fieldset[id]');
	var isInSection = parents.length > 0;
	var isSectionOpened = false;
	if (isInSection) {
		var windowButton = $(parents[0]).find('button.windowButton');
		var image = (windowButton.find('img:visible'));
		isSectionOpened = (image.length > 0 ? image.attr("alt").indexOf("Chiudi") !== -1 : true);
	}
	if (!isInSection || isSectionOpened) {
		// Se gli elementi sono all'interno di una sezione APERTA oppure non sono dentro una sezione
		element.removeClass('displayNone');
		var label = $(element.parent().parent().find('label[for=' + id + ']'));
		label.removeClass('displayNone');
		if (state === 'hidden') {
			label.hide();
			element.parent().hide();
		} else {
			label.show();
			element.parent().show();
		}
	}
}

function CAjaxDataBindTriggers(operationName, parameterName) {
	var event = "click";
	if ($('#' + parameterName).is('select')
		|| $('#' + parameterName).is('input.date')
		|| ($('#' + parameterName).is('input') && $('#' + parameterName).attr('type') == 'checkbox')
	) {
		event = "change";
	} else if ($('#' + parameterName).is('input')) {
		event = "change";
	}
	var walkerFunction = this.formWalk;

	$('#' + parameterName).bind(event, function() {
		// var parameterValue = $('#' + parameterName).val();
		// tutti i parametri del form
		var parameters = $('#spagoLiteAppForm').serializeArray();
		var actionPage = this.form ? this.form.action : "";
		actionPage = actionPage.substring(actionPage.lastIndexOf('/') + 1);
		// check if query string exists
		if (actionPage.indexOf("?") > 0) {
			// push on parameters query string
			parameters.push($.parseParams(actionPage.split('?')[1] || ''));
			// remove query string (if exists) 
			actionPage = actionPage.substring(0, actionPage.indexOf("?"));
		}

		var height = $(document).height() / 2;
		var width = $(document).width() / 2;
		$("body").prepend("<div id='loading' >Caricamento in corso ...</div>");// add
		$('#loading').show();

		$.getJSON(((actionPage) ? actionPage : sPage) + "?operation=" + operationName, parameters, function(jsonData) {
			walkerFunction(jsonData);
			// Dopo aver ripopolato la form con i valori jsonData (chiamata a walkerFunction(jsonData)) vengono gestiti eventuali messaggi di errore
			manageJsonMessages(jsonData);
			$('#loading').remove();
		});
	});
}

/**
 * Preso in ingresso il jsonData, esso può contenere alla posizione jsonData.map[0] una stringa contenente un messaggio nei seguenti attributi:
 * jsonErrorMessage, jsonWarningMessage, jsonInfoMessage.
 * Prima di tutto vengono svuotati eventuali messaggi pregressi, successivamente se presente un messaggio, 
 * crea dinamicamente il div per la visualizzazione del contenuto del messaggio.
 * Quindi viene ridefinito l'handler (gestore) dell'Ok del popup, alla pressione del quale viene eseguita una eventuale funzione JavaScript passata
 * come nuovo attributo del jsonData (jsonData.map[0].jsonFunctionName). In caso non esista il messaggio, ed il jsonData contiene l'attributo relativo
 * alla funzione, essa viene eseguita.
 * 
 * @param {type} jsonData
 * @returns {undefined}
 */
function manageJsonMessages(jsonData) {
	var msgClass = '';
	var divElement = '';
	var esistonoMessaggi = jsonData.map[0].jsonErrorMessage || jsonData.map[0].jsonWarningMessage || jsonData.map[0].jsonInfoMessage;

	$('.errorBox').each(function() {
		$(this).remove();
	});

	$('.warnBox').each(function() {
		$(this).remove();
	});

	$('.infoBox').each(function() {
		$(this).remove();
	});

	if (jsonData.map[0].jsonErrorMessage) {
		msgClass = '.errorBox';
		divElement = '<div class=\"messages  ui-state-error errorBox \"><ul><span class=\"ui-icon ui-icon-alert\"></span>' + jsonData.map[0].jsonErrorMessage + '</ul></div>';
	} else if (jsonData.map[0].jsonWarningMessage) {
		msgClass = '.warnBox';
		divElement = '<div class=\"messages  ui-state-highlight warnBox \"><ul><span class=\"ui-icon ui-icon-alert\"></span>' + jsonData.map[0].jsonWarningMessage + '</ul></div>';
	} else if (jsonData.map[0].jsonInfoMessage) {
		msgClass = '.infoBox';
		divElement = '<div class=\"messages  ui-state-highlight infoBox \"><ul><span class=\"ui-icon ui-icon-alert\"></span>' + jsonData.map[0].jsonInfoMessage + '</ul></div>';
	}

	// Creazione div con messaggio
	if (esistonoMessaggi) {
		$("#spagoLiteAppForm").prepend(divElement);
		new CMessages();
	}

	// Gestione eventuale funzione JavaScript aggiuntiva
	if (jsonData.map[0].jsonFunctionName) {
		if (esistonoMessaggi) {
			$(msgClass).dialog("option", "buttons", {
				"Ok": function () {
					$(this).dialog("close");
					if (typeof window[jsonData.map[0].jsonFunctionName] === "function") {
						window[jsonData.map[0].jsonFunctionName]()
					} else {
						alert('Funzione JavaScript ' + jsonData.map[0].jsonFunctionName + '() inesistente!')
					}
				}
			})
		} else {
			window[jsonData.map[0].jsonFunctionName]()
		}
	}
}

/*
 * Gestione delle section/fieldset
 */
function CWindowHandlerLoad() {
	$("button.windowButton").show();
	var me = this;
	var buttons = $("button.windowButton");
	$.each(buttons, function(index, object) {
		// ATTENZIONE: i blocchi di tipo "<script>" non devono essere considerati
		// nella logica sottostante in cui vengono mostrati o nascosti elementi
		var divElements = $(this).parent().parent().siblings().not("script");
		var images = $(this).find('img');
		$(this).click(function() {
			if (divElements.is(':hidden')) {
				divElements.show('slow');
			} else {
				divElements.hide('hide');
			}
			me.changeImage(images);
			// al click chiamo il metodo di formAction per rendere permanente l
			// apertura/chiusura
			var section = $(this).parent().parent().parent();
			callLoadOpened(section.attr('id'));
		});

		// se ho dei filtri impostati visualizzo la sezione
		// TODO: aggiungere il controllo anche per radio button e combo
		var textElements = divElements.find('input[type="text"]');
		var elementOpened = $(this);
		$.each(textElements, function(index, object) {
			if ($(this).val() != null && $(this).val() != "") {
				elementOpened.addClass('isLoadOpened');
				return;
			}
		});

		// inizializza
		if ($(this).hasClass('isLoadOpened')) {
			//divElements.show();

			$.each(divElements, function() {
				if ($(this).hasClass("displayNone")) {
					$(this).hide();
				} else {
					$(this).show();
				}
			});

		} else {
			divElements.hide();
		}

	});
}
function CWindowHandlerChangeImagesVisibility(images) {
	$.each(images, function(index, obj) {
		if ($(obj).is(':hidden')) {
			$(obj).show();
		} else {
			$(obj).hide();
		}
	});
}
function CWindowHandler() {
	this.changeImage = CWindowHandlerChangeImagesVisibility;
	this.load = CWindowHandlerLoad;
}

/*
 * Gestione dei trigger
 */
function CTriggerHandlerLoad() {
	// Aggancio a tutti gli elementi di input con classe date il calendario
	$('input.date').datepicker($.datepicker.regional["it"]);

	var currentActionName = $('#action_name').val();
	// Aggancio a tutti gli elementi che la nessessitano la gestione dei trigger
	$("input:hidden").each(function(index) {
		var name = $(this).attr('name');
		// per ogni campo hidden il cui nome inizi la parola trigger
		if (name.match("^trigger(.)*")) {
			var methodName = ((name.indexOf('.') > 0) ? name.replace(".", "") : name) + "OnTriggerAjax";
			var parameterName = $(this).val();
			var ajaxData = new CAjaxData(currentActionName);
			ajaxData.bindTriggers(methodName, parameterName);
		}
	});
}
function CTriggerHandler() {
	this.load = CTriggerHandlerLoad;
}

/*
 * DatePicker
 */
function CDatePickerLoad() {
	// localizzazione DatePicker
	$.datepicker.regional['it'] = {
		changeMonth: true,
		changeYear: true,
		closeText: 'Chiudi',
		prevText: '&#x3c;Prec',
		nextText: 'Succ&#x3e;',
		currentText: 'Oggi',
		monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre',
			'Dicembre'],
		monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Nov', 'Dic'],
		dayNames: ['Domenica', 'Luned&#236', 'Marted&#236', 'Mercoled&#236', 'Gioved&#236', 'Venerd&#236', 'Sabato'],
		dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
		dayNamesMin: ['Do', 'Lu', 'Ma', 'Me', 'Gi', 'Ve', 'Sa'],
		weekHeader: 'Sm',
		dateFormat: 'dd/mm/yy',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''
	};
	$.datepicker.setDefaults($.datepicker.regional['it']);
}
function CDatePicker() {
	this.load = CDatePickerLoad;
}

/*
 * Menu Handler
 */
function CMenuHandlerLoad() {
	var OPEN = 0;
	var CLOSED = 1;
	if ($('span#menuStatus').text() == OPEN) {
		$('.menuButtonClose').hide();
	} else {
		$('.menuButtonClose').show();
		$('.menuButtonClose').css({
			'float': 'left',
			'width': '1.5em'
		});
	}

	$('.menuButtonClose').hide();
	$('#menuLateraleApri').click(function() {
		$('.menuButtonOpen').show('slow');
		$('.menuButtonClose').hide('slow');
		$('#menu').show('slow');
		$('.footer').fadeIn('slow');
		$('#content').width('82%');
	});
	$('#menuLateraleChiudi').click(function() {
		$('.menuButtonClose').css({
			'float': 'left',
			'width': '1.5em'
		});
		$('.menuButtonClose').show('slow');
		$('.menuButtonOpen').hide('slow');
		$('#menu').hide('slow');
		$('.footer').fadeOut('slow');
		setTimeout(function() {
			$('#content').width('96%');
		}, 800);
	});

	// effetto slide sui menu

	// frammento di HTML
	// <ul>
	// <li class="voce">
	// <h2>Amministrazione sistema</h2>
	// <ul class="menu" style="display: block;">
	// <li class="voce">
	// <a class="selezionato"
	// href="Amministrazione.html?operation=allineaApplicazione&cleanhistory=true">Allinea
	// componenti SACER</a>
	// </li>
	// </ul>
	// <ul class="menu" style="display: block;">
	// <li class="voce">
	// <a
	// href="Amministrazione.html?operation=loadListaConfigurazioni&cleanhistory=true">Lista
	// parametri applicazione</a>
	// </li>
	// </ul>
	// </li>
	// </ul>
	var menul = $("#menu > span > ul > li.voce > ul");
	CRestoreMenu(menul);

	$("#menu > span > ul > li.voce > h2").click(function() {

		var checkElement = $(this).next();
		if (checkElement.is('ul')) {
			if (checkElement.is(':visible')) {
				$(this).css('background-image', 'url(./img/toolBar/close.png)');
			} else {
				$(this).css('background-image', 'url(./img/toolBar/open.png)');
			}
			$(this).siblings().slideToggle('normal');

			return;
		}
	});
}
function CMenuHandler() {
	this.load = CMenuHandlerLoad;
}

function CRestoreMenu(menul) {
	menul.hide();
	menul.siblings("h2").css('background-image', 'url(./img/toolBar/close.png)')
	var selez = $("li > a.selezionato", menul);
	if (selez) {
		var uls = selez.parent().parent();
		uls.show();
		uls.siblings().show();
		uls.siblings("h2").css('background-image', 'url(./img/toolBar/open.png)')
	}
}

/*
 * Button Handler
 */
function CButtonHandlerLoad() {
	$('input[type="submit"]').click(function() {
		var res = true;
		if ($(this).hasClass('save')) {
			res = confirm('Sei sicuro di voler salvare le informazioni?');
		} else if ($(this).hasClass('cancel')) {
			res = confirm('Sei sicuro di voler annullare?');
		} else if ($(this).hasClass('delete')) {
			res = confirm('Sei sicuro di voler eliminare le informazioni?');
		}
		if (!res) {
			return false;
		}
		if (!$(this).hasClass('disableHourGlass')) {
			var height = $(document).height() / 2;
			var width = $(document).width() / 2;
			//$("body").prepend("<div id='loading' >Caricamento in corso ...</div>");// add
			//$('#loading').show();
            $("body").append("<div class='overlay'><div class='overlay__inner'><div class='overlay__content'><span class='spinner'></span></div></div></div>");
        }

	});
	
	// spinner button
	$('button.pulsantespin').click(function(){
	   	$(this).html('<i class="fa fa-refresh fa-spin fa-lg" aria-hidden="true"></i> ' + $(this).text());
	});
}
function CButtonHandler() {
	this.load = CButtonHandlerLoad;
}

/*
 * Link Handler
 */
function CLinkHandlerLoad() {
	$('table.list td  > a[href*="navigationEvent=confirmdelete"]').click(function() {
		// avoid open link
		event.preventDefault();
		// reset messagebox
		$("div.messages").remove();
		// dialog confirmDeletePage
		$('<div/>', { 'style': 'overflow: hidden; display: none;', 'id': 'confirmdiagdel' }).load($(this).attr('href')).appendTo('body');
		return false;
	});
}
function CLinkHandler() {
	this.load = CLinkHandlerLoad;
}

/* Confirm Delete */
function CDialogConfirmDeleteHandlerLoad() {
	// called by load page inside <div/> confirmdiagdel
	var confirmdel = $('[id="confirmdiagdel"]');
	if(confirmdel) {
	   confirmdel.dialog({
				title: 'Conferma cancellazione',
				width: 'auto',
				height: 'auto',
				modal : true, 
				resizable: false, 
				draggable: false,
			    closeOnEscape: false,
				position: {
				       my: 'center', 
				       at: 'center', 
				       of: window
				},
				close: function(event, ui) {
					// close and destroy dialog
			        $(this).dialog('destroy').remove();
					// go to last location (default action in case confirmDialog page is not opened)
					window.location.href = $(location).attr("href");
			    }
			});
	}
}
function CDialogConfirmDeleteHandler() {
	this.load = CDialogConfirmDeleteHandlerLoad;
}

/*
 * Checkbox Handler
 */
function CCheckBoxHandlerLoad() {
	$('table.list th.cbth').html(function(index, html) {
		return '<span style="display: block;"><input name="' + this.id + '" type="checkbox" />' + html + '</span>';
	});
	$('table.list th input[type="checkbox"]').click(function() {
		$(this).closest("table.list").find('td > input[name="' + this.name + '"]').attr('checked', this.checked);
	});
}
function CCheckBoxHandler() {
	this.load = CCheckBoxHandlerLoad;
}

/*
 * Select Chosen plugin Handler
 */
function CSelectChosenHandlerLoad() {
	$('select[multiple]').chosen({
		placeholder_text_multiple: " ",
		placeholder_text_single: " ",
		no_results_text: "Nessun risultato",
		display_selected_options: false
	});
}
function CSelectChosenHandler() {
	this.load = CSelectChosenHandlerLoad;
}

/*
 * Validator
 */
function CValidatorLoad() {

	$("input[type='text']:not(.novalidate)").change(function() {
		alert('validator');
	});

}
function CValidatorGetUrl(operationName) {
	return sPage + "?operation=" + operationName;
}
function CValidator() {
	this.load = CValidatorLoad;
	this.actionName = this.url = CValidatorGetUrl;
}

/*
 * MessageBox
 */
function CMessagesAlertBox() {
	$('.infoBox').dialog({
		autoOpen: true,
		width: 600,
		modal: false,
		resizable: false,
		dialogClass: "alertBox",
		closeOnEscape: true,
		buttons: {
			"Ok": function() {
				$(this).dialog("close");
			}
		}
	});

	$('.warnBox').dialog({
		autoOpen: true,
		width: 600,
		modal: true,
		resizable: false,
		dialogClass: "alertBox",
		closeOnEscape: true,
		buttons: {
			"Ok": function() {
				$(this).dialog("close");
			}
		}
	});

	$('.errorBox').dialog({
		autoOpen: true,
		width: 600,
		modal: true,
		resizable: false,
		dialogClass: "alertBox",
		closeOnEscape: true,
		buttons: {
			"Ok": function() {
				$(this).dialog("close");
			}
		}
	});
}
function CMessagesLoad() {
	this.alertBox();
}
function CMessages() {
	this.alertBox = CMessagesAlertBox;

	this.alertBox();
}

function CTableSectionHandler() {
	this.load = CTableSectionHandlerLoad;
}

function CTableSectionHandlerLoad() {
	var righeNascoste = $("table tr.nascondiRiga");
	CMostraRighe(righeNascoste);

	// migrate to jquery 3.1.4
	// $('table td > span.tablesection').live('click', function () {    
	$(document).on('click', 'table td > span.tablesection', function() {
		// Nasconde tutte le righe con classe nascondiRiga, poi mostra solo le sezioni 'aperte'
		$(this).parents('tr').siblings('tr.nascondiRiga').hide();
		if ($(this).hasClass('opened')) {
			$(this).parents('tr').removeClass('rigaVisibile');

			$(this).removeClass('opened');
			$(this).addClass('closed');
		} else {
			$(this).parents('tr').addClass('rigaVisibile');

			$(this).removeClass('closed');
			$(this).addClass('opened');
		}

		var righe = $(this).parents('table').find('tr:has(.opened), tr:has(.closed)');
		$(righe).each(function(index, element) {
			if ($(element).find('span.tablesection').hasClass('opened')) {
				$(element).find(' ~ tr.nascondiRiga').show();
			} else {
				$(element).find(' ~ tr.nascondiRiga').hide();
			}
		});

		// al click chiamo il metodo di formAction per rendere permanente l
		// apertura/chiusura
		callLoadOpened($(this).parent().attr("id"));
	});
}

function CMostraRighe(righeNascoste) {
	righeNascoste.hide();

	var sezioniAperte = $("table tr:has(.opened) ~ tr.nascondiRiga");
	sezioniAperte.show();
}

function getQueryParam(param) {
	var result = window.location.search.match(new RegExp("(\\?|&)" + param + "(\\[\\])?=([^&]*)"));

	return result ? result[3] : false;
}

function callLoadOpened(sectionName) {
	var query;
	var tableName = getQueryParam('table');
	var tableHidden = $("input:hidden[name='table']").val();

	if (tableName != false) {
		query = "&table=" + tableName;
	} else if (tableHidden != false) {
		query = "&table=" + tableHidden;
	} else {
		query = "";
	}

	$.ajax({
		url: sPage + "?operation=setSectionLoadOpened&nomeSezione=" + sectionName + query,
		async: true
	});
}

/* Funzione necessaria per il framework Select2 come copia modificata della funzione originale 'CAjaxDataBindTriggers' */

function newTriggerForSelect2(operationName) {
	// tutti i parametri del form
	var parameters = $('#spagoLiteAppForm').serializeArray();
	var actionPage = $('#spagoLiteAppForm').attr('action');
	actionPage = actionPage.substring(actionPage.lastIndexOf('/') + 1);
	// check if query string exists
	if (actionPage.indexOf("?") > 0) {
		// push on parameters query string
		parameters.push($.parseParams(actionPage.split('?')[1] || ''));
		// remove query string (if exists) 
		actionPage = actionPage.substring(0, actionPage.indexOf("?"));
	}
	$("body").prepend("<div id='loading' >Caricamento in corso ...</div>");// add
	$('#loading').show();
	$.getJSON(((actionPage) ? actionPage : sPage) + "?operation=" + operationName, parameters, function(jsonData) {
		CAjaxDataFormWalk(jsonData);
		// Dopo aver ripopolato la form con i valori jsonData (chiamata a walkerFunction(jsonData)) vengono gestiti eventuali messaggi di errore
		manageJsonMessages(jsonData);
		$('#loading').remove();
	});
}

