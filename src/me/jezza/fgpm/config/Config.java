package me.jezza.fgpm.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Config {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Controller {
        String dataFile() default "data.cfg";
        
        String configFile() default "user.cfg";
    }

}
