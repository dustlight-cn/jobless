package cn.dustlight.jobless.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class JobHandlerProvider<T> {

    private JobHandlerUpListener<T> upListener;
    private JobHandlerDownListener downListener;

    protected void callUp(String key, AbstractJobHandler<T> handler) {
        upListener.onJobHandlerUp(key, handler);
    }

    protected void callDown(String key) {
        downListener.onJobHandlerDown(key);
    }

    public interface JobHandlerUpListener<T> {
        void onJobHandlerUp(String key, AbstractJobHandler<T> handler);
    }

    public interface JobHandlerDownListener {
        void onJobHandlerDown(String key);
    }

    public abstract void start();

    public abstract void stop();

    public abstract boolean isClosed();
}
