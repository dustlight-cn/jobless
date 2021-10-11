package plus.jobless.service.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.jobless.core.JobHandlerProvider;
import plus.jobless.service.runner.ProviderRunner;

@Configuration
public class RunnerConfiguration {

    @Bean
    public ProviderRunner providerRunner(@Autowired JobHandlerProvider<?> provider) {
        return new ProviderRunner(provider);
    }

}
