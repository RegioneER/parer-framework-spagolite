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

package it.eng.parer.sacerlog.ejb.util;

/**
 *
 * @author Iacolucci_M
 */
public class PremisEnums {

    public class TipoAgente {

	public static final String PERSON = "person";
	public static final String ORGANIZATION = "organization";
	public static final String SOFTWARE = "software";
	public static final String HARDWARE = "hardware";
    };

    /*
     * public class TipoOrigineAgente {
     *
     * public static final String UTENTE = "UTENTE"; public static final String COMPONENTE_SW =
     * "COMPONENTE_SW"; };
     */

    public enum TipoOrigineAgente {
	UTENTE, COMPONENTE_SW
    };

    public class TipoEvento {
	public static final String CAPTURE = "capture";
	public static final String COMPRESSION = "compression";
	public static final String CREATION = "creation";
	public static final String DEACCESSION = "deaccession";
	public static final String DECOMPRESSION = "decompression";
	public static final String DECRYPTION = "decryption";
	public static final String DELETION = "deletion";
	public static final String DIGITAL_SIGNATURE_VALIDATION = "digital signature validation";
	public static final String FIXITY_CHECK = "fixity check";
	public static final String INGESTION = "ingestion";
	public static final String MESSAGE_DIGEST = "message digest";
	public static final String CALCULATION = "calculation";
	public static final String MIGRATION = "migration";
	public static final String NORMALIZATION = "normalization";
	public static final String REPLICATION = "replication";
	public static final String VALIDATION = "validation";
	public static final String VIRUS_CHECK = "virus check";
    };

    /*
     * public class TipoClasseEvento { public static final String CANCELLAZIONE = "CANCELLAZIONE";
     * public static final String INSERIMENTO = "INSERIMENTO"; public static final String
     * INSERIMENTO_MODIFICA = "INSERIMENTO_MODIFICA"; public static final String MODIFICA =
     * "MODIFICA"; public static final String VISUALIZZAZIONE = "VISUALIZZAZIONE"; };
     */

    public enum TipoClasseEvento {
	CANCELLAZIONE, INSERIMENTO, INSERIMENTO_MODIFICA, MODIFICA, VISUALIZZAZIONE
    };

    public class TipoAgenteEvento {

	public static final String AUTHORIZER = "authorizer";
	public static final String EXECUTING_PROGRAM = "executing program";
	public static final String IMPLEMENTER = "implementer";
	public static final String VALIDATOR = "validator";
    };

    public class TipoEventoOggetto {

	public static final String SOURCE = "source";
	public static final String OUTCOME = "outcome";
    };

}
