package cn.dustlight.jobless.service.configurations;

import cn.dustlight.jobless.kubernetes.FunctionObject;
import cn.dustlight.jobless.kubernetes.FunctionWatcher;
import cn.dustlight.jobless.kubernetes.HttpTriggerObject;
import cn.dustlight.jobless.kubernetes.HttpTriggerWatcher;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.dustlight.jobless.core.JobHandlerManager;

@Configuration
@EnableConfigurationProperties({ZeebeProperties.class, KubelessProperties.class})
public class ServiceConfiguration {

    @Bean
    public ZeebeClient zeebeClient(@Autowired ZeebeProperties properties) {
        ZeebeClientBuilder builder = ZeebeClient.newClientBuilder()
                .gatewayAddress(properties.getGateway());
        if (properties.isPlaintext())
            builder.usePlaintext();
        return builder.build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "dustlight.jobless.kubeless", name = "type", havingValue = "function", matchIfMissing = true)
    public JobHandlerManager<FunctionObject> functionObjectJobHandlerManager(@Autowired ZeebeClient client) {
        return new JobHandlerManager<>(client);
    }

    @Bean
    @ConditionalOnProperty(prefix = "dustlight.jobless.kubeless", name = "type", havingValue = "function", matchIfMissing = true)
    public FunctionWatcher functionWatcher(@Autowired JobHandlerManager<FunctionObject> jobHandlerManager) {
        return new FunctionWatcher((key, handler) -> jobHandlerManager.addHandler(key, handler),
                key -> jobHandlerManager.removeHandler(key));
    }

    @Bean
    @ConditionalOnProperty(prefix = "dustlight.jobless.kubeless", name = "type", havingValue = "http_trigger")
    public JobHandlerManager<HttpTriggerObject> httpTriggerObjectJobHandlerManager(@Autowired ZeebeClient client) {
        return new JobHandlerManager<>(client);
    }

    @Bean
    @ConditionalOnProperty(prefix = "dustlight.jobless.kubeless", name = "type", havingValue = "http_trigger")
    public HttpTriggerWatcher httpTriggerWatcher(@Autowired JobHandlerManager<HttpTriggerObject> jobHandlerManager) {
        return new HttpTriggerWatcher((key, handler) -> jobHandlerManager.addHandler(key, handler),
                key -> jobHandlerManager.removeHandler(key));
    }
}
