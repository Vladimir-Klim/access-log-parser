import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    public enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD, TRACE, CONNECT
    }

    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent agent;

    public LogEntry(String logLine) {
        String[] components = parseLogLine(logLine);

        this.ipAddr = components[0];
        this.time = LocalDateTime.parse(components[1], DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        this.method = HttpMethod.valueOf(components[2].toUpperCase());
        this.path = components[3];
        this.responseCode = Integer.parseInt(components[4]);
        this.responseSize = Integer.parseInt(components[5]);
        this.referer = components[6];
        this.agent = new UserAgent(components[7]);
    }

    private String[] parseLogLine(String line) {
        String regex = "(\\S+)\\s+" + "(\\S+\\s+\\S+)\\s+" + "\\[(.*?)\\]\\s+" + "\"(.*?)\"\\s+" + "(\\d+)\\s+" + "(\\d+)\\s+" + "\"(.*?)\"\\s+" + "\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            String[] parts = new String[8];
            parts[0] = matcher.group(1);
            parts[1] = matcher.group(3);
            parts[2] = matcher.group(4).split(" ")[0];
            parts[3] = matcher.group(4).split(" ")[1];
            parts[4] = matcher.group(5);
            parts[5] = matcher.group(6);
            parts[6] = matcher.group(7);
            parts[7] = matcher.group(8);
            return parts;
        }
        return null;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getAgent() {
        return agent;
    }
}
