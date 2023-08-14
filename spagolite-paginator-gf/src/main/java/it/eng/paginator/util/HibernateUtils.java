/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.paginator.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 *
 * @author Manuel Bertuzzi <manuel.bertuzzi@eng.it>
 */
public abstract class HibernateUtils {
    /**
     * Converte una collection di BigDecimal in una List<Long> Utilizzato per una migrazione ad Hibernate che non
     * modifichi minimamente la logica attuale La soluzione pulita è chiaramente farsi arrivare direttamente il tipo di
     * dato corretto.
     * 
     * @param longCollection
     * 
     * @return
     */
    public static List<BigDecimal> bigDecimalListFrom(Collection<Long> longCollection) {
        if (longCollection == null) {
            return null;
        }

        if (longCollection.isEmpty()) {
            return new ArrayList<>();
        }
        return longCollection.stream().filter(Objects::nonNull).map(l -> BigDecimal.valueOf(l))
                .collect(Collectors.toList());
    }

    /**
     * Converte un Number in Long gestendo il caso di valore null. Utilizzato per una migrazione ad Hibernate che non
     * modifichi minimamente la logica attuale La soluzione pulita è chiaramente farsi arrivare direttamente il tipo di
     * dato corretto. Si utilizza questo metodo piuttosto che farlo inline col metodo longValue() perché in caso di
     * valori null darebbe un NullPointerException che prima non sarebbe stato sollevato.
     * 
     * @param number
     * 
     * @return
     */
    public static Long longFrom(Number number) {
        return number == null ? null : number.longValue();
    }

    public static boolean isCollectionOf(final Class<?> c, Collection collection) {
        return collection.stream().allMatch(o -> c.isInstance(o));
    }

    /**
     * Converte una collection di BigDecimal in una List<Long> Utilizzato per una migrazione ad Hibernate che non
     * modifichi minimamente la logica attuale La soluzione pulita è chiaramente farsi arrivare direttamente il tipo di
     * dato corretto.
     * 
     * @param collection
     * 
     * @return
     */
    public static List<Long> longListFrom(Collection<? extends BigDecimal> collection) {
        if (collection == null) {
            return null;
        }
        if (collection.isEmpty()) {
            return new ArrayList<>();
        }
        return collection.stream().filter(Objects::nonNull).map(BigDecimal::longValue).collect(Collectors.toList());
    }

    /**
     * Converte un Number in BigDecimal gestendo il caso di valore null. Utilizzato per una migrazione ad Hibernate che
     * non modifichi minimamente la logica attuale La soluzione pulita è chiaramente farsi arrivare direttamente il tipo
     * di dato corretto. Si utilizza questo metodo piuttosto che farlo inline col metodo BigDecimal.valueOf() perché in
     * caso di valori null darebbe un NullPointerException che prima non sarebbe stato sollevato.
     * 
     * @param numero
     * 
     * @return
     */
    public static BigDecimal bigDecimalFrom(Number numero) {
        if (numero == null) {
            return null;
        }
        if (numero instanceof Long) {
            return BigDecimal.valueOf(numero.longValue());
        }
        if (numero instanceof Integer) {
            return BigDecimal.valueOf(numero.intValue());
        }
        throw new IllegalArgumentException("Conversione da " + numero.getClass() + " a BigDecimal non gestita");
    }

    public static <T> T unproxyEntity(T entity) {
        if (entity instanceof HibernateProxy) {
            HibernateProxy hibernateProxy = (HibernateProxy) entity;
            LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
            return (T) initializer.getImplementation();
        } else {
            return entity;
        }
    }
}
