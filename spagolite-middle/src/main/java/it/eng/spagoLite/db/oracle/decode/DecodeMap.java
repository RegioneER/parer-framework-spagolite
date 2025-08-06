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

package it.eng.spagoLite.db.oracle.decode;

import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DecodeMap extends DecodeMapAbs {
    private static final long serialVersionUID = 1L;

    public DecodeMap() {
	super();
    }

    public DecodeMap(Map<Object, String> map) {
	super();
	super.map = map;
    }

    public Set<Object> keySet() {
	return map.keySet();
    }

    public boolean containsKey(Object key) {
	return map.containsKey(key);
    }

    public void populatedMap(BaseTableInterface<?> baseTableInterface, String aliasCodice,
	    String aliasDescrizione) {
	map.clear();
	for (BaseRowInterface row : baseTableInterface) {
	    map.put(row.getObject(aliasCodice), row.getString(aliasDescrizione));
	}
    }

    public void populatedMap(BaseTableInterface<?> baseTableInterface) {
	populatedMap(baseTableInterface, DEFAULT_ALIAS_CODICE, DEFAULT_ALIAS_DESCRIZIONE);
    }

    public JSONObject asJSON() {
	JSONObject jsonMap = new JSONObject();

	try {
	    if (map.keySet() != null) {
		for (Object key : map.keySet()) {
		    if (key != null) {
			String chiave = key.toString();
			if (key instanceof BigDecimal) {
			    chiave = String.valueOf(key);
			}
			// jsonMap.put(chiave, getDescrizione(key));
			// Fix per CHROME e IE9
			// http://code.google.com/p/chromium/issues/detail?id=37404,
			// aggiungo un "_" come prefisso della chiave.
			jsonMap.put("_" + chiave, getDescrizione(key));
		    }
		}
	    }
	} catch (JSONException e) {
	    // SOFFOCO l'eccezione
	}
	return jsonMap;
    }

    public static class Factory {
	// public static DecodeMap newInstance (BaseTableInterface<BaseRowInterface>
	// baseTableInterface, String
	// aliasCodice, String aliasDescrizione) {
	// DecodeMap decodeMap = new DecodeMap();
	//
	// decodeMap.populatedMap(baseTableInterface, aliasCodice, aliasDescrizione,
	// ParameterType.TYPE_STRING);
	// }
	//
	// public static DecodeMap newInstance(BaseTableInterface<BaseRowInterface>
	// baseTableInterface, String
	// aliasCodice, final String aliasDescrizione, final ParameterType parTypeValue)
	// {
	// this(getPopulatedMap(baseTableInterface, aliasCodice, new
	// SqlDefault(aliasDescrizione, parTypeValue)));
	// }

	public static DecodeMap newInstance(BaseTableInterface<?> baseTableInterface,
		String aliasCodice, String aliasDescrizione) {
	    DecodeMap decodeMap = new DecodeMap();
	    decodeMap.populatedMap(baseTableInterface, aliasCodice, aliasDescrizione);

	    return decodeMap;
	}

	public static DecodeMap newInstance(BaseTableInterface<?> baseTableInterface) {
	    DecodeMap decodeMap = new DecodeMap();
	    decodeMap.populatedMap(baseTableInterface);

	    return decodeMap;
	}
    }

}
