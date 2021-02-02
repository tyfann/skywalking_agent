package com.tyfann.agent;

import com.tyfann.agent.MyInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.apache.skywalking.apm.agent.core.boot.AgentPackageNotFoundException;
import org.apache.skywalking.apm.agent.core.conf.SnifferConfigInitializer;
import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.agent.core.plugin.PluginBootstrap;
import org.apache.skywalking.apm.agent.core.plugin.PluginFinder;

import java.lang.instrument.Instrumentation;

/**
 * @author tyfann
 * @date 2021/1/29 3:31 下午
 */
public class SkyWalkingAgent {

    private static ILog LOGGER = LogManager.getLogger(SkyWalkingAgent.class);


    public static void premain(String agentArgs, Instrumentation inst){
        final PluginFinder pluginFinder;
        try {
            SnifferConfigInitializer.initializeCoreConfig(agentArgs);

        } catch (Exception e) {
            LogManager.getLogger(SkyWalkingAgent.class)
                    .error(e, "SkyWalking agent initialized failure. Shutting down.");
            return;
        } finally {
            LOGGER = LogManager.getLogger(SkyWalkingAgent.class);
        }

        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                return builder.method(ElementMatchers.<MethodDescription>any())
                        .intercept(MethodDelegation.to(MyInterceptor.class));
            }
        };
        new AgentBuilder.Default().type(ElementMatchers.<TypeDescription>nameStartsWith("com.tyfann.skywalking.controller"))
                .transform(transformer).installOn(inst);

//        ByteBuddy byteBuddy = (new ByteBuddy()).with(TypeValidation.of(Config.Agent.IS_OPEN_DEBUGGING_CLASS));
//
//        (new AgentBuilder.Default(byteBuddy)).ignore(
//                ElementMatchers.<TypeDescription>nameStartsWith("org.apache.skywalking.apm.dependencies.net.bytebuddy.")
//                .or(ElementMatchers.<TypeDescription>nameStartsWith("org.slf4j."))
//
//        ).type(pluginFinder.buildMatch())
//                .transform(transformer)
//                .installOn(inst);
    }
}
