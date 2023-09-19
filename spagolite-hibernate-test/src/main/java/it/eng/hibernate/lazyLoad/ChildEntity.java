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

package it.eng.hibernate.lazyLoad;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "TEST_CHILD")
public class ChildEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_TEST_CHILD")
    private long idTestChild;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TEST_PARENT")
    private ParentEntity parentEntity;

    public long getIdTestParent() {
        return idTestChild;
    }

    public void setIdTestParent(long idTestParent) {
        this.idTestChild = idTestParent;
    }

    public ParentEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(ParentEntity parentEntity) {
        this.parentEntity = parentEntity;
    }
}
