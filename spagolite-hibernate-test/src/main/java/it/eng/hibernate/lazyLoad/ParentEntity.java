package it.eng.hibernate.lazyLoad;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "TEST_PARENT")
public class ParentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idTestParent;
    private ChildEntity childEntity;

    @Id
    @Column(name = "ID_TEST_PARENT")
    public long getIdTestParent() {
        return idTestParent;
    }

    public void setIdTestParent(long idTestParent) {
        this.idTestParent = idTestParent;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TEST_PARENT")
    public ChildEntity getChildEntity() {
        return childEntity;
    }

    public void setChildEntity(ChildEntity childEntity) {
        this.childEntity = childEntity;
    }

}
