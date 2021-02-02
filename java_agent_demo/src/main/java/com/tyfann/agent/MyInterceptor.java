package com.tyfann.agent;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author tyfann
 * @date 2021/1/29 4:00 下午
 */
public class MyInterceptor {

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {


        String traceId = TraceContext.traceId();
        DateTime startTime = DateUtil.parse(DateUtil.now());
        try {
            return callable.call();
        }finally {
            ActiveSpan.info("Test-Info-Msg");
            System.out.println("traceId is: "+traceId);
            System.out.println(method.getName() + ":" +
                    (DateUtil.between(startTime,DateUtil.parse(DateUtil.now()), DateUnit.MS)) + "ms");
        }

    }
}
