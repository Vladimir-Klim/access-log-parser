import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static class LineTooLongException extends RuntimeException {
        public LineTooLongException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        var PATH_ERROR = "Введен ошибочный путь к файлу или путь к несуществующему файлу.";
        var count = 0;
        var googleBotCount = 0;
        var yandexBotCount = 0;
        var totalRequests = 0;
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
                    while ((line = reader.readLine()) != null) {
                        totalRequests++;
                        if (line.length() > 1024) {
                            throw new LineTooLongException("Строка превышает максимальную длину в 1024 символа!");
                        }

                        String[] components = parseLogLine(line);

                        if (components != null) {
                            String userAgent = components[7];
                            String botProgram = extractBotProgram(userAgent);
                            if (botProgram != null) {
                                if (botProgram.equals("Googlebot")) {
                                    googleBotCount++;
                                } else if (botProgram.equals("YandexBot")) {
                                    yandexBotCount++;
                                }
                            }
                        }
                    }

                    System.out.printf("Доля запросов от Googlebot: %.2f%%%n", (googleBotCount * 100.0) / totalRequests);
                    System.out.printf("Доля запросов от YandexBot: %.2f%%%n", (yandexBotCount * 100.0) / totalRequests);

                    reader.close();

                } catch (LineTooLongException ex) {
                    System.err.println(ex.getMessage());
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static String[] parseLogLine(String line) {
        String regex = "(\\S+)\\s+" + "(\\S+\\s+\\S+)\\s+" + "\\[(.*?)\\]\\s+" + "\"(.*?)\"\\s+" + "(\\d+)\\s+" + "(\\d+)\\s+" + "\"(.*?)\"\\s+" + "\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            String[] parts = new String[8];
            parts[0] = matcher.group(1);
            parts[1] = matcher.group(2);
            parts[2] = matcher.group(3);
            parts[3] = matcher.group(4);
            parts[4] = matcher.group(5);
            parts[5] = matcher.group(6);
            parts[6] = matcher.group(7);
            parts[7] = matcher.group(8);
            return parts;
        }

        return null;
    }

    private static String extractBotProgram(String userAgent) {
        int startIdx = userAgent.indexOf('(');
        int endIdx = userAgent.indexOf(')');
        if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
            String firstBrackets = userAgent.substring(startIdx + 1, endIdx);
            String[] parts = firstBrackets.split(";");
            if (parts.length >= 2) {
                String fragment = parts[1].trim();
                String[] programParts = fragment.split("/");
                if (programParts.length > 1) {
                    return programParts[0].trim();
                }
            }
        }
        return null;
    }
}
