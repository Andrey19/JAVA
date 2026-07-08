package ru.effectivemobile.java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import ru.effectivemobile.java.view.SortingCanvasView;

public class SortFragment extends Fragment {

    private SortingCanvasView canvasView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        canvasView = view.findViewById(R.id.sortingCanvas);
        MaterialButton btnGenerate = view.findViewById(R.id.btnGenerate);
        MaterialButton btnSort = view.findViewById(R.id.btnSort);

        btnGenerate.setOnClickListener(v -> {
            canvasView.stopSorting();
            canvasView.generateRandomArray();
        });

        btnSort.setOnClickListener(v -> canvasView.startSort());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (canvasView != null) {
            canvasView.stopSorting();
        }
    }
}