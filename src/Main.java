import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;

public class Main {
    static class LineTooLongException extends RuntimeException {
        public LineTooLongException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        var PATH_ERROR = "Введен ошибочный путь к файлу или путь к несуществующему файлу.";
        var count = 0;
        while (true) {
            var path = new Scanner(System.in).nextLine();
            var file = new File(path);
            var fileExists = file.exists();
            var isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println(PATH_ERROR);
                continue;
            }
            if (isDirectory) {
                System.out.println(PATH_ERROR);
            } else {
                System.out.printf("Путь указан верно. Это файл номер %d%n", ++count);

                try {
                    FileReader fileReader = new FileReader(path);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line;
                    int totalLines = 0;
                    int maxLength = 0;
                    int minLength = Integer.MAX_VALUE;

                    while ((line = reader.readLine()) != null) {
                        totalLines++;
                        int length = line.length();

                        if (length > 1024) {
                            throw new LineTooLongException("В файле встретилась строка длиннее 1024 символов");
                        }
                        if (length > maxLength) {
                            maxLength = length;
                        }
                        if (length < minLength) {
                            minLength = length;
                        }
                    }
                    reader.close();

                    System.out.printf("Общее количество строк в файле: %d%n", totalLines);
                    System.out.printf("Длина самой длинной строки в файле: %d%n", maxLength);
                    System.out.printf("Длина самой короткой строки в файле: %d%n", minLength);

                } catch (LineTooLongException ex) {
                    System.err.println(ex.getMessage());
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
