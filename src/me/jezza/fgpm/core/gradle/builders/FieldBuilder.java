package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.core.exceptions.GradleSyntaxException;
import me.jezza.fgpm.core.gradle.components.FieldComponent;
import me.jezza.fgpm.core.gradle.lib.ElementTypes;
import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

import java.util.ArrayList;
import java.util.List;

public class FieldBuilder implements IComponentBuilder {
    public FieldBuilder() {
    }

    @Override
    public IGradleComponent consume(GradleNavigator navigator) {
        String name = navigator.asNamespace();
        if (!navigator.hasNext())
            throw new GradleSyntaxException(String.format("No value following equals @ %s", navigator.cursorPosition()));
        navigator.next();

        byte type = navigator.type();
        if (!(type == ElementTypes.NAMESPACE || type == ElementTypes.STRING))
            throw new GradleSyntaxException(String.format("Invalid equal type: %s @ %s", type, navigator.cursorPosition()));

        String initValue = navigator.asString().trim();
        List<String> parts = new ArrayList<>();
        while (navigator.hasNext()) {
            navigator.next();

            if (navigator.type() != ElementTypes.CONCAT){
                navigator.previous();
                break;
            }
            if (!navigator.hasNext())
                throw new GradleSyntaxException(String.format("Invalid addition: %s @ %s", type, navigator.cursorPosition()));

            navigator.next();
            parts.add(navigator.asString());
        }

        return new FieldComponent(name, initValue, parts);
    }
}
