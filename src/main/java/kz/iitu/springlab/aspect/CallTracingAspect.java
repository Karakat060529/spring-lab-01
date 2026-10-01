package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(3)
public class CallTracingAspect {

    private final ThreadLocal<Integer> depth =
            ThreadLocal.withInitial(() -> 0);

    @Before("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public void before(JoinPoint jp) {

        int currentDepth = depth.get();

        String indent = "  ".repeat(currentDepth);

        System.out.println(
                "[TRACE] " + indent + "-> "
                        + jp.getSignature().toShortString()
        );

        depth.set(currentDepth + 1);
    }

    @After("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public void after(JoinPoint jp) {

        int newDepth = Math.max(0, depth.get() - 1);
        depth.set(newDepth);

        String indent = "  ".repeat(newDepth);

        System.out.println(
                "[TRACE] " + indent + "<- "
                        + jp.getSignature().toShortString()
        );

        if (newDepth == 0) {
            depth.remove();
        }
    }
}