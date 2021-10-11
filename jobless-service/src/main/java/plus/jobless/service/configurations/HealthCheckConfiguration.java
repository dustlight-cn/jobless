package plus.jobless.service.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.jobless.core.JobHandlerManager;
import plus.jobless.core.JobHandlerProvider;
import plus.jobless.service.health.ManagerChecker;
import plus.jobless.service.health.ProviderChecker;

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
