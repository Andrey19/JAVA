package ru.effectivemobile.java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ru.effectivemobile.java.task.LogAdapter;
import ru.effectivemobile.java.task.Task;
import ru.effectivemobile.java.task.WorkerThread;

public class WorkerFragment extends Fragment implements WorkerThread.OnTaskStatusListener {

    private WorkerThread workerThread;
    private LogAdapter logAdapter;
    private final List<String> logs = new ArrayList<>();
    private View statusIndicator;
    private TextView statusText;
    private TextView queueSizeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_worker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        statusIndicator = view.findViewById(R.id.statusIndicator);
        statusText = view.findViewById(R.id.statusText);
        queueSizeText = view.findViewById(R.id.queueSizeText);

        RecyclerView logRecyclerView = view.findViewById(R.id.logRecyclerView);
        logRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        logAdapter = new LogAdapter(logs);
        logRecyclerView.setAdapter(logAdapter);


        workerThread = new WorkerThread();
        workerThread.setListener(this);
        workerThread.start();


        MaterialButton btnSimple = view.findViewById(R.id.btnAddSimpleTask);
        MaterialButton btnHeavy = view.findViewById(R.id.btnAddHeavyTask);
        MaterialButton btnMany = view.findViewById(R.id.btnAddManyTasks);

        btnSimple.setOnClickListener(v -> addSimpleTask());
        btnHeavy.setOnClickListener(v -> addHeavyTask());
        btnMany.setOnClickListener(v -> {
            for (int i = 0; i < 5; i++) {
                addSimpleTask();
            }
        });
    }

    private void addSimpleTask() {
        workerThread.execute(new Task() {
            @Override
            public void execute() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
            }

            @Override
            public String getName() {
                return "Простая задача";
            }
        });
    }

    private void addHeavyTask() {
        workerThread.execute(new Task() {
            @Override
            public void execute() {
                try {
                    long result = 1;
                    for (int i = 2; i <= 15; i++) {
                        result *= i;
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ignored) {}
            }

            @Override
            public String getName() {
                return "Тяжёлая задача (15!)";
            }
        });
    }

    @Override
    public void onTaskStarted(Task task) {
        String msg = "▶ Начало: " + task.getName();
        logAdapter.addLog(msg);
        queueSizeText.setText("Очередь: " + (workerThread.isIdle() ? 0 : "?"));
    }

    @Override
    public void onTaskFinished(Task task) {
        String msg = "✔ Завершено: " + task.getName();
        logAdapter.addLog(msg);
        int remaining = workerThread.isIdle() ? 0 : 1;
        queueSizeText.setText("Очередь: " + remaining);
    }

    @Override
    public void onThreadStateChanged(boolean isWorking) {
        if (isWorking) {
            statusIndicator.setBackground(
                    ContextCompat.getDrawable(requireContext(), R.drawable.indicator_working));
            statusText.setText("Работает");
            statusText.setTextColor(android.graphics.Color.parseColor("#2ECC71"));
        } else {
            statusIndicator.setBackground(
                    ContextCompat.getDrawable(requireContext(), R.drawable.indicator_idle));
            statusText.setText("Спит");
            statusText.setTextColor(android.graphics.Color.parseColor("#888888"));
        }
        if (!isWorking) {
            queueSizeText.setText("Очередь: 0");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (workerThread != null) {
            workerThread.quit();
            try {
                workerThread.join();
            } catch (InterruptedException ignored) {}
        }
    }
}