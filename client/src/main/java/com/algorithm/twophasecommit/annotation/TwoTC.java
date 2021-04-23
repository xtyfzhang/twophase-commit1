package com.algorithm.twophasecommit.annotation;


import java.lang.annotation.*;

/**
 * 分布式事务注解,利用切面的方式进行事务解决
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TwoTC {

}
