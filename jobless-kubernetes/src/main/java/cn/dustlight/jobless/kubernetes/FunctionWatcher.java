package cn.dustlight.jobless.kubernetes;

import com.google.common.reflect.TypeToken;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.util.Watch;
import okhttp3.Call;

import java.lang.reflect.Type;

public class FunctionWatcher extends AbstractWatcher<FunctionObject> {

    public FunctionWatcher(JobHandlerUpListener<FunctionObject> upListener, JobHandlerDownListener downListener) {
        super(upListener, downListener);
    }

    @Override
    protected Call createWatchCall() throws Exception {
        return new CustomObjectsApi()
                .listClusterCustomObjectCall("kubeless.io",
                        "v1beta1",
                        "functions",
                        "function",
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
        return new TypeToken<Watch.Response<FunctionObject>>(){}.getType();
    }
}
