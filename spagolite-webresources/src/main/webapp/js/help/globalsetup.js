/** 
 * AJAX Global Setup
 */

$.ajaxSetup({
    beforeSend: function (xhr) {
        if($("meta[name='_csrf_header']").length && $("meta[name='_csrf']").length) {
            xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), $("meta[name='_csrf']").attr("content"));
        }
    }
});