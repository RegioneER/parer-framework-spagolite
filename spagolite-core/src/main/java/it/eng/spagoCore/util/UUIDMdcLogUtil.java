package it.eng.spagoCore.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import com.fasterxml.uuid.Generators;

public class UUIDMdcLogUtil {

    private static final String UUID_LOG_MDC = "log_uuid";

    /**
     * Generate UUID and put it on MDC Log4j
     * 
     * "Remember MDC is managed on a per thread basis and every child thread automatically inherits a copy of the mapped
     * diagnostic context from its parent. This is achieved by using InheritableThreadLocal class, which is a subclass
     * of the ThreadLocal class."
     * 
     * L'application server in determinati contesti applicativi (N servlet coinvolte) potrebbe gestire più worker (multi
     * thread) non parenti, il meccanismo di ereditarietà dell'MDC non sarebbe quindi garantito (l'UUID non è trasmesso
     * tra thread cge non sono parenti).
     */
    public static void genUuid() {
        MDC.put(UUID_LOG_MDC, Generators.randomBasedGenerator().generate().toString());
    }

    /**
     * Get UUID from MDC (if is blank - it means new thread, create a new one)
     * 
     * @return UUID
     * 
     */
    public static String getUuid() {
        if (StringUtils.isBlank(MDC.get(UUIDMdcLogUtil.UUID_LOG_MDC))) {
            genUuid();
        }
        return MDC.get(UUIDMdcLogUtil.UUID_LOG_MDC);
    }
}
