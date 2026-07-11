package committee.nova.mkw.bridge.binding;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class CategoryDisplayResolver {
    private static final String VANILLA_CATEGORY_PREFIX = "key.categories.";

    private CategoryDisplayResolver() {
    }

    public static Text resolve(String categoryId) {
        if (categoryId == null || categoryId.isEmpty()) {
            return Text.literal("");
        }

        if (categoryId.startsWith(VANILLA_CATEGORY_PREFIX)) {
            Text translated = Text.translatable(categoryId);
            String categoryOwner = categoryId.substring(VANILLA_CATEGORY_PREFIX.length());
            int separator = categoryOwner.indexOf('.');
            if (separator >= 0) {
                categoryOwner = categoryOwner.substring(0, separator);
            }
            return FabricLoader.getInstance().getModContainer(categoryOwner)
                    .filter(mod -> !"minecraft".equals(mod.getMetadata().getId()))
                    .<Text>map(mod -> Text.literal(mod.getMetadata().getName() + " / " + translated.getString()))
                    .orElse(translated);
        }

        Text translated = Text.translatable(categoryId);
        Identifier id = Identifier.tryParse(categoryId);
        if (id == null || "minecraft".equals(id.getNamespace())) {
            return translated;
        }

        String ownerName = FabricLoader.getInstance().getModContainer(id.getNamespace())
                .map(mod -> mod.getMetadata().getName())
                .orElse(id.getNamespace());
        return Text.literal(ownerName + " / " + translated.getString());
    }
}
