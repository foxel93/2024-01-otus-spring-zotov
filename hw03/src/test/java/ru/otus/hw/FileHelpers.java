package ru.otus.hw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileHelpers {
    public static File createTempFileInResources(@NonNull String prefix, @Nullable String suffix) throws IOException {
        var resource = FileHelpers.class.getResource("/");
        if (resource == null) {
            throw new FileNotFoundException("Resource is not found");
        }
        var resourcePath = Path.of(resource.getPath());
        return File.createTempFile(prefix, suffix, resourcePath.toFile());
    }

    public static void tryDeleteFile(@Nullable File file) {
        if (file != null && file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }
}
