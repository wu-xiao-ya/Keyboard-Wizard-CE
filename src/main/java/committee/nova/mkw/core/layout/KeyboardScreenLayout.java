package committee.nova.mkw.core.layout;

public record KeyboardScreenLayout(
        Rect bindingList,
        Rect searchBar,
        Rect categorySelector,
        Rect mainLayoutButton,
        Rect numpadLayoutButton,
        Rect auxiliaryLayoutButton,
        Rect mouseMinusButton,
        Rect mouseKey,
        Rect mousePlusButton,
        Rect keyboard,
        Rect resetButton,
        Rect clearButton,
        Rect resetAllButton,
        Rect helpButton,
        Rect screenToggleButton
) {
    public record Rect(int x, int y, int width, int height) {
        public int right() {
            return this.x + this.width;
        }

        public int bottom() {
            return this.y + this.height;
        }

        public boolean overlaps(Rect other) {
            return this.x < other.right()
                    && this.right() > other.x
                    && this.y < other.bottom()
                    && this.bottom() > other.y;
        }
    }
}
