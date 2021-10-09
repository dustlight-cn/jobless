package plus.jobless.kubernetes;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpTriggerSpec {

    @SerializedName("function-name")
    private String functionName;

    @SerializedName("host-name")
    private String hostName;

    private boolean tls;

    @Override
    public String toString() {
        return String.format("%s://%s/%s", tls ? "https" : "http", hostName, functionName);
    }
}
