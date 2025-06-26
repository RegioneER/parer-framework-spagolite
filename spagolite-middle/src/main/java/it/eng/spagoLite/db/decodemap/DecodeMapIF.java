/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
