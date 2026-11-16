package committee.nova.mkw.bridge.binding;

import committee.nova.mkw.compat.HideKeyBindingCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PlatformBindingBridge implements BindingAccessBridge<KeyBinding, InputMappings.Input, KeyModifier>,
        BindingMutationBridge<KeyBinding, InputMappings.Input, KeyModifier> {

    @Override
    @SuppressWarnings("resource")
    public List<KeyBinding> getAllBindings() {
        return Arrays.stream(Minecraft.getInstance().options.keyMappings)
                .filter(PlatformBindingBridge::isVisible)
                .collect(Collectors.toList());
    }

    public static boolean isVisible(KeyBinding binding) {
        return binding != null && !HideKeyBindingCompat.isHidden(binding.getName());
    }

    @Override
    public List<String> getCategories() {
        return getAllBindings().stream()
                .map(KeyBinding::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Map<InputMappings.Input, Integer> getBindingCountsByKey() {
        Map<InputMappings.Input, Integer> counts = new HashMap<>();
        for (KeyBinding binding : getAllBindings()) {
            counts.merge(getKey(binding), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(counts);
    }

    @Override
    public InputMappings.Input getKey(KeyBinding binding) {
        return binding.getKey();
    }

    @Override
    public KeyModifier getModifier(KeyBinding binding) {
        return binding.getKeyModifier();
    }

    @Override
    public boolean isUnbound(KeyBinding binding) {
        return getKey(binding) == InputMappings.UNKNOWN;
    }

    @Override
    public void setModifierAndKey(KeyBinding binding, KeyModifier modifier, InputMappings.Input key) {
        binding.setKeyModifierAndCode(modifier, key);
    }

    @Override
    public void resetToDefault(KeyBinding binding) {
        binding.setToDefault();
    }

    @Override
    public void refreshMappings() {
        KeyBinding.resetMapping();
    }
}
