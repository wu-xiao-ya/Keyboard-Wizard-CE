package committee.nova.mkw.util;

import committee.nova.mkw.ModernKeyWizard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MinecraftCompat {
    private static final Method SET_SCREEN = findMethod(Minecraft.class, Screen.class, "setScreen", "setScreen");
    private static final Method GET_SCREEN = findGetter(Minecraft.class, Screen.class, "getScreen", "screen");
    private static final Field SCREEN_FIELD = findField(Minecraft.class, Screen.class, "screen");

    private MinecraftCompat() {
    }

    public static Screen getScreen(Minecraft minecraft) {
        if (minecraft == null) return null;
        Object value = invoke(GET_SCREEN, minecraft);
        if (value instanceof Screen screen) return screen;
        Object fieldValue = readField(SCREEN_FIELD, minecraft);
        return fieldValue instanceof Screen screen ? screen : null;
    }

    public static void setScreen(Minecraft minecraft, Screen screen) {
        if (minecraft == null) return;
        if (SET_SCREEN != null) {
            invoke(SET_SCREEN, minecraft, screen);
        }
    }

    private static Method findMethod(Class<?> owner, Class<?> parameterType, String... names) {
        for (String name : names) {
            try {
                Method method = owner.getMethod(name, parameterType);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
        }
        for (Method method : owner.getMethods()) {
            if (method.getReturnType() == Void.TYPE && method.getParameterCount() == 1 && parameterType.isAssignableFrom(method.getParameterTypes()[0])) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    private static Method findGetter(Class<?> owner, Class<?> returnType, String... names) {
        for (String name : names) {
            try {
                Method method = owner.getMethod(name);
                if (returnType.isAssignableFrom(method.getReturnType())) {
                    method.setAccessible(true);
                    return method;
                }
            } catch (NoSuchMethodException ignored) {
            }
        }
        for (Method method : owner.getMethods()) {
            if (method.getParameterCount() == 0 && returnType.isAssignableFrom(method.getReturnType())) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    private static Field findField(Class<?> owner, Class<?> fieldType, String... names) {
        for (String name : names) {
            try {
                Field field = owner.getField(name);
                if (fieldType.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return field;
                }
            } catch (NoSuchFieldException ignored) {
            }
        }
        for (Field field : owner.getFields()) {
            if (fieldType.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    private static Object invoke(Method method, Object target, Object... args) {
        if (method == null) return null;
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            ModernKeyWizard.LOGGER.warn("Unable to call Minecraft compatibility method {}", method.getName(), e);
            return null;
        }
    }

    private static Object readField(Field field, Object target) {
        if (field == null) return null;
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            ModernKeyWizard.LOGGER.warn("Unable to read Minecraft compatibility field {}", field.getName(), e);
            return null;
        }
    }
}
