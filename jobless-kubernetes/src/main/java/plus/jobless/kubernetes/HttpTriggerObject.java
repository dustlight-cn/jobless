package plus.jobless.kubernetes;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpTriggerObject implements KubernetesObject,Accessible {

    private V1ObjectMeta metadata;
    private String apiVersion;
    private String kind;
    private HttpTriggerSpec spec;

    @Override
    public String getUrl() {
        return spec.toString();
    }

    @Override
    public String getKey() {
        return spec.getFunctionName();
    }
}
