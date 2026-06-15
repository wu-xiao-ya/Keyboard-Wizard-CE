package committee.nova.mkw.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(KeyMapping.class)
public interface AccessorKeyBinding {
    @Accessor("key")
    InputConstants.Key getBoundKey();

    @Accessor("CATEGORIES")
    static Set<String> getKeyCategories() {
        throw new AssertionError();
    }
}

