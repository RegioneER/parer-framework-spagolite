/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.sequences.hibernate;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
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
@Table(name = "TEST_SEQUENCE_CHILD3")
@NamedQuery(name = "SequenceChild3Entity.findAll", query = "SELECT a FROM SequenceChild3Entity a order by a.id")
public class SequenceChild3Entity implements Serializable {

    private Long id;
    private String campo;
    private SequenceChild2Entity sequenceChild2Entity;

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
    @JoinColumn(name = "ID_CHILD2")
    public SequenceChild2Entity getSequenceChild2Entity() {
        return sequenceChild2Entity;
    }

    public void setSequenceChild2Entity(SequenceChild2Entity sequenceChild2Entity) {
        this.sequenceChild2Entity = sequenceChild2Entity;
    }

}
