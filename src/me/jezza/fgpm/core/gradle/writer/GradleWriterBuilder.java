package me.jezza.fgpm.core.gradle.writer;

import com.google.gson.internal.Streams;

import java.io.StringWriter;
import java.io.Writer;

public class GradleWriterBuilder {

    private Writer out;
    private String indent;
    private boolean prettyPrinting;

    public GradleWriterBuilder() {
        this.out = new StringWriter();
        this.indent = "    ";
        this.prettyPrinting = true;
    }

    public GradleWriterBuilder setWriter(Appendable appendable) {
        out = Streams.writerForAppendable(appendable);
        return this;
    }

    public GradleWriterBuilder setIndent(String indent) {
        this.indent = indent;
        return this;
    }

    public GradleWriterBuilder disablePrettyPrinting() {
        prettyPrinting = false;
        return this;
    }

    public GradleWriter create() {
        return new GradleWriter(out, prettyPrinting);
    }

}
