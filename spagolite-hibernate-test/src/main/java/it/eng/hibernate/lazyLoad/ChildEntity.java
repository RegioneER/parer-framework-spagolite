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
