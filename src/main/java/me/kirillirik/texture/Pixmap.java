package me.kirillirik.texture;

import me.kirillirik.util.Color;
import me.kirillirik.util.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class Pixmap {

    private final ByteBuffer buffer;
    private final int width, height;

    public Pixmap(String filePath) {
        ByteBuffer imageBuffer;
        try {
            imageBuffer = FileUtils.ioResourceToByteBuffer(filePath, 8 * 1024);
        } catch (Exception exception) {
            throw new RuntimeException("Not found image " + filePath);
        }

        try (final var stack = stackPush()) {
            final var width = stack.mallocInt(1);
            final var height = stack.mallocInt(1);
            final var comp = stack.mallocInt(1);

            // Use info to read image metadata without decoding the entire image.
            // We don't need this for this demo, just testing the API.
            if (!stbi_info_from_memory(imageBuffer, width, height, comp)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            }

            // Decode the image
            this.width = width.get(0);
            this.height = height.get(0);
            buffer = stbi_load_from_memory(imageBuffer, width, height, comp, 4);
            if (buffer == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }
        }
    }

    public Pixmap(int width, int height) {
        buffer = BufferUtils.createByteBuffer(width * height * 4);
        this.width = width;
        this.height = height;
        clear();
    }

    public Pixmap(@NotNull ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void drawPixmap(@NotNull Pixmap pix, int xOwner, int yOwner) {
        drawPixmap(pix, xOwner, yOwner, 0, 0, pix.getWidth(), pix.getHeight());
    }

    public void drawPixmap(@NotNull Pixmap pix, int xOwner, int yOwner, int xPix, int yPix, int frameWidth, int frameHeight) {
        final var pixBuffer = pix.getPixels();

        for (int yP = 0; yP < frameHeight; yP++) {
            for (int xP = 0; xP < frameWidth; xP++) {
                final var drawIndex = (xPix + xP + (yPix + yP) * pix.width) * 4;
                if (drawIndex >= pixBuffer.limit() || drawIndex < 0) break;
                final var ownerIndex = (xOwner + xP + (yOwner + yP) * width) * 4;
                if (ownerIndex >= buffer.limit() || ownerIndex < 0) break;
                buffer.putInt(ownerIndex, pixBuffer.getInt(drawIndex));
            }
        }
    }

    public void clear() {
        buffer.clear();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                buffer.putInt(Color.fromRGBA(0, 0, 0, 0));
            }
        }
        buffer.flip();
    }

    @NotNull
    public ByteBuffer getPixels() {
        return buffer;
    }

    public void destroy() {
        buffer.clear();
    }
}
