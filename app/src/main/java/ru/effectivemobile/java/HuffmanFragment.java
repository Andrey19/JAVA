package ru.effectivemobile.java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;

import ru.effectivemobile.java.huffman.FrequencyAdapter;
import ru.effectivemobile.java.huffman.HuffmanCoder;

public class HuffmanFragment extends Fragment {

    private TextInputEditText inputText;
    private TextView compressedText, compressionRatio, decodedText;
    private RecyclerView freqRecyclerView;
    private FrequencyAdapter freqAdapter;
    private HuffmanCoder huffmanCoder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_huffman, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputText = view.findViewById(R.id.inputText);
        compressedText = view.findViewById(R.id.compressedText);
        compressionRatio = view.findViewById(R.id.compressionRatio);
        decodedText = view.findViewById(R.id.decodedText);
        freqRecyclerView = view.findViewById(R.id.freqRecyclerView);
        freqRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        freqAdapter = new FrequencyAdapter();
        freqRecyclerView.setAdapter(freqAdapter);

        MaterialButton btnCompress = view.findViewById(R.id.btnCompress);
        btnCompress.setOnClickListener(v -> compressText());

        huffmanCoder = new HuffmanCoder();
    }

    private void compressText() {
        String text = inputText.getText().toString();
        if (text.isEmpty()) {
            compressedText.setText("Введите текст!");
            return;
        }

        String encoded = huffmanCoder.compress(text);
        compressedText.setText(encoded);

        int originalBits = text.length() * 8; // ASCII по 8 бит
        int compressedBits = encoded.length();
        double ratio = (double) compressedBits / originalBits;
        compressionRatio.setText(String.format("Коэффициент сжатия: %.2f (оригинал: %d бит, сжато: %d бит)",
                ratio, originalBits, compressedBits));

        Map<Character, Integer> freqMap = huffmanCoder.getFrequencyMap(text);
        Map<Character, String> codeMap = huffmanCoder.getHuffmanCodes();
        freqAdapter.setData(codeMap, freqMap);

        String decoded = huffmanCoder.decode(encoded);
        decodedText.setText(decoded);
    }
}