package me.kirillirik.util;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.system.MemoryUtil.memSlice;

public final class FileUtils {

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        final var path = Paths.get(resource);

        if (Files.isReadable(path)) {
            try (final var fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) {}
            }
        } else {
            try {
                final var source = FileUtils.class.getClassLoader().getResourceAsStream(resource);
                if (source == null) {
                    throw new Exception("Null input " + resource);
                }

                final var rbc = Channels.newChannel(source);
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (rbc.read(buffer) != -1) {
                    if (buffer.remaining() != 0) continue;

                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                }
            } catch (Exception e) {
                throw new IOException("Bad file loading " + resource);
            }
        }

        return memSlice(buffer.flip());
    }

    @NotNull
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        final var newBuffer = BufferUtils.createByteBuffer(newCapacity);
        newBuffer.put(buffer.flip());
        return newBuffer;
    }
}
