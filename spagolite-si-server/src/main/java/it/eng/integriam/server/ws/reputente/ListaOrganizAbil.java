package it.eng.integriam.server.ws.reputente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gilioli_P
 */
public class ListaOrganizAbil implements Iterable<OrganizAbil> {

    private List<OrganizAbil> organizAbilList;

    public List<OrganizAbil> getOrganizAbilList() {
        if (organizAbilList == null) {
            organizAbilList = new ArrayList();
        }
        return organizAbilList;
    }

    public void setOrganizAbilList(List<OrganizAbil> organizAbilList) {
        this.organizAbilList = organizAbilList;
    }

    @Override
    public Iterator<OrganizAbil> iterator() {
        return organizAbilList.iterator();
    }
}