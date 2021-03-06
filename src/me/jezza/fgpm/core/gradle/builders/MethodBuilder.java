package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.core.exceptions.GradleSyntaxException;
import me.jezza.fgpm.core.gradle.components.MethodComponent;
import me.jezza.fgpm.core.gradle.lib.ElementTypes;
import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

import java.util.ArrayList;
import java.util.List;

public class MethodBuilder implements IComponentBuilder {

    public MethodBuilder() {
    }

    @Override
    public IGradleComponent consume(GradleNavigator navigator) {
        String methodName = navigator.asNamespace();
        List<IGradleComponent> components = new ArrayList<>();

        if (!navigator.hasNext())
            throw new GradleSyntaxException("Invalid method body @ " + navigator.cursorPosition());

        while (navigator.hasNext()) {
            navigator.next();
            if (navigator.type() == ElementTypes.METHOD_END)
                break;
            IGradleComponent build = navigator.builder().build(navigator.type());
            if (build == null)
                throw new GradleSyntaxException("No build component returned!");
            components.add(build);
        }

        if (navigator.type() != ElementTypes.METHOD_END)
            throw new GradleSyntaxException("Invalid method body @ " + navigator.cursorPosition());

        return new MethodComponent(methodName, components);
    }
}
