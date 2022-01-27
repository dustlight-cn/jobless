package cn.dustlight.jobless.service.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "dustlight.jobless.kubeless")
public class KubelessProperties {

    private Type type = Type.HTTP_TRIGGER;

    public enum Type {
        FUNCTION,
        HTTP_TRIGGER
    }
}
