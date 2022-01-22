package cn.dustlight.jobless.service.health;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import cn.dustlight.jobless.core.JobHandlerManager;

@AllArgsConstructor
@Getter
public class ManagerChecker implements HealthIndicator {

    private JobHandlerManager<?> provider;

    @Override
    public Health health() {
        return (provider == null ?
                Health.down().withDetail("message", "Manager is null.") :
                (provider.isAnyClosed() ?
                        Health.down().withDetail("message", "Manager is closed.") :
                        Health.up())).build();
    }
}
