package com.romantiskt.easyannotations.core;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by romantiskt on 2018/8/15.
 */
public class BindUtil {
    public static void bind(Activity activity) {
        bind(activity, activity.getWindow().getDecorView());
    }

    public static void bind(Object target, View source) {
        Class<?> cls = target.getClass();
        String clsName = cls.getName();
        String bClassName = clsName + "_Binding";
        try {
            Class<?> bindingClass = cls.getClassLoader().loadClass(bClassName);
            //noinspection unchecked
            Constructor constructor = bindingClass.getConstructor(cls, View.class);
            constructor.newInstance(target, source);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException " + bClassName, e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to invoke " + bClassName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + bClassName, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + bClassName, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke " + bClassName, e);
        }
    }
}
