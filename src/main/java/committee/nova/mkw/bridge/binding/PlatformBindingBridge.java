package committee.nova.mkw.bridge.binding;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.keybinding.KeyModifier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PlatformBindingBridge implements BindingAccessBridge<KeyMapping, InputConstants.Key, KeyModifier>, BindingMutationBridge<KeyMapping, InputConstants.Key, KeyModifier> {
    private static final Method GET_KEY = findMethod(KeyMapping.class, InputConstants.Key.class, "getKey");
    private static final Method SET_TO_DEFAULT = findMethod(KeyMapping.class, Void.TYPE, "setToDefault");

    @Override
    public List<KeyMapping> getAllBindings() {
        return Arrays.asList(Minecraft.getInstance().options.keyMappings);
    }

    @Override
    public List<String> getCategories() {
        return getAllBindings().stream()
                .map(KeyMapping::getCategory)
                .distinct()
                .map(category -> category.label().getString())
                .sorted()
                .toList();
    }

    @Override
    public Map<InputConstants.Key, Integer> getBindingCountsByKey() {
        HashMap<InputConstants.Key, Integer> map = new HashMap<>();
        for (KeyMapping binding : getAllBindings()) {
            map.merge(getKey(binding), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public InputConstants.Key getKey(KeyMapping binding) {
        Object key = invoke(GET_KEY, binding);
        return key instanceof InputConstants.Key inputKey ? inputKey : InputConstants.UNKNOWN;
    }

    @Override
    public KeyModifier getModifier(KeyMapping binding) {
        return KeyModifier.NONE;
    }

    @Override
    public boolean isUnbound(KeyMapping binding) {
        return binding.isUnbound();
    }

    @Override
    public void setModifierAndKey(KeyMapping binding, KeyModifier modifier, InputConstants.Key key) {
        binding.setKey(key);
    }

    @Override
    public void resetToDefault(KeyMapping binding) {
        invoke(SET_TO_DEFAULT, binding);
    }

    @Override
    public void refreshMappings() {
        KeyMapping.resetMapping();
    }

    private static Method findMethod(Class<?> owner, Class<?> returnType, String... names) {
        for (String name : names) {
            try {
                Method method = owner.getMethod(name);
                if (returnType == Void.TYPE ? method.getReturnType() == Void.TYPE : returnType.isAssignableFrom(method.getReturnType())) {
                    method.setAccessible(true);
                    return method;
                }
            } catch (NoSuchMethodException ignored) {
            }
        }
        for (Method method : owner.getMethods()) {
            if (method.getParameterCount() == 0 && (returnType == Void.TYPE ? method.getReturnType() == Void.TYPE : returnType.isAssignableFrom(method.getReturnType()))) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    private static Object invoke(Method method, Object target) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
