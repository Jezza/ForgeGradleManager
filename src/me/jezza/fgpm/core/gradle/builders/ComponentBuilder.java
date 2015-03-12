package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.App;
import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

import static me.jezza.fgpm.core.gradle.lib.ElementTypes.*;

public class ComponentBuilder {

    private GradleNavigator navigator;

    private IComponentBuilder builder;

    public ComponentBuilder(GradleNavigator navigator) {
        this.navigator = navigator;
    }

    public IGradleComponent[] build() {
        while (navigator.hasNext()) {
            navigator.next();
            String name = navigator.asString();
            App.LOG.info(name);
            switch (navigator.type()) {
                case NAMESPACE:
                    continue;
                case METHOD_START:
                    consumeMethod();
                    continue;
                case FUNCTION_START:
                    consumeFunction();
                    continue;
                case METHOD_END:
                case FUNCTION_END:
//                    throw new GradleSyntaxException("Illegal type found.");
                case EQUAL:
                case COLON:
                case STRING:
                case COMMA:
                case LAMBDA:
                case CONCAT:
            }
        }

        return null;
    }

    private void consumeMethod() {
        int count = navigator.countMethodComponents();
        beginBuilder(new MethodBuilder(count));
    }

    private void consumeFunction() {
        int count = navigator.countFunctionComponents();
//        beginBuilder(new FunctionBuilder(name, count));
    }

    private void beginBuilder(IComponentBuilder builder) {
        builder.consume(navigator);
        if (this.builder == null)
            this.builder = builder;
        else
            this.builder.addComponent(builder);
        // Add it to a list or something.
    }
}
