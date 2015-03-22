package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.core.exceptions.GradleSyntaxException;
import me.jezza.fgpm.core.gradle.components.StatementComponent;
import me.jezza.fgpm.core.gradle.lib.ElementTypes;
import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

public class StatementBuilder implements IComponentBuilder {

    public StatementBuilder() {
    }

    @Override
    public IGradleComponent consume(GradleNavigator navigator) {
        String name = navigator.asString();

        if (!navigator.hasNext())
            throw new GradleSyntaxException("Invalid statement @ " + navigator.cursorPosition());

        navigator.next();
        if (navigator.type() != ElementTypes.STRING)
            throw new GradleSyntaxException("Invalid statement @ " + navigator.cursorPosition());
        return new StatementComponent(name.substring(0, name.length() - 1), navigator.asString());
    }
}
