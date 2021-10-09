package plus.jobless.core;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class AbstractJobHandler<T> implements JobHandler {

    private T spec;

    @Override
    public void handle(JobClient client, ActivatedJob job) {
        Response response;
        try {
            response = handle(job);
        } catch (Throwable e) {
            response = new Response();
            response.errorMessage = e.getMessage();
            response.errorCode = "Internal Server Error";
        }
        switch (response.result) {
            case FAIL:
                client.newFailCommand(job.getKey())
                        .retries(response.retires)
                        .errorMessage(response.errorMessage)
                        .send()
                        .join();
                break;
            case ERROR:
                client.newThrowErrorCommand(job.getKey())
                        .errorCode(response.errorCode)
                        .errorMessage(response.errorMessage)
                        .send()
                        .join();
                break;
            case COMPLETE:
            default:
                client.newCompleteCommand(job.getKey())
                        .variables(response.variables)
                        .send()
                        .join();
                break;
        }
        job.toJson();
    }

    protected abstract Response handle(ActivatedJob job) throws Throwable;

    /**
     * Job Execute Response
     */
    @Getter
    @Setter
    public static class Response {

        private Result result;

        // On Error
        private String errorCode;

        // On Error or Fail
        private String errorMessage;

        // On Fail
        private int retires;

        // On Complete
        private Map<String, Object> variables;

    }

    /**
     * Job Execute Result
     */
    public enum Result {
        COMPLETE,
        FAIL,
        ERROR
    }
}
