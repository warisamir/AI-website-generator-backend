package com.rockhardy.lovable.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {
    @Around("@annotation(com.rockhardy.lovable.aop.TrackExecutionTime)")
    public Object trackExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {
        long start = System.nanoTime();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;
        String threadName = Thread.currentThread().getName();
        Object[] args = joinPoint.getArgs();
        log.info("""
                
                ==============================
                METHOD EXECUTION STARTED
                ==============================
                Class       : {}
                Method      : {}
                Thread      : {}
                Arguments   : {}
                Start Time  : {} ns
                ==============================
                """,
                className,
                methodName,
                threadName,
                Arrays.toString(args),
                start
        );

        try {

            Object result = joinPoint.proceed();
            long end = System.nanoTime();
            long executionNs = end - start;
            double executionMs = executionNs / 1_000_000.0;
            double executionSec = executionNs / 1_000_000_000.0;
            log.info("""
                    
                    ==============================
                    METHOD EXECUTION COMPLETED
                    ==============================
                    Method          : {}
                    Execution Time  : {} ns
                    Execution Time  : {} ms
                    Execution Time  : {} sec
                    Status          : SUCCESS
                    ==============================
                    """,
                    fullMethodName,
                    executionNs,
                    String.format("%.3f", executionMs),
                    String.format("%.6f", executionSec)
            );
            return result;
        } catch (Throwable ex) {
            long end = System.nanoTime();
            long executionNs = end - start;
            log.error("""
                    
                    ==============================
                    METHOD EXECUTION FAILED
                    ==============================
                    Method          : {}
                    Exception       : {}
                    Message         : {}
                    Execution Time  : {} ns
                    Status          : FAILED
                    ==============================
                    """,
                    fullMethodName,
                    ex.getClass().getSimpleName(),
                    ex.getMessage(),
                    executionNs,
                    ex
            );
            throw ex;
        }
    }
}