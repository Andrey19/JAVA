package ru.effectivemobile.java.huffman;

public class HuffmanNode implements Comparable<HuffmanNode> {
    Character character;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(Character character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        this.character = null;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }
}