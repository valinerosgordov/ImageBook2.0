package ru.minogin.core.server.hibernate;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Aspect
@Order(value = 300)
public class DehibernateAdvice {
    @Around("@annotation(ru.minogin.core.server.hibernate.Dehibernate)")
    public Object dehibernate(ProceedingJoinPoint pjp) throws Throwable {
        return new Dehibernator().clean(pjp.proceed());
    }
}
