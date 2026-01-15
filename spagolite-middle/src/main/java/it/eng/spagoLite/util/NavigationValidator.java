/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */
package it.eng.spagoLite.util;

import it.eng.spagoLite.message.MessageBox;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gilioli_P
 */
public class NavigationValidator {

    private static Logger logger = LoggerFactory.getLogger(NavigationValidator.class.getName());

    MessageBox messageBox = null;

    public NavigationValidator(MessageBox messageBox) {
        this.messageBox = messageBox;
    }

    public MessageBox getMessageBox() {
        return this.messageBox;
    }

    /**
     * Verifica se una stringa rappresenta un intero valido compreso tra 0 e un valore massimo
     * specificato per la validazione del numero di pagina nella funzionalità goToPage
     *
     * @param request   contiene il valore della pagina su cui atterrare
     * @param nomeLista nome della lista
     * @param maxVal    Il valore massimo consentito (incluso).
     *
     * @return Un oggetto Integer contenente il valore numerico della stringa se la stringa è un
     *         intero valido compreso tra 0 e maxVal. Restituisce null se la stringa non è un intero
     *         valido o è fuori dal range.
     */
    public Integer getValidNavigationInteger(HttpServletRequest request, int maxVal,
            String nomeLista) {
        // Recupero il parametro in servletRequest relativo al numero di pagina
        // impostato il cui nome sarà numPag +
        // nome_lista.
        // Mi recupero un array perchè potrei avere più di una barra di scorrimento (una
        // sopra e una sotto la lista)
        String[] str = (String[]) request.getParameterValues("numPag" + nomeLista);

        String valoreNumeroPagina = "";

        // 1. Verifico di aver inserito un solo valore in caso l'array sia maggiore di 1
        if (str.length > 2) {
            getMessageBox().addError(
                    "Attenzione: sono presenti più di 2 barre di scorrimento per la stessa lista, operazione non consentita");
        }

        if (str.length == 2) {
            if (str[0].equals(str[1])) {
                valoreNumeroPagina = str[0]; // Gestisce sia il caso di entrambi vuoti sia il caso
                // di entrambi uguali
            } else if (str[1].equals("")) {
                valoreNumeroPagina = str[0];
            } else if (str[0].equals("")) {
                valoreNumeroPagina = str[1];
            } else {
                getMessageBox().addError(
                        "Attenzione: sono stati inseriti valori diversi nel campo per selezionare la pagina desiderata");
                return null;
            }
        }

        if (str.length == 1) {
            valoreNumeroPagina = str[0];
        }

        if (valoreNumeroPagina == null || valoreNumeroPagina.isEmpty()) {
            getMessageBox().addError("Numero di pagina non indicato");
            return null; // Stringa nulla o vuota non è un intero valido
        }

        try {
            // 1. Tentativo di convertire la stringa in un intero
            int num = Integer.parseInt(valoreNumeroPagina);

            // 2. Controllo del range
            if (num <= 0 || num > maxVal) {
                getMessageBox().addError("Numero di pagina al di fuori del range consentito");
                return null; // Fuori dal range consentito
            }

            // 3. Se arriviamo qui, la stringa è un intero valido nel range
            return num; // Restituisci l'Integer
        } catch (NumberFormatException e) {
            getMessageBox().addError(
                    "Attenzione: il numero fornito per selezionare la pagina desiderata non è formalmente corretto");
            // L'eccezione NumberFormatException viene lanciata se la stringa non può essere
            // convertita in un intero.
            return null; // La stringa non rappresenta un intero valido
        } catch (Exception e) {
            // Gestione di altre eccezioni impreviste (raro, ma per completezza).
            logger.error("Errore inatteso durante la validazione del numero di paginazione lista: "
                    + e.getMessage()); // Log
            // dell'errore
            return null; // Considera l'input non valido in caso di errore inatteso
        }
    }
}
