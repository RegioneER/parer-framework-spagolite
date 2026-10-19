/*
 * Utility di sanitizzazione centralizzata per contenuti HTML amministrativi
 * (news, note di rilascio, help online, informativa privacy) che devono
 * essere renderizzati come markup ma provengono da input utente.
 *
 * Usa DOMPurify (incluso di default da <sl:head>, vedi framework-spagolite
 * HeadTag) con una whitelist ristretta di tag/attributi di solo formattazione
 * testuale, per prevenire XSS mantenendo comunque la possibilità di
 * formattazione minima (grassetto, corsivo, interruzioni di riga, link).
 */
var SANITIZE_HTML_PREVIEW_CONFIG = {
    ALLOWED_TAGS: ["p", "br", "b", "strong", "i", "em", "u", "span", "a"],
    ALLOWED_ATTR: ["href", "target", "rel", "class"]
};

function sanitizeHtmlPreview(rawHtml) {
    if (rawHtml === null || rawHtml === undefined) {
        return "";
    }
    if (window.DOMPurify && typeof window.DOMPurify.sanitize === "function") {
        return window.DOMPurify.sanitize(String(rawHtml), SANITIZE_HTML_PREVIEW_CONFIG);
    }
    // Fallback prudenziale: se DOMPurify non e' disponibile, tratta il
    // contenuto come solo testo (nessun tag HTML viene reso).
    var div = document.createElement("div");
    div.textContent = String(rawHtml);
    return div.innerHTML;
}

/*
 * Inizializza tutte le anteprime HTML presenti nella pagina: legge il
 * contenuto grezzo (gia' HTML-encoded lato JSTL) dagli elementi sorgente
 * nascosti individuati da [data-html-source], lo decodifica, sanitizza con
 * DOMPurify e lo inietta nell'elemento di destinazione con lo stesso valore
 * di data-html-source in [data-html-preview].
 */
function initSanitizedHtmlPreviews() {
    var sources = document.querySelectorAll("[data-html-source]");
    sources.forEach(function (sourceEl) {
        var key = sourceEl.getAttribute("data-html-source");
        var targetEl = document.querySelector('[data-html-preview="' + key + '"]');
        if (!targetEl) {
            return;
        }
        // sourceEl e' una textarea/div nascosto il cui contenuto testuale
        // rappresenta il valore HTML-encoded originale: leggendolo come
        // textContent otteniamo automaticamente la decodifica delle entity.
        var rawHtml = sourceEl.tagName === "TEXTAREA" ? sourceEl.value : sourceEl.textContent;
        targetEl.innerHTML = sanitizeHtmlPreview(rawHtml);
    });
}

document.addEventListener("DOMContentLoaded", initSanitizedHtmlPreviews);
