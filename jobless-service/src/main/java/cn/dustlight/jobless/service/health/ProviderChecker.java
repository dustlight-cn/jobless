package cn.dustlight.jobless.service.health;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import cn.dustlight.jobless.core.JobHandlerProvider;

@AllArgsConstructor
@Getter
public class ProviderChecker implements HealthIndicator {

    private JobHandlerProvider<?> provider;

    @Override
    public Health health() {
        return (provider == null ?
                Health.down().withDetail("message", "Provider is null.") :
                (provider.isClosed() ?
                        Health.down().withDetail("message", "Provider is closed.") :
                        Health.up())).build();
    }
}
