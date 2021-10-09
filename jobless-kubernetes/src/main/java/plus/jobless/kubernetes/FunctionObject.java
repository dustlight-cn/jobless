package plus.jobless.kubernetes;

import io.kubernetes.client.common.KubernetesObject;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1ServicePort;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FunctionObject implements KubernetesObject, Accessible {

    public static final String DEFAULT_NAMESPACE = "default";
    private V1ObjectMeta metadata;
    private String apiVersion;
    private String kind;
    private FunctionSpec spec;

    @Override
    public String getUrl() {
        String namespace = metadata.getNamespace();
        if (namespace == null || namespace.trim().length() == 0)
            namespace = DEFAULT_NAMESPACE;
        int port = 80;
        if (spec.getService() != null) {
            List<V1ServicePort> ports = spec.getService().getPorts();
            if (ports != null && ports.size() > 0)
                port = ports.get(0).getPort();
        }
        return String.format("http://%s.%s:%d/",
                metadata.getName(),
                namespace,
                port);
    }

    @Override
    public String getKey() {
        return metadata.getName();
    }
}
