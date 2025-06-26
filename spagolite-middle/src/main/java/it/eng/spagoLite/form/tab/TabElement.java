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

package it.eng.spagoLite.form.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseComponent;

public class TabElement extends BaseComponent {

    private static final long serialVersionUID = 1L;
    private boolean current;
    private boolean hidden;
    private boolean disabled;
    private List<String> iconUrlList = new ArrayList<String>();

    public TabElement(Component parent, String name, String description) {
        this(parent, name, description, false, false, false, null);
    }

    /**
     *
     * @param parent
     *            riferimento genitore
     * @param name
     *            value
     * @param description
     *            value
     * @param current
     *            boolean true/false
     * @param hidden
     *            boolean true/false
     * @param disabled
     *            boolean true/false
     * @param iconUrls
     *            url separati da spazio
     */
    public TabElement(Component parent, String name, String description, boolean current, boolean hidden,
            boolean disabled, String iconUrls) {
        super(parent, name, description);
        this.current = current;
        this.hidden = hidden;
        this.disabled = disabled;
        setIconUrlList(iconUrls);

    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<String> getIconUrlList() {
        return iconUrlList;
    }

    public void setIconUrlList(List<String> iconUrlList) {
        this.iconUrlList = iconUrlList;
    }

    /**
     *
     * @param iconUrls
     *            {@link String} di url separati da spazio
     */
    public void setIconUrlList(String iconUrls) {
        if (iconUrls != null) {
            String[] split = iconUrls.split(" ");
            iconUrlList = Arrays.asList(split);
        } else {
            iconUrlList = null;
        }
    }

}
