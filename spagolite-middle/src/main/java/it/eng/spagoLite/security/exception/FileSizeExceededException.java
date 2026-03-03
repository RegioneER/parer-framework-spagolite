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

package it.eng.spagoLite.security.exception;

import org.apache.commons.fileupload.FileUploadException;

/**
 * Eccezione lanciata quando un file caricato supera la dimensione massima consentita.
 */
public class FileSizeExceededException extends FileUploadException {

    private static final long serialVersionUID = 1L;

    private final long actualSize;
    private final long maxSize;
    private final String fileName;

    /**
     * Costruisce una nuova eccezione con il messaggio specificato.
     *
     * @param message    messaggio di errore
     * @param actualSize dimensione effettiva del file in bytes
     * @param maxSize    dimensione massima consentita in bytes
     * @param fileName   nome del file che ha superato il limite
     */
    public FileSizeExceededException(String message, long actualSize, long maxSize,
            String fileName) {
        super(message);
        this.actualSize = actualSize;
        this.maxSize = maxSize;
        this.fileName = fileName;
    }

    /**
     * Costruisce una nuova eccezione con il messaggio e la causa specificati.
     *
     * @param message    messaggio di errore
     * @param cause      causa dell'eccezione
     * @param actualSize dimensione effettiva del file in bytes
     * @param maxSize    dimensione massima consentita in bytes
     * @param fileName   nome del file che ha superato il limite
     */
    public FileSizeExceededException(String message, Throwable cause, long actualSize, long maxSize,
            String fileName) {
        super(message, cause);
        this.actualSize = actualSize;
        this.maxSize = maxSize;
        this.fileName = fileName;
    }

    /**
     * Restituisce la dimensione effettiva del file caricato.
     *
     * @return dimensione in bytes
     */
    public long getActualSize() {
        return actualSize;
    }

    /**
     * Restituisce la dimensione massima consentita.
     *
     * @return dimensione massima in bytes
     */
    public long getMaxSize() {
        return maxSize;
    }

    /**
     * Restituisce il nome del file che ha superato il limite.
     *
     * @return nome del file
     */
    public String getFileName() {
        return fileName;
    }
}
