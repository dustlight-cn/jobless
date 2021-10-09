package plus.jobless.kubernetes;

import com.google.common.reflect.TypeToken;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Watch;
import okhttp3.Call;

import java.lang.reflect.Type;

public class HttpTriggerWatcher extends AbstractWatcher<HttpTriggerObject> {

    public HttpTriggerWatcher(JobHandlerUpListener<HttpTriggerObject> upListener, JobHandlerDownListener downListener) {
        super(upListener, downListener);
    }

    @Override
    protected Call createWatchCall() throws Exception {
        return new CustomObjectsApi()
                .listClusterCustomObjectCall("kubeless.io",
                        "v1beta1",
                        "httptriggers",
                        "httptrigger",
                        "",
                        "",
                        "",
                        null,
                        "",
                        null,
                        true,
                        null);
    }

    @Override
    protected Type getType() {
        return new TypeToken<Watch.Response<HttpTriggerObject>>(){}.getType();
    }
}
