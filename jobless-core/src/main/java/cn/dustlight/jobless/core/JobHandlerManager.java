package cn.dustlight.jobless.core;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class JobHandlerManager<T> {

    private Map<String, JobWorker> handlerMap;
    private ZeebeClient client;
    private final Log logger = LogFactory.getLog(getClass());

    public JobHandlerManager(ZeebeClient client) {
        this(new ConcurrentHashMap<>(), client);
    }

    public void addHandler(String key, AbstractJobHandler<T> handler) {
        if (handlerMap.containsKey(key)) {
            JobWorker oldWorker = handlerMap.get(key);
            if (oldWorker != null)
                oldWorker.close();
        }
        JobWorker worker = client.newWorker()
                .jobType(key)
                .handler(handler)
                .open();
        handlerMap.put(key, worker);
        logger.info(String.format("Handler up: [%s].", key));
    }

    public void removeHandler(String key) {
        JobWorker worker = handlerMap.remove(key);
        if (worker != null) {
            worker.close();
            logger.info(String.format("Handler down: [%s].", key));
        }
    }

    public boolean isAnyClosed() {
        for (JobWorker worker : handlerMap.values()) {
            if (worker.isClosed())
                return true;
        }
        return false;
    }
}
