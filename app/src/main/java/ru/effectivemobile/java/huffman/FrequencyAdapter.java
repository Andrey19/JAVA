package ru.effectivemobile.java.huffman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.effectivemobile.java.R;

public class FrequencyAdapter extends RecyclerView.Adapter<FrequencyAdapter.FreqViewHolder> {

    private List<Map.Entry<Character, String>> entries = new ArrayList<>();

    public void setData(Map<Character, String> codeMap, Map<Character, Integer> freqMap) {
        entries.clear();
        for (Map.Entry<Character, String> entry : codeMap.entrySet()) {
            Character ch = entry.getKey();
            String code = entry.getValue();
            int freq = freqMap.getOrDefault(ch, 0);
            entries.add(new Map.Entry<Character, String>() {
                @Override
                public Character getKey() { return ch; }

                @Override
                public String getValue() { return code; }

                @Override
                public String setValue(String value) { return null; }
            });
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FreqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_freq, parent, false);
        return new FreqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreqViewHolder holder, int position) {
        Map.Entry<Character, String> entry = entries.get(position);
        char symbol = entry.getKey();
        String code = entry.getValue();
        String displayChar = (symbol == '\n') ? "\\n" :
                (symbol == '\t') ? "\\t" :
                        (symbol == ' ') ? "пробел" :
                                String.valueOf(symbol);
        holder.charText.setText("Символ: " + displayChar);
        holder.codeText.setText("Код: " + code);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class FreqViewHolder extends RecyclerView.ViewHolder {
        TextView charText, codeText;
        FreqViewHolder(@NonNull View itemView) {
            super(itemView);
            charText = itemView.findViewById(R.id.charText);
            codeText = itemView.findViewById(R.id.codeText);
        }
    }
}
