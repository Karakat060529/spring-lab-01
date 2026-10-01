package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class AuditAspect {

    private static final Logger log =
            LoggerFactory.getLogger(AuditAspect.class);

    @Before("@annotation(kz.iitu.springlab.annotation.Audited)")
    public void audit(JoinPoint jp) {
        log.info("[AUDIT] method={} args={}",
                jp.getSignature().toShortString(),
                java.util.Arrays.toString(jp.getArgs()));
    }
}
