/**
 * Highlightingjs Global Setup
 */

// on load init pre code element
hljs.initHighlightingOnLoad();
// on load init line numbers
hljs.initLineNumbersOnLoad();
// on ready add badge
$(document).ready(function() {
	// add HighlightJS-badge (options are optional)
	var options = {
		contentSelector : 'pre[class*="code-badge-pre"]',
		label : 'Copia',
		title : 'Copia',
		hasLineNumber : 'true',
		badgeClickable : 'true',
		xmlBeautifier : 'true'
	};
	window.highlightJsBadge(options);
});