package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;

public record KeyPlacement(float x, float y, float width, float height, int keyCode, InputConstants.Type inputType) {
}
