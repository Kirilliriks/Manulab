package me.kirillirik.texture;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL42.*;

public final class Texture {

    public static final Vector2f[] DEFAULT_COORDS = new Vector2f[] {
                new Vector2f(0, 0),
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
    };

    private String name;

    private final int id;
    private final int width;
    private final int height;

    private final Pixmap pixmap;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        this.name = null;
        pixmap = new Pixmap(width, height);
        id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixmap.getPixels());
    }

    public Texture(ByteBuffer byteBuffer, int width, int height) {
        this(new Pixmap(byteBuffer, width, height));
    }

    public Texture(Pixmap pixmap) {
        this.pixmap = pixmap;
        width = pixmap.getWidth();
        height = pixmap.getHeight();
        name = null;
        id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixmap.getPixels());
    }

    private Texture(String filename) {
        this(new Pixmap(filename));
    }

    public static Texture create(String fileName) {
        return new Texture(fileName);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @NotNull
    public Pixmap getPixmap() {
        return pixmap;
    }

    public void bind(int offset) {
        glActiveTexture(GL_TEXTURE0 + offset);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void drawPixmap(@NotNull Pixmap drawPixmap, int x, int y) {
        bind();
        glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, drawPixmap.getWidth(), drawPixmap.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE,
                drawPixmap.getPixels());
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void destroy() {
        glDeleteTextures(id);
        pixmap.destroy();
    }
}
