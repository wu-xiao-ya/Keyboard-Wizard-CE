package committee.nova.mkw.bridge.binding;

import committee.nova.mkw.api.IKeyBinding;
import committee.nova.mkw.keybinding.KeyModifier;
import committee.nova.mkw.mixin.AccessorKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PlatformBindingBridge implements BindingAccessBridge<KeyBinding, InputUtil.Key, KeyModifier>, BindingMutationBridge<KeyBinding, InputUtil.Key, KeyModifier> {
    @Override
    public List<KeyBinding> getAllBindings() {
        return List.of(MinecraftClient.getInstance().options.allKeys);
    }

    @Override
    public List<String> getCategories() {
        return AccessorKeyBinding.getKeyCategories().stream().sorted().toList();
    }

    @Override
    public Map<InputUtil.Key, Integer> getBindingCountsByKey() {
        HashMap<InputUtil.Key, Integer> map = new HashMap<>();
        for (KeyBinding binding : getAllBindings()) {
            map.merge(getKey(binding), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public InputUtil.Key getKey(KeyBinding binding) {
        return ((AccessorKeyBinding) binding).getBoundKey();
    }

    @Override
    public KeyModifier getModifier(KeyBinding binding) {
        return ((IKeyBinding) binding).getKeyModifier();
    }

    @Override
    public boolean isUnbound(KeyBinding binding) {
        return binding.isUnbound();
    }

    @Override
    public void setModifierAndKey(KeyBinding binding, KeyModifier modifier, InputUtil.Key key) {
        ((IKeyBinding) binding).setKeyModifierAndCode(modifier, key);
    }

    @Override
    public void resetToDefault(KeyBinding binding) {
        ((IKeyBinding) binding).setToDefault();
    }

    @Override
    public void refreshMappings() {
        KeyBinding.updateKeysByCode();
    }
}
