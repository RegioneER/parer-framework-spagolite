package it.eng.spagoLite.security.exception;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.WebFault;

@WebFault(name = "AuthWSException", faultBean = "SOAPFaultInfo")
public class AuthWSException extends Exception {

    public enum CodiceErrore {
        LOGIN_FALLITO, UTENTE_NON_ATTIVO, UTENTE_SCADUTO, UTENTE_NON_AUTORIZZATO, PROBLEMA_ESTRAZIONE_APPLICAZIONE
    };

    private static final long serialVersionUID = 1L;

    private CodiceErrore codiceErrore;
    private String descrizioneErrore;

    public AuthWSException(CodiceErrore code, String msg) {
        super();
        this.codiceErrore = code;
        this.descrizioneErrore = msg;
    }

    public CodiceErrore getCodiceErrore() {
        return codiceErrore;
    }

    public void setCodiceErrore(CodiceErrore codiceErrore) {
        this.codiceErrore = codiceErrore;
    }

    public String getDescrizioneErrore() {
        return descrizioneErrore;
    }

    public void setDescrizioneErrore(String descrizioneErrore) {
        this.descrizioneErrore = descrizioneErrore;
    }

}
