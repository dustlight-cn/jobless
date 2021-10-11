package plus.jobless.kubernetes;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import plus.jobless.core.JobHandlerProvider;
import org.springframework.boot.ApplicationRunner;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class AbstractWatcher<T extends Accessible> extends JobHandlerProvider<T> implements ApplicationRunner, InitializingBean {

    private ApiClient client;
    private OkHttpClient httpClient;

    public AbstractWatcher(JobHandlerUpListener<T> upListener, JobHandlerDownListener downListener) {
        super(upListener, downListener);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        okhttp3.Call watchCall = createWatchCall();
        try (Watch<T> watch = Watch.createWatch(client,
                watchCall,
                getType())) {
            while (watch.hasNext()) {
                Watch.Response<T> item = watch.next();
                switch (item.type) {
                    case "DELETED":
                        callDown(item.object.getKey());
                        break;
                    case "MODIFIED":
                    case "ADDED":
                    default:
                        callUp(item.object.getKey(),
                                new AccessibleJobHandler<>(item.object, httpClient));
                        break;
                }
            }
        }
    }

    protected abstract okhttp3.Call createWatchCall() throws Exception;

    protected abstract Type getType();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (client == null)
            client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        httpClient = Objects.requireNonNullElseGet(httpClient,
                () -> client.getHttpClient()
                        .newBuilder()
                        .readTimeout(0, TimeUnit.SECONDS)
                        .build());
        client.setHttpClient(httpClient);
    }
}
