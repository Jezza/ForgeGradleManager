package me.jezza.fgpm.core.gradle.lib;

import me.jezza.fgpm.core.gradle.reader.GradleNavigator;

public interface IComponentBuilder {

    public IGradleComponent consume(GradleNavigator navigator);

}
