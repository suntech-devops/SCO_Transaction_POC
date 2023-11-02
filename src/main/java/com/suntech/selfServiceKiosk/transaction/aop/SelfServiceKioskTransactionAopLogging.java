package com.suntech.selfServiceKiosk.transaction.aop;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class SelfServiceKioskTransactionAopLogging {

	

		private static final Logger LOGGER = LogManager.getLogger();

		@Pointcut(value = "execution(* com.suntech.selfServiceKiosk.transaction.controller.*.*(..) )")
		public void myPointCut() {
		}

		@Around("myPointCut()")
		public Object applicationLogger(ProceedingJoinPoint joinPoint) throws Throwable {
			ObjectMapper mapper = new ObjectMapper();
			String methodName = joinPoint.getSignature().getName();
			String className = joinPoint.getTarget().getClass().toString();
			Object[] array = joinPoint.getArgs();
			LOGGER.info("before method invoke : {} :{} request payload:{}" , className, methodName , mapper.writeValueAsString(array));
			Object response = joinPoint.proceed();
			LOGGER.info("after method invoke : {} :{} response payload:{}" , className, methodName , mapper.writeValueAsString(response));

			return response;
		}
	}

