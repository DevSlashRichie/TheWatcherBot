package me.richdev.TheWatcher.Commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    String[] aliases();
    String permissionGroup();
    boolean fromPrivateChat();
    String descriptionID();

}
