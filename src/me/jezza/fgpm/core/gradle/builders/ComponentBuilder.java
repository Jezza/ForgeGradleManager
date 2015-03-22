package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.App;
import me.jezza.fgpm.core.exceptions.GradleSyntaxException;
import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

import java.util.ArrayList;
import java.util.List;

import static me.jezza.fgpm.core.gradle.lib.ElementTypes.*;

public class ComponentBuilder {

    private List<IGradleComponent> components = new ArrayList<>();
    private GradleNavigator navigator;

    public ComponentBuilder(GradleNavigator navigator) {
        this.navigator = navigator;
    }

    public IGradleComponent[] buildAll() {
        while (navigator.hasNext()) {
            navigator.next();
            App.LOG.info(navigator.asString());
            App.LOG.info(navigator.typeString());
//            IGradleComponent component = build(navigator.type());
//            if (component == null)
//                throw new GradleSyntaxException("No build component returned!");
//            components.add(component);
        }

        return components.toArray(new IGradleComponent[components.size()]);
    }

    public IGradleComponent build(byte type) {
        switch (type) {
            case METHOD_START:
                return beginBuilder(new MethodBuilder());
            case FUNCTION_START:
                return beginBuilder(new FunctionBuilder());
            case NAMESPACE:
            case STRING: // ""
                return beginBuilder(new StringBuilder());
            case EQUAL: // =
                return beginBuilder(new FieldBuilder());
            case COLON: // :
                return beginBuilder(new StatementBuilder());
            case COMMA: // ,
                return null;
            case LAMBDA: // ->
                return null;
            case METHOD_END:
                throw new GradleSyntaxException("Invalid '}' @ " + navigator.cursorPosition());
            case FUNCTION_END:
                throw new GradleSyntaxException("Invalid ')' @ " + navigator.cursorPosition());
            case CONCAT: // +
                throw new GradleSyntaxException("Invalid '+' @ " + navigator.cursorPosition());
        }
        return null;
    }

    private IGradleComponent beginBuilder(IComponentBuilder builder) {
        IGradleComponent component = builder.consume(navigator);
        if (component == null)
            throw new GradleSyntaxException(builder.getClass().getSimpleName() + "returned a null component!");
        return component;
    }
}
