package it.eng.parer.sacerlog.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Iacolucci_M
 * 
 *         Questa annotazione serve per indicare che l'ejb che si sta annotando è un ejb del pacchetto SacerLog. Serve
 *         per far funzionare correttamente il paginatore il quale quando trova questa annotazione sull'ejb che sta
 *         paginando fa la lookup dal modulo sacerlog-ejb invece che da "module" come faceva inizialmente. Quando l'ejb
 *         deve essere paginato utilizzare questa annotazione a livello di classe.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SacerLogEjbType {

}
