package committee.nova.mkw.keybinding;

import committee.nova.mkw.api.IKeyConflictContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public enum KeyConflictContext implements IKeyConflictContext {
    UNIVERSAL {
        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return true;
        }
    },
    GUI {
        @Override
        public boolean isActive() {
            return MinecraftClient.getInstance().currentScreen != null;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this || other == UNIVERSAL;
        }
    },
    IN_GAME {
        @Override
        public boolean isActive() {
            Screen screen = MinecraftClient.getInstance().currentScreen;
            return screen == null;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other == this || other == UNIVERSAL;
        }
    }
}
