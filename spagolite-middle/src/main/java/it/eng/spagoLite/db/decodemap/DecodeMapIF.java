package it.eng.spagoLite.db.decodemap;

import it.eng.spagoLite.FrameElementInterface;

import java.util.Set;

public interface DecodeMapIF extends FrameElementInterface {
    public static final String DEFAULT_ALIAS_CODICE = "codice";
    public static final String DEFAULT_ALIAS_DESCRIZIONE = "descrizione";
    public static final String DEFAULT_ALIAS_DESCRIZIONELUNGA = "descrizionelunga";
    public static final String DEFAULT_ALIAS_POSIZIONE = "posizione";

    public String getDescrizione(Object codice);

    public boolean isEmpty();

    public Set<Object> keySet();

    public boolean containsKey(Object key);

    public Object firstCodice();

}
