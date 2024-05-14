package org.palpalmans.ollive_back.common.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

//콘솔창에 프린트가 아닌 로그를 띄워서 디버깅을 쉽게한다.
@Slf4j
@Aspect
@Component
public class LogAopConfig {

    private final ThreadLocal<Deque<Long>> callStack = ThreadLocal.withInitial(ArrayDeque::new);
    private final ThreadLocal<String> traceId = new ThreadLocal<>();

    @Pointcut("execution(* org.palpalmans..*.*(..)) && !within(*..*Filter)")
    private void cut() {
    }


    // Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        callStack.get().push(System.currentTimeMillis());

        int depth = callStack.get().size();

        StringBuilder logData = new StringBuilder();

        logData.append("[").append(getTraceId()).append("] ").append(getIndentation(depth))
                .append("-->").append(method.getDeclaringClass().getSimpleName())
                .append(".").append(method.getName()).append("()");

        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) continue;
            logData.append("\t").append(arg.getClass().getSimpleName()).append(": ").append(arg);
        }
        log.debug(logData.toString());
    }

    // Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);

        int depth = callStack.get().size();

        long startMs = callStack.get().pop();


        if (returnObj == null) {
            log.debug("[{}] {}()  time={}ms", getTraceId(), getIndentation(depth) + "<--" +
                            method.getDeclaringClass().getSimpleName() + "." + method.getName(),
                    System.currentTimeMillis() - startMs);
        } else {
            log.debug("[{}] {}() returned {}: {}  time={}ms", getTraceId(), getIndentation(depth) + "<--" +
                            method.getDeclaringClass().getSimpleName() + "." + method.getName(),
                    returnObj.getClass().getSimpleName(), returnObj,
                    System.currentTimeMillis() - startMs);
        }

        if (callStack.get().isEmpty()) callStack.remove();
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    // Trace ID 생성
    private String getTraceId() {
        if (traceId.get() == null) {
            traceId.set(UUID.randomUUID().toString().substring(0, 8));
        }

        return traceId.get();
    }

    // 함수 호출 depth에 따라 들여쓰기 생성
    private String getIndentation(int depth) {
        StringBuilder indentation = new StringBuilder();
        indentation.append("| ".repeat(Math.max(0, depth - 1)));
        return indentation.toString();
    }
}
