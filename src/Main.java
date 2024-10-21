import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    // Последовательное копирование файлов
    public static void copyFileSequentially(String sourceFilePath, String destinationFilePath) throws IOException {
        Path sourcePath = new File(sourceFilePath).toPath();
        Path destinationPath = new File(destinationFilePath).toPath();
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    // Параллельное копирование файлов
    public static void copyFileParallel(String sourceFilePath, String destinationFilePath) throws IOException {
        Path sourcePath = new File(sourceFilePath).toPath();
        Path destinationPath = new File(destinationFilePath).toPath();
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) throws Exception {
        String sourceFilePath1 = "file1.txt";
        String destinationFilePath1 = "copy_file1.txt";

        String sourceFilePath2 = "file2.txt";
        String destinationFilePath2 = "copy_file2.txt";

        // Замер времени для последовательного копирования
        long timeSequential = System.currentTimeMillis();
        copyFileSequentially(sourceFilePath1, destinationFilePath1);
        copyFileSequentially(sourceFilePath2, destinationFilePath2);
        timeSequential = System.currentTimeMillis() - timeSequential;

        System.out.println("Последовательное копирование заняло " + timeSequential + " мс");

        // Замер времени для параллельного копирования
        ExecutorService executor = Executors.newFixedThreadPool(2);
        long timeParallel = System.currentTimeMillis();

        Future<?> job1 = executor.submit(() -> {
            try {
                copyFileParallel(sourceFilePath1, destinationFilePath1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Future<?> job2 = executor.submit(() -> {
            try {
                copyFileParallel(sourceFilePath2, destinationFilePath2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Ждем завершения обоих заданий
        job1.get();
        job2.get();

        timeParallel = System.currentTimeMillis() - timeParallel;

        executor.shutdown();

        System.out.println("Параллельное копирование заняло " + timeParallel + " мс");
    }
}
