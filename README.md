# Framework SpagoLite

Fonte template redazione documento:  https://www.makeareadme.com/.

# Descrizione

Il seguente progetto è utilizzato come **dipendenza** interna.
Lo scopo è quello di definire un vero e proprio framework basato su modello MVC (Model View Control) con il quale è quindi possibile definire le modalità di interazione client-server per l'implementazioni di applicazion Web (con front-end / UI).

# Installazione

Come già specificato nel paragrafo precedente [Descrizione](# Descrizione) si tratta di un progetto di tipo "libreria", quindi un modulo applicativo utilizzato attraverso la definzione della dipendenza Maven secondo lo standard previsto (https://maven.apache.org/): 

```xml
<dependency>
    <groupId>it.eng.parer</groupId>
    <artifactId>spagofat</artifactId>
    <version>4.11.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

# Utilizzo

Il framework Spagolite, nasce con l'idea di realizzare un set di moduli (e relative dipendenze) per "standardizzare" e "personalizzare" sviluppi legati ad applicazioni con front-end basate sul modello MVC (Model View Control) che eredita design pattern già noti da framework esistenti come Struts e Spring.
Lo scopo è quello di definire un vero e proprio framework con il quale le applicazioni web che prevedono front-end (e.g. Sacer), possono definire le modalità di interazione client-server, ereditare il set di librerie standard previsto dalla logica, le risore statiche previste per la definizione di stili, javascript, immagini e molto altro, attraverdso delle implementazioni standard con le quali si definisce l'intero processo.


## Personalizzazioni interfaccia web (e parametri applicativi)

Esiste una apposita servlet  [ConfigServlet.java](spagolite-core/src/main/java/it/eng/spagoCore/configuration/ConfigServlet.java) che permette di definire alcune configurazioni legate ad elementi personalizzabili dell'interfaccia grafica (loghi, stili, ecc.) con la quale si possono inoltre modificare parametri applicativi esistenti che sono legati a processi server-side (timeout, URI webservice, ecc.).
Di seguito viene riportata una tabella riepilogativa.

| Nome parametro| Descrizione| Valore predefinito |
|------------------------|--------------------------------------------------------------------|--------------------|
| *disableSecurity*      | Permette di disabilitare i controlli di sicurezza tra le varie pagine. | **false**   |
| *enableLazySort*       | Abilita la possibilità di ordinare le colonne delle liste.        | **true**         |
| *debugAuthorization*   | Debug sulle informazioni relative all'autorizzazione degli utenti.| **false**        |
| *logoApp_absolutePath* | Percorso assoluto del logo dell'applicazione. Se configurato sovrascrive l'utilizzo del percorso relativo. |  |
| *logoApp_relativePath* | Percorso relativo del logo dell'applicazione.   | **/img/logos/logo_app.png** |
| *logoApp_alt*          | Nome alternativo del logo dell'applicazione (se non viene trovata l'immagine).     |    |
| *logoApp_url*          | Pagina web associata al logo dell'applicazione.   |    |
| *logoApp_title*        | Title per il logo dell'applicazione. |   |
| *logo1_absolutePath*   | Percorso assoluto del "logo 1". Se configurato sovrascrive l'utilizzo del percorso relativo. |  |
| *logo1_relativePath*   | Percorso relativo del "logo 1".     | **/img/logos/logo1.png** |
| *logo1_alt*            | Nome alternativo del "logo 1" (se non viene trovata l'immagine).     |    |
| *logo1_url*            | Pagina web associata al "logo 1".   |    |
| *logo1_title*          | Title per il "logo 1". |    |
| *logo2_absolutePath*   | Percorso assoluto del "logo 2". Se configurato sovrascrive l'utilizzo del percorso relativo. |  |
| *logo2_relativePath*   | Percorso relativo del "logo 2".   | **/img/logs/logo2.png**   |
| *logo2_alt*            | Nome alternativo del "logo 2" (se non viene trovata l'immagine).  |   |
| *logo2_url*            | Pagina web associata al "logo 2".   |   |
| *logo2_title*          | Title per il "logo 2".            |    |
| *logo3_absolutePath*   | Percorso assoluto del "logo 3". Se configurato sovrascrive l'utilizzo del percorso relativo. |   |
| *logo3_relativePath*   | Percorso relativo del "logo 3".   | **/img/logos/logo3.png**    |
| *logo3_alt*            | Nome alternativo del "logo 3" (se non viene trovata l'immagine).   |  |
| *logo3_url*            | Pagina web associata al "logo 3".            |    |
| *logo3_title*          | Title per il "logo 3".  |    |
| *favicon_absolutePath* | Percorso assoluto della favicon. Se configurato sovrascrive l'utilizzo del percorso relativo.    |   |
| *favicon_relativePath* | Percorso relativo della favicon.  | **/img/logos/favicon.ico**   |
| *cssover_absolutePath* | Percorso assoluto dell'overlay sui css. Se configurato sovrascrive l'utilizzo del percorso relativo.  |   |
| *cssover_relativePath* | Percorso relativo dell'overlay sui css.    | **/css/slForms-over.css**  |
| *titolo_applicativo*   | Titolo (title html) dell'applicazione.           | **Titolo Applicazione** |
| *ambiente_deploy*      | Ambiente su cui viene effettuato il deploy dell'applicazione  |    |
| *enableHelpOnline*     | Abilita l'help online.  | **false**  |


Oltre alle risorse statiche con le quali è possibile, per ambiente, customizzare l'aspetto grafico delle singoli applicazioni, vengono introdotti alcuni parametri che riguardano le configurazioni legate ad alcuni WS Rest esposti dalle stesse, attraverso tali parametri è quindi possibile "personalizzare" alcuni valori da essi utilizzati.
Si riporta nella seguente tabelle di quali parametri è possibile modificare il valore (con "null" si intende nessun valore impostato di default):

| Nome parametro| Descrizione| Valore predefinito |
|------------------------|--------------------------------------------------------------------|--------------------|
| *ws.instanceName*     | Nome istanza.  | **minefield**  |
| *ws.upload.directory*     | Directory su cui si effettua l'upload dei file raw presenti sulla chiamata multipart/form-data.  | **/tmp**  |
| *versamentoSync.saveLogSession*     | Versamento sicrono unità documentaria: abilitazione dei log di sessione.  | **true**  |
| *versamentoSync.maxRequestSize*     |  Versamento sicrono unità documentaria: dimensione massima della richiesta multipart/form-data in byte. | **1000000000** (1 Gbyte) |
| *versamentoSync.maxFileSize*     |  Versamento sicrono unità documentaria: dimensione massima del singolo file presente su richiesta multipart/form-data in byte.  | **1000000000** (1 Gbyte) |
| *aggAllegati.saveLogSession*     |  Aggiunta documento: abilitazione dei log di sessione.  | **false**  |
| *aggAllegati.maxRequestSize*     |   Aggiunta documento: dimensione massima della richiesta multipart/form-data in byte. | **1000000000** (1 Gbyte) |
| *aggAllegati.maxFileSize*     |   Aggiunta documento: dimensione massima del singolo file presente su richiesta multipart/form-data in byte.  | **1000000000** (1 Gbyte) |
|  *profilerApp.upload.directory*     |  Applicazione profile: directory su cui si effettua l'upload dei file raw presenti sulla chiamata multipart/form-data.  | **/tmp**  |
|  *profilerApp.maxRequestSize*     | Applicazione profile:dimensione massima della richiesta multipart/form-data in byte.  | **1000000000**  (1 Gbyte) |
|  *profilerApp.maxFileSize*     | Applicazione profile: dimensione massima del singolo file presente su richiesta multipart/form-data in byte.| **10000000** (10 Mbyte)  |
|  *profilerApp.charset*     |  Applicazione profile: charset di riferimento. | **UTF-8**  |
| *recuperoSync.saveLogSession*    | Servizi di recupero:  abilitazione dei log di sessione.  | **false**  |
| *recuperoSync.maxResponseSize*    | Servizi di recupero:  dimensione massima della risposta restituida dal servizio in byte.   | **20000000** (20 Mbyte) |
| *recuperoSync.maxFileSize*     |  Servizi di recupero: dimensione massima del singolo file presente su richiesta multipart/form-data in byte. | **20000000** (20 Mbyte) |
| *loadXsdApp.upload.directory*     |  Caricamento modello XSD:  directory su cui si effettua l'upload dei file.   | **/tmp**  |
| *loadXsdApp.maxRequestSize*     |  Caricamento modello XSD:  dimensiome massima della richiesta.  | **1000000000**  (1 Gbyte) |
| *loadXsdApp.maxFileSize*     | Caricamento modello XSD:  dimensiome massima del file xsd caricato.   | **10000000** (1 Mbyte)  |
| *loadXsdApp.charset*    | Caricamento modello XSD: charset di riferimento. | **UTF-8**  |
| *moduloInformazioni.upload.maxFileSize*    | Modulo Informazioni: dimensiome massima del file caricato. | **10000000** (1 Mbyte) |
| *helpOnline.upload.maxFileSize*    | Help Online: dimensiome massima del file caricato.  | **10000000** (1 Mbyte)  |
| *variazioneAccordo.upload.maxFileSize*    |  Variazione Accordo: dimensiome massima del file caricato. | **10000000** (1 Mbyte) |
| *disciplinareTecnico.upload.maxFileSize*    |  Disciplinare Tecnico: dimensiome massima del file caricato. | **10000000** (1 Mbyte) |
| *importVersatore.upload.maxFileSize*    |  Import Versatore: dimensiome massima del file caricato. | **10000000** (1 Mbyte) |
| *parameters.csv.maxFileSize*    |  Parametri CSV: dimensiome massima del file caricato. | **10000000** (1 Mbyte) |
| *serverName.property*    |  Property di sistema per identificare il nome del server/nodo (e.s. su Jboss identificato con il valore jboss.node.name). | jboss.node.name |
| *uri.versamentoSync*    |  URI API versamento sincrono unità documentaria. |  |


Nota: alcune delle property sopra elencate sono il frutto di un censimento "globale" che non trovo però sempre applicazione, alcune di essere sono infatti da ritenersi non più utilizzate (vedi ~~non utilizzata~~).

La logica con cui vengono letti e configurati i parametri d'inizializzazione è la seguente:

1. il parametro è presente sul web.xml;
2. è presente un parametro dal nome ```applicazione-nome_parametro``` sulle proprietà di sistema;
3. è presente un parametro dal nome ```nome_parametro``` sulle proprietà di sistema;
4. viene utilizzato un valore predefinito per il parametro.

Nel caso in cui si ricada in uno dei casi sopracitati, tutti gli altri non vengono valutati.

Le risorse con un percorso assoluto non vengono referenziate sull'HTML delle pagine come risorse esterne ma vengono **incluse** nell'applicazione  a **deploy time**.
In particolare:

 * il logo dell'applicazione (*logoApp_absolutePath*) sarà disponibile all'applicazione al percorso `/img/external/logo_app.png`
 * il logo in alto a destra (*logo1_absolutePath*) sarà disponibile all'applicazione al percorso `/img/external/logo1.png`
 * il logo in basso a sinistra (*logo2_absolutePath*) sarà disponibile all'applicazione al percorso `/img/external/logo2.png`
 * il  logo in basso a destra (*logo3_absolutePath*) sarà disponibile all'applicazione al percorso `/img/external/logo3.png`
 * la favicon (*favicon_absolutePath*) sarà disponibile all'applicazione al percorso `/img/external/favicon.ico`
 * la sovrascrittura stili CSS (*cssover_absolutePath*) sarà disponibile all'applicazione al percorso `/css/external/slForms-over.css`

Tali risorse devono essere espresse come URL e possono referenziare indirizzi remoti oppure file. Ecco un esempio:
 * logo1_absolutePath: `https://parer.pages.ente.regione.emr.it/risorse-statiche/parer-svil/sacer/img/logo_sacer_small.png` 
 * logo1_absolutePath: `file:///opt/jbosseap/images/logo_sacer_small.png`

Siccome le risorse vengono caricate all'interno dell'applicazione <ins>non è necessario che siano esposte esternamente</ins>. Questo significa che anche per ambienti esposti su internet i loghi possono essere registrati su percorsi interni alle rete dell'application server.

Disposizione dei loghi all'interno della pagina:

```
,--------------------------------------.                           
|logo applicazione |          | logo 1 |
|------------------'          '--------|
|                                      |
|                                      |
|                                      |
|                                      |
|-------,                     ,--------|
|logo 2 |                     | logo 3 |  
`--------------------------------------' 
```
# Supporto

Progetto a cura di [Engineering Ingegneria Informatica S.p.A.](https://www.eng.it/).

# Contributi

Se interessati a crontribuire alla crescita del progetto potete scrivere all'indirizzo email <a href="mailto:areasviluppoparer@regione.emilia-romagna.it">areasviluppoparer@regione.emilia-romagna.it</a>.

# Autori

Proprietà intellettuale del progetto di [Regione Emilia-Romagna](https://www.regione.emilia-romagna.it/) e [Polo Archivisitico](https://poloarchivistico.regione.emilia-romagna.it/).

# Licenza

Questo progetto è rilasciato sotto licenza GNU Affero General Public License v3.0 or later ([LICENSE.txt](LICENSE.txt)).
