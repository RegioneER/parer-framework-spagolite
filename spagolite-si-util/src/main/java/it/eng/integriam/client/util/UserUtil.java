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

package it.eng.integriam.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import it.eng.integriam.client.ws.recauth.HelpDips;
import it.eng.integriam.client.ws.recauth.RecuperoAutorizzazioniRisposta;
import it.eng.spagoLite.FrameElement;
import it.eng.spagoLite.security.User;
import it.eng.spagoLite.security.menu.impl.Link;
import it.eng.spagoLite.security.menu.impl.Menu;
import it.eng.spagoLite.security.profile.Azione;
import it.eng.spagoLite.security.profile.MenuDips;
import it.eng.spagoLite.security.profile.Pagina;
import it.eng.spagoLite.security.profile.Profile;

public class UserUtil {

    private UserUtil() {
        throw new IllegalStateException(
                "Impossibile istanziare la classe. Contiene solo metodi statici.");
    }

    public static void fillComponenti(User utente, RecuperoAutorizzazioniRisposta auth) {
        utente.setMenu(populateMenu(auth.getMenuList()));
        Profile profilo = new Profile(utente.getUsername(), "Profilo");

        // Colleziono in una mappa l'insieme di azioni per ogni pagina
        HashMap<String, List<String>> aplMap = new HashMap<>();
        // Aggiunto io
        HashMap<String, it.eng.integriam.client.ws.recauth.Pagina> pagineMap = new HashMap<>();
        for (it.eng.integriam.client.ws.recauth.Pagina aplPagina : auth.getPagineList()) {
            aplMap.put(aplPagina.getNmPaginaWeb(), new ArrayList<String>());
            pagineMap.put(aplPagina.getNmPaginaWeb(), aplPagina);
        }
        for (it.eng.integriam.client.ws.recauth.Azione aplAzione : auth.getAzioniList()) {
            List<String> azioniList = aplMap.get(aplAzione.getNmPaginaWeb());
            azioniList.add(aplAzione.getNmAzionePagina());
            aplMap.put(aplAzione.getNmPaginaWeb(), azioniList);
        }

        for (String nmPaginaWeb : aplMap.keySet()) {
            Pagina pagina = new Pagina(nmPaginaWeb, nmPaginaWeb);
            List<String> azioniList = aplMap.get(nmPaginaWeb);
            // Aggiungo le azioni alla pagina
            for (String aplAzione : azioniList) {
                Azione azione = new Azione(aplAzione, aplAzione);
                pagina.addChild(azione);
            }
            // Aggiungo le operazioni di menu alla pagina (le azioni di menu sono valide per
            // tutte le pagine)
            for (it.eng.integriam.client.ws.recauth.Menu entryMenu : auth.getMenuList()) {
                if (entryMenu.getDsLinkEntryMenu() != null) {
                    Azione azione = new Azione(entryMenu.getNmEntryMenu(),
                            entryMenu.getDsEntryMenu());
                    pagina.addChild(azione);
                }
            }
            // AGGIUNTO DA ME
            if (pagineMap.get(nmPaginaWeb).isFlHelpPresente()) {
                pagina.setHelpAvailable(true);
            }
            if (pagineMap.get(nmPaginaWeb).getHelpDipsList() != null) {
                List<HelpDips> lista = pagineMap.get(nmPaginaWeb).getHelpDipsList();
                Iterator<HelpDips> it = lista.iterator();
                ArrayList<MenuDips> al = new ArrayList<>();
                while (it.hasNext()) {
                    HelpDips next = it.next();
                    MenuDips menuDips = new MenuDips(next.getNmEntryMenu(), "");
                    al.add(menuDips);
                }
                pagina.setMenuDips(al);
            }
            // Aggiungo la pagina al profilo
            profilo.addChild(pagina);
        }

        // Aggiungo le operazioni di menu alla pagina con nome vuoto ("") per i casi in
        // cui viene resettato il
        // lastPublisher
        Pagina pagina = new Pagina("", "");
        for (it.eng.integriam.client.ws.recauth.Menu entryMenu : auth.getMenuList()) {
            if (entryMenu.getDsLinkEntryMenu() != null) {
                Azione azione = new Azione(entryMenu.getNmEntryMenu(), entryMenu.getDsEntryMenu());
                pagina.addChild(azione);
            }
        }
        // Aggiungo la pagina al profilo
        profilo.addChild(pagina);
        utente.setProfile(profilo);
    }

    public static Menu populateMenu(List<it.eng.integriam.client.ws.recauth.Menu> userMenu) {
        Menu menu = new Menu("", "");
        List<Node> list = new ArrayList<>();
        for (it.eng.integriam.client.ws.recauth.Menu entryMenu : userMenu) {
            int level = entryMenu.getNiLivelloEntryMenu();
            Node node = new Node(entryMenu);

            if (list.size() < level) {
                list.add(null);
            }

            if (level > 1) {
                list.get(level - 2).add(node);
            }

            list.set(level - 1, node);
        }
        if (!list.isEmpty()) {
            for (Node childNode : list.get(0)) {
                populateMenu(menu, childNode);
            }
        }
        return menu;
    }

    private static void populateMenu(Menu menu, Node node) {

        if (!node.hasChild()) {
            Link childLink = new Link(node.getEntryMenu().getNmEntryMenu(),
                    node.getEntryMenu().getDsEntryMenu(), node.getEntryMenu().getDsLinkEntryMenu());
            menu.add(childLink);
        } else {
            Menu childMenu = new Menu(node.getEntryMenu().getNmEntryMenu(),
                    node.getEntryMenu().getDsEntryMenu());
            menu.add(childMenu);

            for (Node childNode : node) {
                populateMenu(childMenu, childNode);
            }
        }
    }

    private static class Node extends FrameElement implements Iterable<Node> {

        private final List<Node> child;
        private final it.eng.integriam.client.ws.recauth.Menu entryMenu;

        public Node(it.eng.integriam.client.ws.recauth.Menu entryMenu) {
            this.child = new ArrayList<>();
            this.entryMenu = entryMenu;
        }

        public it.eng.integriam.client.ws.recauth.Menu getEntryMenu() {
            return entryMenu;
        }

        public boolean hasChild() {
            return !child.isEmpty();
        }

        public void add(Node node) {
            child.add(node);
        }

        @Override
        public Iterator<Node> iterator() {
            return child.iterator();
        }

        @Override
        public Element asXml() {
            Element element = super.asXml();
            element.addAttribute("codice", entryMenu.getNmEntryMenu());

            for (Node c : this) {
                element.add(c.asXml());
            }

            return element;
        }
    }

}
