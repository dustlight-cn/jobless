package cn.dustlight.jobless.service.runner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import cn.dustlight.jobless.core.JobHandlerProvider;

@AllArgsConstructor
@Getter
public class ProviderRunner implements ApplicationRunner {

    private JobHandlerProvider<?> provider;
    private final static Log logger = LogFactory.getLog(ProviderRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info(String.format("JobHandlerProvider: [%s]", provider));
        provider.start();
    }
}
