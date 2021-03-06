package cn.dustlight.jobless.service.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "dustlight.jobless.zeebe")
public class ZeebeProperties {

    private String gateway;

    private boolean plaintext = true;


}
