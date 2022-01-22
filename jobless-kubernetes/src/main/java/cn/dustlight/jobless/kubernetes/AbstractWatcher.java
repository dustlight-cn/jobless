package cn.dustlight.jobless.kubernetes;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import cn.dustlight.jobless.core.JobHandlerProvider;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class AbstractWatcher<T extends Accessible> extends JobHandlerProvider<T> implements Runnable, InitializingBean {

    private ApiClient client;
    private OkHttpClient httpClient;
    private boolean closed;
    private ExecutorService executorService;

    public AbstractWatcher(JobHandlerUpListener<T> upListener, JobHandlerDownListener downListener) {
        super(upListener, downListener);
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        closed = false;
        try {
            okhttp3.Call watchCall = createWatchCall();
            try (Watch<T> watch = Watch.createWatch(client,
                    watchCall,
                    getType())) {
                while (watch.hasNext() && !Thread.interrupted()) {
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
        } catch (InterruptedException e) {
            // 线程中断
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closed = true;
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

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void start() {
        executorService.execute(this);
    }

    @Override
    public void stop() {
        executorService.shutdownNow();
    }
}
