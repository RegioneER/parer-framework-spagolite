/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eng.integriam.server.ws.reputente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gilioli_P
 */
public class ListaIndIp implements Iterable<String> {

    private List<String> indIp;

    public List<String> getIndIp() {
        return indIp;
    }

    public void setIndIp(List<String> indIp) {
        this.indIp = indIp;
    }

    @Override
    public Iterator<String> iterator() {
        return indIp.iterator();
    }
}
