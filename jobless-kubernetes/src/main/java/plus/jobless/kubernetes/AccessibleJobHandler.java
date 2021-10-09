package plus.jobless.kubernetes;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import plus.jobless.core.AbstractJobHandler;

@Getter
@Setter
public class AccessibleJobHandler extends AbstractJobHandler<Accessible> {

    private OkHttpClient client;
    private ObjectMapper objectMapper = new ObjectMapper();

    public AccessibleJobHandler(Accessible spec,OkHttpClient client) {
        super(spec);
        this.client = client;
    }

    public AccessibleJobHandler(Accessible spec) {
        this(spec,new OkHttpClient());
    }

    @Override
    protected Response handle(ActivatedJob job) throws Throwable {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), job.toJson());
        return objectMapper.readValue(client.newCall(new Request.Builder()
                        .url(getSpec().getUrl())
                        .post(body)
                        .build())
                .execute()
                .body()
                .byteStream(), Response.class);
    }

}
