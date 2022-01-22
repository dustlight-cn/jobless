package cn.dustlight.jobless.service.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.dustlight.jobless.core.JobHandlerManager;
import cn.dustlight.jobless.core.JobHandlerProvider;
import cn.dustlight.jobless.service.health.ManagerChecker;
import cn.dustlight.jobless.service.health.ProviderChecker;

@Configuration
public class HealthCheckConfiguration {

    @Bean
    public ManagerChecker managerChecker(@Autowired JobHandlerManager<?> manager) {
        return new ManagerChecker(manager);
    }

    @Bean
    public ProviderChecker providerChecker(@Autowired JobHandlerProvider<?> provider) {
        return new ProviderChecker(provider);
    }
}
