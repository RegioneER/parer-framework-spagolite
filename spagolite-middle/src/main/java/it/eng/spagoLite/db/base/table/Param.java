package it.eng.spagoLite.db.base.table;

import org.springframework.util.Assert;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Param {
    private final String name;
    private final Object value;
    private final TemporalType temporalType;

    public Param(String name, Object value) {
        this(name, value, null);
    }

    public Param(String name, Object value, TemporalType temporalType) {
        Assert.notNull(name);
        this.name = name;
        this.value = value;
        this.temporalType = temporalType;
    }

    private boolean isDate() {
        return value instanceof Date;
    }

    private Date getDate() {
        if (isDate()) {
            return Date.class.cast(value);
        }
        return null;
    }

    private boolean isCalendar() {
        return value instanceof Calendar;
    }

    private Calendar getCalendar() {
        if (isCalendar()) {
            return Calendar.class.cast(value);
        }
        return null;
    }

    private boolean hasTemporalType() {
        return (isDate() || isCalendar()) && temporalType != null;
    }

    public void setParameter(Query query) {
        if (hasTemporalType()) {
            if (isCalendar()) {
                query.setParameter(name, getCalendar(), temporalType);
            }
            if (isDate()) {
                query.setParameter(name, getDate(), temporalType);
            }
        } else {
            query.setParameter(name, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Param param = (Param) o;
        return name.equals(param.name) && Objects.equals(value, param.value) && temporalType == param.temporalType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, temporalType);
    }
}
