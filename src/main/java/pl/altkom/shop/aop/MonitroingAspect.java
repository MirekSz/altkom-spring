package pl.altkom.shop.aop;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MonitroingAspect {
	Logger log = Logger.getLogger(MonitroingAspect.class);

	@Around("@annotation(pl.altkom.shop.aop.Monitoring)")
	public Object monitpr(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature ms = (MethodSignature) pjp.getSignature();
		Method m = ms.getMethod();
		long currentTimeMillis = System.currentTimeMillis();
		Object obj = pjp.proceed();
		long end = System.currentTimeMillis() - currentTimeMillis;
		log.info(m + " took: " + end);
		return obj;
	}

}
