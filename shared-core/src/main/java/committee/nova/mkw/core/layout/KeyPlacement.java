package committee.nova.mkw.core.layout;

/** Rectangle relative to the origin of a keyboard layout. */
public final class KeyPlacement {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final PhysicalKey key;

    public KeyPlacement(float x, float y, float width, float height, PhysicalKey key) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.key = key;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public PhysicalKey key() {
        return key;
    }
}
