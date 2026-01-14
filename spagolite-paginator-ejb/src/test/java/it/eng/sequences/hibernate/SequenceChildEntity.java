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
package it.eng.sequences.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

/**
 *
 * @author mbertuzzi
 */
@Entity
@Cacheable(true)
@Table(name = "TEST_SEQUENCE_CHILD")
@NamedQuery(name = "SequenceChildEntity.findAll", query = "SELECT a FROM SequenceChildEntity a order by a.id")
public class SequenceChildEntity implements Serializable {

    private Long id;
    private String campo;
    private SequenceEntity testSequenceEntity;
    private List<SequenceChild2Entity> child2Entitys;

    @Id
    @GenericGenerator(name = "TSEQUENCE_GENERATOR", strategy = "it.eng.sequences.hibernate.NonMonotonicSequenceGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "TSEQUENCE2"),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TSEQUENCE_GENERATOR")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CAMPO", nullable = true)
    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PARENT")
    public SequenceEntity getTestSequenceEntity() {
        return testSequenceEntity;
    }

    public void setTestSequenceEntity(SequenceEntity testSequenceEntity) {
        this.testSequenceEntity = testSequenceEntity;
    }

    @OneToMany(mappedBy = "sequenceChildEntity", cascade = {
            CascadeType.PERSIST, CascadeType.MERGE })
    public List<SequenceChild2Entity> getChild2Entitys() {
        return child2Entitys;
    }

    public void setChild2Entitys(List<SequenceChild2Entity> child2Entitys) {
        this.child2Entitys = child2Entitys;
    }
}
