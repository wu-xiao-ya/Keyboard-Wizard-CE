package committee.nova.mkw.api;

import net.minecraft.client.option.KeyBinding;

public interface IKeyConflictContext {
    boolean isActive();

    boolean conflicts(IKeyConflictContext other);

    default boolean conflicts(KeyBinding self, KeyBinding other) {
        return conflicts(((IKeyBinding) other).getKeyConflictContext());
    }
}
