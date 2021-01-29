import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * @author tyfann
 * @date 2021/1/29 3:31 下午
 */
public class PreMainAgent {

//    public static void premain(String agentparam, Instrumentation inst){
//        System.out.println("premain执行1");
//        System.out.println(agentparam);
//    }

    public static void premain(String agentparam){
        System.out.println("premain执行2");
        System.out.println(agentparam);
    }

    public static void premain(String agentparam, Instrumentation inst){
        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                return builder.method(ElementMatchers.<MethodDescription>any())
                        .intercept(MethodDelegation.to(MyInterceptor.class));
            }
        };
        new AgentBuilder.Default().type(ElementMatchers.<TypeDescription>nameStartsWith("com.agent"))
                .transform(transformer).installOn(inst);
    }
}
