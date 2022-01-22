package cn.dustlight.jobless.service.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.dustlight.jobless.core.JobHandlerProvider;
import cn.dustlight.jobless.service.runner.ProviderRunner;

@Configuration
public class RunnerConfiguration {

    @Bean
    public ProviderRunner providerRunner(@Autowired JobHandlerProvider<?> provider) {
        return new ProviderRunner(provider);
    }

}
