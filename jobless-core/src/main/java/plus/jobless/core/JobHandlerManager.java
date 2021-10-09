package plus.jobless.core;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class JobHandlerManager<T> {

    private Map<String, JobWorker> handlerMap;
    private ZeebeClient client;

    public JobHandlerManager(ZeebeClient client) {
        this(new ConcurrentHashMap<>(), client);
    }

    public void addHandler(String key, AbstractJobHandler<T> handler) {
        JobWorker worker = client.newWorker()
                .jobType(key)
                .handler(handler)
                .open();
        handlerMap.put(key, worker);
    }

    public void removeHandler(String key) {
        JobWorker worker = handlerMap.remove(key);
        if (worker.isOpen())
            worker.close();
    }
}
