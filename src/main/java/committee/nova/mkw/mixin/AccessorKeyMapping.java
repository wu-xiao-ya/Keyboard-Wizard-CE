package committee.nova.mkw.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
    @Accessor("key")
    InputConstants.Key mkw$getKey();

    @Accessor("SORT_ORDER")
    static List<KeyMapping.Category> mkw$getCategorySortOrder() {
        throw new AssertionError();
    }
}
