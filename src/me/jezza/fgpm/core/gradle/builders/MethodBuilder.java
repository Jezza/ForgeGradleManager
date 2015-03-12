package me.jezza.fgpm.core.gradle.builders;

import me.jezza.fgpm.core.gradle.lib.IComponentBuilder;
import me.jezza.fgpm.core.gradle.lib.IGradleComponent;
import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

public class MethodBuilder implements IComponentBuilder {

    private IComponentBuilder[] nested;
    private int index = 0;

    public MethodBuilder(int count) {
        if (count > 0)
            nested = new IComponentBuilder[count];
    }

    @Override
    public void addComponent(IComponentBuilder builder) {
        nested[index] = builder;
        if (index++ > nested.length)
            throw new ArrayIndexOutOfBoundsException("No available space for the builder.");
    }

    @Override
    public void consume(GradleNavigator navigator) {
//        int depth = 0;
//        while (navigator.hasNext()) {
//            byte type = navigator.type();
//            if (depth == 0 && type == ElementTypes.METHOD_END)
//                break;
//            switch (type) {
//                case FUNCTION_START:
//                case METHOD_START:
//                    depth++;
//                    break;
//                case FUNCTION_END:
//                case METHOD_END:
//                    depth--;
//            }
//
//            navigator.next();
//        }
    }

    public IGradleComponent build() {
        IGradleComponent[] components = new IGradleComponent[nested.length];
        for (int i = 0; i < nested.length; i++)
            components[i] = nested[i].build();
        // TODO Finish this.
        return components[0];
    }
}
