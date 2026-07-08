package ru.effectivemobile.java.task;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkerThread extends Thread {

    private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning = true;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private OnTaskStatusListener listener;

    public interface OnTaskStatusListener {
        void onTaskStarted(Task task);
        void onTaskFinished(Task task);
        void onThreadStateChanged(boolean isWorking);
    }

    public void setListener(OnTaskStatusListener listener) {
        this.listener = listener;
    }

    public void execute(Task task) {
        if (task != null) {
            taskQueue.offer(task);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                final Task task = taskQueue.take();

                if (listener != null) {
                    uiHandler.post(() -> {
                        listener.onThreadStateChanged(true);
                        listener.onTaskStarted(task);
                    });
                }

                task.execute();

                if (listener != null) {
                    uiHandler.post(() -> {
                        listener.onTaskFinished(task);
                        if (taskQueue.isEmpty()) {
                            listener.onThreadStateChanged(false);
                        }
                    });
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void quit() {
        isRunning = false;
        interrupt();
    }

    public boolean isIdle() {
        return taskQueue.isEmpty();
    }
}
