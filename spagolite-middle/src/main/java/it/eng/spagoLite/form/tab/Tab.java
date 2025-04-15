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

package it.eng.spagoLite.form.tab;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseElements;

import java.util.List;

public class Tab extends BaseElements<TabElement> {
    private static final long serialVersionUID = 1L;
    private BaseElements<?> masterList;

    public Tab(Component parent, String name, String description) {
	super(parent, name, description);
	masterList = null;
    }

    public TabElement getCurrentTab() {
	List<TabElement> list = getComponentList();

	if (list.size() == 0) {
	    return null;
	}
	for (TabElement tabElement : list) {
	    if (tabElement.isCurrent()) {
		return tabElement;
	    }
	}

	return list.get(0);
    }

    public void setCurrentTab(TabElement currentTabElement) {
	for (TabElement tabElement : this) {
	    if (currentTabElement == tabElement) {
		tabElement.setCurrent(true);
	    } else {
		tabElement.setCurrent(false);
	    }
	}
    }

    public void showAll() {
	for (TabElement tabElement : this) {
	    tabElement.setHidden(false);
	}
    }

    public void hideAll() {
	for (TabElement tabElement : this) {
	    tabElement.setHidden(true);
	}
    }

    public void disableAll() {
	for (TabElement tabElement : this) {
	    tabElement.setDisabled(true);
	}
    }

    public void enableAll() {
	for (TabElement tabElement : this) {
	    tabElement.setDisabled(false);
	}
    }

    public BaseElements<?> getMasterList() {
	return masterList;
    }

    public void setMasterList(it.eng.spagoLite.form.list.List<?> masterList) {
	this.masterList = masterList;
    }

    public void setMasterList(it.eng.spagoLite.form.list.NestedList<?> masterList) {
	this.masterList = masterList;
    }

}
