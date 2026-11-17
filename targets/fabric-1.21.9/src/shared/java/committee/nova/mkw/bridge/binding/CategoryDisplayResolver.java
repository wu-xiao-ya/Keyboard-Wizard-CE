package committee.nova.mkw.bridge.binding;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class CategoryDisplayResolver {
    private CategoryDisplayResolver() {
    }

    public static Text resolve(String categoryId) {
        if (categoryId.startsWith("key.categories.")) {
            return Text.translatable(categoryId);
        }

        for (KeyBinding keyBinding : MinecraftClient.getInstance().options.allKeys) {
            KeyBinding.Category category = keyBinding.getCategory();
            Identifier id = category.id();
            if (!id.toString().equals(categoryId)) {
                continue;
            }

            String translatedCategory = category.getLabel().getString();
            if ("minecraft".equals(id.getNamespace())) {
                return Text.literal(translatedCategory);
            }

            String ownerName = FabricLoader.getInstance().getModContainer(id.getNamespace())
                    .map(mod -> mod.getMetadata().getName())
                    .orElse(id.getNamespace());
            return Text.literal(ownerName + " / " + translatedCategory);
        }

        return Text.literal(categoryId);
    }
}
