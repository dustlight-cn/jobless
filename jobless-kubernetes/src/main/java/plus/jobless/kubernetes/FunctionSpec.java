package plus.jobless.kubernetes;

import io.kubernetes.client.openapi.models.V1ServiceSpec;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FunctionSpec {

    private String checksum;
    private String runtime;
    private V1ServiceSpec service;
    private String timeout;

}
