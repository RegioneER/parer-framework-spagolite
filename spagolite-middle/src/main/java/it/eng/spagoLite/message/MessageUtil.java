/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoLite.message;

import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author MIacolucci
 */
public class MessageUtil {

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
