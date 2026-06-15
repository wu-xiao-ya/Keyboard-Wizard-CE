package committee.nova.mkw.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
    @Accessor("key")
    InputConstants.Key mkw$getKey();

    @Accessor("CATEGORY_SORT_ORDER")
    static Map<String, Integer> mkw$getCategorySortOrder() {
        throw new AssertionError();
    }
}
