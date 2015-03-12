package me.jezza.fgpm.core.gradle.reader;

public class CharBuffer {
    public char[] data = null;
    public int length = 0;

    public CharBuffer() {
    }

    public CharBuffer(char[] data) {
        this.data = data;
    }

    public CharBuffer(int capacity) {
        this.data = new char[capacity];
    }

}
