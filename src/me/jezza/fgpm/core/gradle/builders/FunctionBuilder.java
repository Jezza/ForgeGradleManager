package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.core.exceptions.GradleSyntaxException;
import me.jezza.fgpm.core.gradle.components.FunctionComponent;
import me.jezza.fgpm.core.gradle.lib.ElementTypes;
import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

import java.util.ArrayList;
import java.util.List;

public class FunctionBuilder implements IComponentBuilder {
    public FunctionBuilder() {
    }

    @Override
    public IGradleComponent consume(GradleNavigator navigator) {
        String functionName = navigator.asString();
        functionName = functionName.substring(0, functionName.length() - 1);
        List<IGradleComponent> components = new ArrayList<>();

        if (!navigator.hasNext())
            throw new GradleSyntaxException("Invalid function body @ " + navigator.cursorPosition());

        while (navigator.hasNext()) {
            navigator.next();
            if (navigator.type() == ElementTypes.FUNCTION_END)
                break;
            IGradleComponent build = navigator.builder().build(navigator.type());
            if (build == null)
                throw new GradleSyntaxException("No build component returned!");
            components.add(build);
        }

        if (navigator.type() != ElementTypes.FUNCTION_END)
            throw new GradleSyntaxException("Invalid function body @ " + navigator.cursorPosition());

        return new FunctionComponent(functionName, components);
    }
}
