package it.eng.integriam.server.ws.reputente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gilioli_P
 */
public class ListaTipiDatoAbil implements Iterable<TipoDatoAbil> {

    private List<TipoDatoAbil> tipoDatoAbilList;

    public List<TipoDatoAbil> getTipoDatoAbilList() {
        if (tipoDatoAbilList == null) {
            tipoDatoAbilList = new ArrayList();
        }
        return tipoDatoAbilList;
    }

    public void setTipoDatoAbilList(List<TipoDatoAbil> tipoDatoAbilList) {
        this.tipoDatoAbilList = tipoDatoAbilList;
    }

    @Override
    public Iterator<TipoDatoAbil> iterator() {
        return tipoDatoAbilList.iterator();
    }
}