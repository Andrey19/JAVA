package ru.effectivemobile.java.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class SortingCanvasView extends View {

    private int[] array;
    private final int itemCount = 25;
    private int pivotIndex = -1;
    private int swapIndex1 = -1;
    private int swapIndex2 = -1;
    private boolean isSorted = false;
    private volatile boolean isSorting = false;

    private final Paint barPaint = new Paint();
    private final Paint pivotPaint = new Paint();
    private final Paint swapPaint = new Paint();
    private final Paint sortedPaint = new Paint();

    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private int animationDelay = 60;

    public SortingCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
        generateRandomArray();
    }

    private void initPaints() {
        barPaint.setColor(Color.parseColor("#4A90E2"));
        barPaint.setStyle(Paint.Style.FILL);

        pivotPaint.setColor(Color.parseColor("#E74C3C")); // красный
        pivotPaint.setStyle(Paint.Style.FILL);

        swapPaint.setColor(Color.parseColor("#2ECC71")); // зелёный
        swapPaint.setStyle(Paint.Style.FILL);

        sortedPaint.setColor(Color.parseColor("#F1C40F")); // золотой
        sortedPaint.setStyle(Paint.Style.FILL);
    }

    public void generateRandomArray() {
        isSorted = false;
        isSorting = false;
        pivotIndex = -1;
        swapIndex1 = -1;
        swapIndex2 = -1;
        array = new int[itemCount];
        Random rand = new Random();
        for (int i = 0; i < itemCount; i++) {
            array[i] = rand.nextInt(290) + 10;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (array == null || array.length == 0) return;

        int width = getWidth();
        int height = getHeight();
        int padding = 20;
        float barWidth = (float) (width - 2 * padding) / array.length;
        float maxHeight = height - 40;

        for (int i = 0; i < array.length; i++) {
            float left = padding + i * barWidth;
            float right = left + barWidth - 2;
            float barHeight = (float) array[i] / 300 * maxHeight;
            float top = height - 20 - barHeight;

            Paint currentPaint;
            if (isSorted) {
                currentPaint = sortedPaint;
            } else if (i == pivotIndex) {
                currentPaint = pivotPaint;
            } else if (i == swapIndex1 || i == swapIndex2) {
                currentPaint = swapPaint;
            } else {
                currentPaint = barPaint;
            }

            canvas.drawRect(left, top, right, height - 20, currentPaint);
        }
    }

    public void startSort() {
        if (isSorting || array == null) return;
        if (isSorted) {
            generateRandomArray(); // если уже отсортировано – сначала перемешаем
        }
        isSorting = true;
        isSorted = false;

        new Thread(() -> {
            try {
                quickSort(0, array.length - 1);
                uiHandler.post(() -> {
                    isSorted = true;
                    isSorting = false;
                    pivotIndex = -1;
                    swapIndex1 = -1;
                    swapIndex2 = -1;
                    invalidate();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void quickSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) throws InterruptedException {
        int pivot = array[high];
        setHighlights(high, -1, -1);
        Thread.sleep(animationDelay);

        int i = low - 1;
        for (int j = low; j < high; j++) {
            setHighlights(high, i, j);
            Thread.sleep(animationDelay);

            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                setHighlights(high, i, j);
                Thread.sleep(animationDelay);
            }
        }

        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        setHighlights(-1, i + 1, high);
        Thread.sleep(animationDelay);
        setHighlights(-1, -1, -1);
        return i + 1;
    }

    private void setHighlights(int pivot, int s1, int s2) {
        uiHandler.post(() -> {
            pivotIndex = pivot;
            swapIndex1 = s1;
            swapIndex2 = s2;
            invalidate();
        });
        try {
            Thread.sleep(5);
        } catch (InterruptedException ignored) {}
    }


    public void stopSorting() {
        isSorting = false;
    }
}
