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

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.spagoLite.message;

import java.time.LocalTime;

/**
 *
 * @author MIacolucci
 */
public class MessageUtil {

    private MessageUtil() {
        //
    }

    public static String getSalutoPerOrario() {
        String ritorno = "";
        LocalTime adesso = LocalTime.now();
        LocalTime pomeriggio = LocalTime.parse("12:59:59");
        LocalTime sera = LocalTime.parse("16:59:59");
        if (adesso.isAfter(pomeriggio) && adesso.isBefore(sera)) {
            ritorno = "Buon pomeriggio";
        } else if (adesso.isAfter(sera)) {
            ritorno = "Buonasera";
        } else if (adesso.isBefore(pomeriggio)) {
            ritorno = "Buongiorno";
        }
        return ritorno;
    }
}
