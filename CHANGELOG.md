
## 4.1.27 (18-07-2022)

### Bugfix: 1
- [#27444](https://parermine.regione.emilia-romagna.it/issues/27444) Correzione visualizzazione dettaglio differenze negative in Esame Consistenza Sacer

## 4.1.26 (21-06-2022)

### Novità: 1
- [#27367](https://parermine.regione.emilia-romagna.it/issues/27367) allineamento del framework 4.1.6 versione non hibernate  alla versione 4.2.6 hibernae

## 4.1.25 (24-03-2022)

### Novità: 1
- [#26717](https://parermine.regione.emilia-romagna.it/issues/26717) Aggiornamento librerie: BouncyCastle JCE/Varie

## 4.1.24

### Novità: 1
- [#26661](https://parermine.regione.emilia-romagna.it/issues/26661) aggiornamento librerie obsolete primo quadrimestre 2021

## 4.1.23

### Novità: 1
- [#26465](https://parermine.regione.emilia-romagna.it/issues/26465) Aggiunto tooltip per fornire indicazioni per compilazione campi di tipo input - textarea - checkbox

## 4.1.22

### Novità: 1
- [#26293](https://parermine.regione.emilia-romagna.it/issues/26293) Autenticazione sui web services di recupero con certificato client

## 4.1.18

### Bugfix: 1
- [#26171](https://parermine.regione.emilia-romagna.it/issues/26171) Correzione gestione proprietà di sistema relativa al livello di autenticazione SPID 

## 4.1.17 (21-10-2021)

### Bugfix: 1
- [#26101](https://parermine.regione.emilia-romagna.it/issues/26101) Introduzione livelli accesso spid

## 4.1.16

### Novità: 1
- [#26071](https://parermine.regione.emilia-romagna.it/issues/26071) Aggiornamento link di default del logo PARER

## 4.1.15 (19-07-2021)

### Bugfix: 1
- [#25473](https://parermine.regione.emilia-romagna.it/issues/25473) risoluzione lettura parametri all'interno della combo box con ricerca incrementale.

### Novità: 1
- [#25474](https://parermine.regione.emilia-romagna.it/issues/25474) Associazione utente SPID con anagrafica utenti 

## 4.1.14

### Bugfix: 1
- [#24091](https://parermine.regione.emilia-romagna.it/issues/24091) Generazione javadoc
# # Changelog

## [4.1.2] - 2020-29-04

* Implementazione delle combo con select2. 

* Introdotto filtro servlet e handler sOAP per iniettare un UUID come custom field per i log %X{log_uuid}.

## [4.1.1] - 2020-11-03

* Aggiornata versione Jstree alla 3.3.8 
* Introdotto pluging Higlightjs (gestione visualizzazione XML/Altri documenti in formati noti)


#### JStree migrazione nuova versione: problematiche riscontrate


>  Fare riferimento a : https://www.jstree.com/api/

<b>Attenzione</b> tie_selection true/false e proplematiche nella gestione degli stati di selezione delle checkbox  

Nota bene : dopo attenta analisi e sperimentazione la configurazione scelta per il plugin "checkbox" al fine di mantenere il comportamento precedente è : tie_selection = false e 
 whole_node  false, laddove sia necessaria la gestione "two_state" della precedente versione è necessario introdurre la three_state = false

* whole_node = false
   * funziona esclusivamente se tie_selection false
* tie_selection = false 
   * gli eventi di chech_node.jstree e uncheck_node.jstree vengono intercentati altrimenti no
   * la function is_checked re-implementata con una function appositamente implementata, che richiama l'apposita api https://www.jstree.com/api/#/?f=is_checked(obj)
* json_data
    * integrato, non esiste un plugin è sufficiente modificare la logica di back-end in modo da restituire un json compliant (vedi buildJSONNode su AmministrazioneRuoliAction
    * vedere inoltre su taglibrary dichiarazione "core / data / uri" per la nuova modalità di integrazione con un modello JSON (vedi core : data + url) 
* check_node 
    * serve prima eseguire il triggering dell'open_all altrimenti non riesce a checkare i nodi selezionati
* get_path 
    * necessario passare il nodo (vedi get_node) https://www.jstree.com/api/#/?f=get_path(obj%20[,%20glue,%20ids])
* metodo lock non esiste 
    * è stata creata apposita function che locka il tree (in sola lettura)
* loaded() 
    * non esiste, è necessario scatenare l'evento loaded.jstree
* populateTreee 
    * è necessario gestire l'hiding/show del tree (come elemnto all'interno del DOM) quando non sono presenti valori selezionati (vedi nome applicazione/azione)
* checkbox two_state 
    * non esiste più, si pone a "false" il three_state 
* get_checked(node) 
    * non esiste più la possibilità di ottenere i soli "figli" selezionati è stata indorotta apposita function
```javascript
function get_selected_children(node) {
    [....]
    return result;
}
```
* è stata modificata la taglibrary JTreeTag in modo tale da gestire delle proprietà legate ai plugin in uso (vedi checkbox : tree_state & altro) questo per fare in modo di pilotare determinate caratteristiche secondo il bisogno, e.g. nel caso dell'abilitazione al menu il three_state NON risulta necessario a differenza invece delle azioni (attenzione che in questo caso è necesssaria la javascript function che gestisce l'interazione in fase di editing)
* non esiste il metodo unlock quindi è necessario creare un'apposita function per rimuovere eventuali lock sul tree

> Fare rifemento a: https://highlightjs.org/

## [4.1.0] - 2020-28-02

* Aggiornato JQuery alla versione <b>3.4.1</b>.
* Aggiornamento dei plugin (basati su JQuery), tutti tranne JStree.
* Gestione delle customizzazioni css dei plugin, vedere sotto modulo webresources/src/main/webapp/custom
* Introdotta configurazione xml baseSecurity.xml su modulo webresources/src/main/resources/security da ereditare sui progetti figli di spago con la seguente notazione (nel relativo file securityContext.xml)
```xml
	<!-- common settings -->
    <import resource="classpath:security/baseSecurityContext.xml"/>
```
in pratica, attraverso l'import del file "base" si ereditano tutte le configurazioni comuni che servono per la parte di login via SSO (SAML), mentre nel progetto rimane solo da gestire quello che è interno allo stesso e non comune (vedi keystore/metadati).



