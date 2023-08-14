/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.hibernate.sequence;

import it.eng.sequences.hibernate.NonMonotonicSequenceGenerator;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author mbertuzzi
 */
@Entity
@Cacheable(true)
@Table(name = "TEST_SEQUENCE_CHILD")
@NamedQuery(name = "SequenceChildEntity.findAll", query = "SELECT a FROM SequenceChildEntity a order by a.id")
public class SequenceChildEntity implements Serializable {

    private long id;
    private String campo;
    private SequenceEntity testSequenceEntity;
    private List<SequenceChild2Entity> child2Entitys;

    @Id
    // @NonMonotonicSequenceGenerator(sequenceName = "TSEQUENCE2")

    @GeneratedValue(generator = "childGen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "childGen", sequenceName = "TSEQUENCE2")
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @OneToMany(mappedBy = "sequenceChildEntity", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    public List<SequenceChild2Entity> getChild2Entitys() {
        return child2Entitys;
    }

    public void setChild2Entitys(List<SequenceChild2Entity> child2Entitys) {
        this.child2Entitys = child2Entitys;
    }
}
