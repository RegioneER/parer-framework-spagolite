/* 
 * Author: Marco Iacolucci
 */
function mostraHelpPagina(codiceMenuHelp) {
    var wWidth = $(window).width();
    var wHeight = $(window).height();
    $.getJSON('#', {
        operation: 'mostraHelpPagina',
        codiceMenuHelp: 'codiceMenuHelp'
    }).done(function (data) {
        var obj = jQuery.parseJSON(data.map[0].risposta);
        if (obj.cdEsito==='OK') {
            $('#helpDialog').html(obj.blHelp).first().focus();
        } else {
            $('#helpDialog').html(obj.dlErr).first().focus();
        }
    }).fail(function () {
        $('#helpDialog').html('Errore di comunicazione con il server.');
    });

    $('#helpDialog').dialog({
        autoOpen: true,
        width: wWidth * 0.9,
        height: wHeight * 0.9,
        modal: true,
        resizable: true,
        title: 'Help On Line',
        closeOnEscape: true,
        close: function () {
            $(this).html('');
        },
        position: {my: "center", at: "center"},
        buttons: {
            "Ok": function () {
                $(this).dialog("close");
            }
        }
    });
}

/**
 * sinatti_s
 * 
 * $.parseParams - parse query string paramaters into an object.
 */
(function($) {
	var re = /([^&=]+)=?([^&]*)/g;
	var decodeRE = /\+/g; // Regex for replacing addition symbol with a space
	var decode = function(str) {
		return decodeURIComponent(str.replace(decodeRE, " "));
	};
	$.parseParams = function(query) {
		var params = {}, e;
		while (e = re.exec(query))
			params[decode(e[1])] = decode(e[2]);
		return params;
	};
})(jQuery);

