package me.jezza.fgpm.core.gradle.reader;

public class IndexBuffer {
    public int[] position = null;
    public int[] length = null;
    public byte[] type = null;
    public int count = 0;

    public IndexBuffer() {
    }

    public IndexBuffer(int capacity, boolean useTypeArray) {
        this.position = new int[capacity];
        this.length = new int[capacity];
        if (useTypeArray)
            this.type = new byte[capacity];
    }
}
