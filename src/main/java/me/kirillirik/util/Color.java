package me.kirillirik.util;

public class Color {

    public static final int MAX = 255;

    public static final Color WHITE = new Color(Color.MAX, Color.MAX, Color.MAX);
    public static final Color RED = new Color(Color.MAX, 0, 0);
    public static final Color GREEN = new Color(0, Color.MAX, 0);
    public static final Color BLUE = new Color(0, 0, Color.MAX);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color GRAY = new Color(128, 128, 128);
    public static final Color DARK_GRAY = new Color(64, 64, 64);
    public static final Color VOID = new Color(0, 0, 0, 0);

    public int r, g, b, a;

    public Color(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, MAX);
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void set(int r, int g, int b) {
        set(r, g, b, MAX);
    }

    public void set(Color color) {
        set(color.r, color.g, color.b, color.a);
    }

    public void set(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static void toColor(Color color, int value) {
        color.r = ((value & 0xff000000) >>> 24);
        color.g = ((value & 0x00ff0000) >>> 16);
        color.b = ((value & 0x0000ff00) >>> 8);
        color.a = ((value & 0x000000ff));
    }

    public static int fromRGBA(int r, int g, int b, int a) {
        return  (r << 24) +
                (g << 16) +
                (b << 8) +
                (a << 0);
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
