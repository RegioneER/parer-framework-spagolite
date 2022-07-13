// MAIN
$(document).ready(function() {
  var datePicker = new CDatePicker();
  datePicker.load();

  var triggerHandler = new CTriggerHandler();
  triggerHandler.load();
  
  var windowHandler = new CWindowHandler();
  windowHandler.load();
  
  var menuHandler = new CMenuHandler();
  menuHandler.load();
  
  var buttonHandler = new CButtonHandler();
  buttonHandler.load();      
  
  var linkHandler = new CLinkHandler();
  linkHandler.load();
  
  var selectChosenHandler = new CSelectChosenHandler();
  selectChosenHandler.load();
  
  var checkBoxHandler = new CCheckBoxHandler();
  checkBoxHandler.load();
  
  var tableSectionHandler = new CTableSectionHandler();
  tableSectionHandler.load();
 
//  var validator = new CValidator();
//  validator.load();

  // Carica ed inizializza i messaggi
  new CMessages();
  
  // Se siamo in inverno cade la neve...
  //$(document).snowfall({shadow : true, round : true,  minSize: 5, maxSize:8});
  setMaxWidth();
  $( window ).bind( "resize", setMaxWidth );
  function setMaxWidth() {
	  $("div#content .list td span.longline").css( "maxWidth", ( $("div#content .list td span.longline").parent().width()) + "px" );
  }  
  
});