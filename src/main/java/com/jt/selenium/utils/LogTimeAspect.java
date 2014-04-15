package com.jt.selenium.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component  // For Spring AOP
@Aspect
public class LogTimeAspect {

	static Boolean isDebugMode = null;
	
	public static boolean isDebugMode() throws FileNotFoundException, IOException {
		if (isDebugMode == null) {
			isDebugMode = PropertiesHelper.isDebugMode();
		}
		return isDebugMode;
	}

    @Around(value = "@annotation(annotation)")
    public Object LogExecutionTime(final ProceedingJoinPoint joinPoint, final LogExecTime annotation) throws Throwable {
        final long startMillis = System.currentTimeMillis();
		if(isDebugMode()) {
	        try {
	        	String ar = "";
	        	Object[] args = joinPoint.getArgs();
	        	for (Object object : args) {
					ar+=String.format(" \"%s\" ", object);
				}
	        	System.out.print("# DEBUGMODE #: Calling "+joinPoint.getSignature() + "with"+ar);
	        } finally {
	            final long duration = System.currentTimeMillis() - startMillis;
	            System.out.println(" took " + duration + " ms");
	        }
		}
		return joinPoint.proceed();
    }
}
