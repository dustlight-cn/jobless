package plus.jobless.core;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

@AllArgsConstructor
public abstract class AbstractJobHandler<T> implements JobHandler {

    private T spec;
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void handle(JobClient client, ActivatedJob job) {
        Response response;
        try {
            response = handle(job, spec);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            response = new Response();
            response.setResult(Result.ERROR);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(byteArrayOutputStream));
            response.errorMessage = e.getMessage() == null ? new String(byteArrayOutputStream.toByteArray()) : e.getMessage();
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
    }

    protected abstract Response handle(ActivatedJob job, T spec) throws Throwable;

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
