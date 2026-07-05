package committee.nova.mkw.bridge.binding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.extensions.IKeyMappingExtension;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PlatformBindingBridge implements BindingAccessBridge<KeyMapping, InputConstants.Key, KeyModifier>, BindingMutationBridge<KeyMapping, InputConstants.Key, KeyModifier> {
    @Override
    public List<KeyMapping> getAllBindings() {
        return List.of(Minecraft.getInstance().options.keyMappings);
    }

    @Override
    public List<String> getCategories() {
        return getAllBindings().stream()
                .map(KeyMapping::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
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
        return binding.getKey();
    }

    @Override
    public KeyModifier getModifier(KeyMapping binding) {
        return ((IKeyMappingExtension) binding).getKeyModifier();
    }

    @Override
    public boolean isUnbound(KeyMapping binding) {
        return binding.isUnbound();
    }

    @Override
    public void setModifierAndKey(KeyMapping binding, KeyModifier modifier, InputConstants.Key key) {
        ((IKeyMappingExtension) binding).setKeyModifierAndCode(modifier, key);
    }

    @Override
    public void resetToDefault(KeyMapping binding) {
        ((IKeyMappingExtension) binding).setToDefault();
    }

    @Override
    public void refreshMappings() {
        KeyMapping.resetMapping();
    }
}
