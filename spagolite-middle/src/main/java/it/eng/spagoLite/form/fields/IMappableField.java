package it.eng.spagoLite.form.fields;

import it.eng.spagoLite.db.decodemap.DecodeMapIF;

/**
 * 
 * @author Arcella_A
 *
 */
public interface IMappableField {

    public DecodeMapIF getDecodeMap();

    public void setDecodeMap(DecodeMapIF decodeMap);

}
