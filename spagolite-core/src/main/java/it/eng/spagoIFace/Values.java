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

package it.eng.spagoIFace;

public final class Values {

    public static final String LOGGED_USER = "LOGGED_USER";

    public static final String MENU_STATUS = "MENU_STATUS";
    public static final int MENU_OPEN = 0;
    public static final int MENU_CLOSED = 1;
    public static final String CURRENT_ACTION = "CURRENT_ACTION";
    public static final int LIST_DEFAULT_PAGE_SIZE = 10;
    public static final String OPERATION = "operation";
    public static final String PUBLISHER_REDIRECT = "REDIRECT";
    public static final String VOID_SESSION = "VOID_SESSION";
    public static final String SESSION_ID = "SESSION_ID";
    public static final String OPERATION_SEPARATOR = "__";
    public static final int SELECT_LIST_MAX_ROW = 20;
    public static final String SUB_LIST = "TABELLA";

    private Values() {
        throw new IllegalStateException("Impossibile istanziare la classe, contiene solo costanti");
    }

}
