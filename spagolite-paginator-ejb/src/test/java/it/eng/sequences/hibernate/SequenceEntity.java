/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.sequences.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "TEST_SEQUENCE")
@NamedQuery(name = "SequenceEntity.findAll", query = "SELECT a FROM SequenceEntity a order by a.id")
public class SequenceEntity implements Serializable {

    private Long id;
    private String note;
    private List<SequenceChildEntity> testSequenceChildEntities;

    @Id
    @GenericGenerator(name = "TSEQUENCE_GENERATOR", strategy = "it.eng.sequences.hibernate.NonMonotonicSequenceGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "TSEQUENCE"),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TSEQUENCE_GENERATOR")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NOTE", nullable = true)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @OneToMany(mappedBy = "testSequenceEntity", cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH })
    public List<SequenceChildEntity> getTestSequenceChildEntities() {
        return testSequenceChildEntities;
    }

    public void setTestSequenceChildEntities(List<SequenceChildEntity> testSequenceChildEntities) {
        this.testSequenceChildEntities = testSequenceChildEntities;
    }

}
