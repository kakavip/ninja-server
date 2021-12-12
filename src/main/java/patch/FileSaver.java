package patch;

import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.util.List;

public class FileSaver {
    @SneakyThrows
    public static void saveFileName(@NotNull String fileName, @NotNull List<@NotNull String> data) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            val content = "[" + String.join(",", data) + "]";
            fileOutputStream.write(content.getBytes());
        }
    }
}
