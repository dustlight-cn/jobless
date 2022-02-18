package cn.dustlight.jobless.kubernetes;

import cn.dustlight.jobless.core.Utils;
import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpTriggerObject implements KubernetesObject, Accessible {

    private V1ObjectMeta metadata;
    private String apiVersion;
    private String kind;
    private HttpTriggerSpec spec;

    @Override
    public String getUrl() {
        return spec == null ?
                null :
                String.format("%s://%s/%s",
//                        spec.isTls() || StringUtils.hasText(spec.getTlsSecret()) ? "https" : "http",
                        "http",
                        spec.getHostName(),
                        Utils.getSuffix(spec.getFunctionName()));
    }

    @Override
    public String getKey() {
        return spec.getFunctionName();
    }

    @Override
    public String toString() {
        return getUrl();
    }

}
