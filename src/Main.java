import java.io.File;
import java.util.Scanner;

public class Main {
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
            }
        }
    }
}
