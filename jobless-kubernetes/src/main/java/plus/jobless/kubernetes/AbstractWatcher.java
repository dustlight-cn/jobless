package plus.jobless.kubernetes;

import com.google.common.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Watch;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import plus.jobless.core.JobHandlerProvider;
import org.springframework.boot.ApplicationRunner;

@Getter
@Setter
public abstract class AbstractWatcher extends JobHandlerProvider<Accessible> implements ApplicationRunner, InitializingBean {

    private ApiClient client;
    private OkHttpClient httpClient;

    public AbstractWatcher(JobHandlerUpListener<Accessible> upListener, JobHandlerDownListener downListener) {
        super(upListener, downListener);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        okhttp3.Call watchCall = createWatchCall();
        try (Watch<Accessible> watch = Watch.createWatch(client,
                watchCall,
                new TypeToken<Watch.Response<Accessible>>() {
                }.getType())) {
            while (watch.hasNext()) {
                Watch.Response<Accessible> item = watch.next();
                switch (item.type) {
                    case "DELETED":
                        callDown(item.object.getKey());
                    case "MODIFIED":
                    case "ADDED":
                    default:
                        callUp(item.object.getKey(),
                                new AccessibleJobHandler(item.object, httpClient));
                }
            }
        }
    }

    protected abstract okhttp3.Call createWatchCall();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (client == null)
            client = Config.defaultClient();
        if (httpClient != null)
            client.setHttpClient(httpClient);
    }
}
