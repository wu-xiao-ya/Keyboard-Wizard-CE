package committee.nova.mkw.api;

import committee.nova.mkw.keybinding.KeyConflictContext;
import committee.nova.mkw.keybinding.KeyModifier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public interface IKeyBinding {
    InputUtil.Key getKey();

    IKeyConflictContext getKeyConflictContext();

    KeyModifier getKeyModifierDefault();

    KeyModifier getKeyModifier();

    void setKeyConflictContext(IKeyConflictContext keyConflictContext);

    void setKeyModifierAndCode(KeyModifier keyModifier, InputUtil.Key keyCode);

    default boolean isConflictContextAndModifierActive() {
        return getKeyConflictContext().isActive() && getKeyModifier().isActive();
    }

    default boolean hasKeyCodeModifierConflict(KeyBinding other) {
        IKeyBinding otherExt = (IKeyBinding) other;
        if (getKey().equals(InputUtil.UNKNOWN_KEY) || otherExt.getKey().equals(InputUtil.UNKNOWN_KEY)) {
            return false;
        }
        return getKey().equals(otherExt.getKey())
                && getKeyModifier().equals(otherExt.getKeyModifier())
                && getKeyConflictContext().conflicts(otherExt.getKeyConflictContext());
    }

    default boolean isActiveAndMatches(InputUtil.Key keyCode) {
        return isConflictContextAndModifierActive() && getKey().equals(keyCode);
    }

    default void setToDefault() {
        setKeyModifierAndCode(getKeyModifierDefault(), getKeyBinding().getDefaultKey());
    }

    default KeyBinding getKeyBinding() {
        return (KeyBinding) this;
    }

    default void press() {
    }

    static IKeyConflictContext defaultContext() {
        return KeyConflictContext.UNIVERSAL;
    }
}
