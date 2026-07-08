package ru.effectivemobile.java.huffman;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCoder {

    private Map<Character, String> huffmanCodeMap = new HashMap<>();
    private HuffmanNode root;


    public String compress(String text) {
        if (text == null || text.isEmpty()) return "";

        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            pq.add(parent);
        }
        root = pq.poll();

        huffmanCodeMap.clear();
        generateCodes(root, "");

        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(huffmanCodeMap.get(c));
        }
        return encoded.toString();
    }

    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.isLeaf()) {
            huffmanCodeMap.put(node.character, code);
        } else {
            generateCodes(node.left, code + "0");
            generateCodes(node.right, code + "1");
        }
    }


    public String decode(String encoded) {
        if (encoded == null || encoded.isEmpty() || root == null) return "";
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;
        for (char bit : encoded.toCharArray()) {
            if (bit == '0') current = current.left;
            else if (bit == '1') current = current.right;

            if (current.isLeaf()) {
                decoded.append(current.character);
                current = root; // возвращаемся к корню
            }
        }
        return decoded.toString();
    }

    public Map<Character, Integer> getFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        if (text == null) return freqMap;
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }

    public Map<Character, String> getHuffmanCodes() {
        return huffmanCodeMap;
    }

    public HuffmanNode getRoot() {
        return root;
    }
}
