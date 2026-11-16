package committee.nova.mkw.compat;

import net.minecraftforge.fml.ModList;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Optional runtime bridge for Hide-Key-Binding. */
public final class HideKeyBindingCompat {
    private static final String MOD_ID = "hide_key_binding";
    private static final String CONFIG_CLASS = "com.hkb.client.HKBModConfig";
    private static final String CONFIG_FIELD = "HIDE_KEY_BINDINGS";

    private HideKeyBindingCompat() {
    }

    public static boolean isHidden(String keyId) {
        return keyId != null && getHiddenKeyIds().contains(keyId);
    }

    public static Set<String> getHiddenKeyIds() {
        if (!ModList.get().isLoaded(MOD_ID)) {
            return Collections.emptySet();
        }

        try {
            Class<?> configClass = Class.forName(CONFIG_CLASS);
            Field field = configClass.getDeclaredField(CONFIG_FIELD);
            field.setAccessible(true);
            Object configValue = field.get(null);
            Object hiddenBindings = readConfigValue(configValue);
            return normalizeStrings(hiddenBindings);
        } catch (ReflectiveOperationException | RuntimeException | LinkageError ignored) {
            return Collections.emptySet();
        }
    }

    private static Object readConfigValue(Object configValue) throws ReflectiveOperationException {
        if (configValue == null) {
            return null;
        }
        Method get = configValue.getClass().getMethod("get");
        get.setAccessible(true);
        return get.invoke(configValue);
    }

    private static Set<String> normalizeStrings(Object value) {
        if (value == null) {
            return Collections.emptySet();
        }

        HashSet<String> hidden = new HashSet<>();
        if (value instanceof Iterable<?>) {
            for (Object entry : (Iterable<?>) value) {
                addString(hidden, entry);
            }
        } else if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                addString(hidden, Array.get(value, i));
            }
        } else {
            addString(hidden, value);
        }
        return hidden.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(hidden);
    }

    private static void addString(Set<String> hidden, Object value) {
        if (value instanceof String && !((String) value).isEmpty()) {
            hidden.add((String) value);
        }
    }
}
