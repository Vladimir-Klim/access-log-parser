public class UserAgent {
    private final String oS;
    private final String browser;

    public UserAgent(String userAgentString) {
        String os = extractOS(userAgentString);
        String browser = extractBrowser(userAgentString);
        this.oS = os;
        this.browser = browser;
    }

    private String extractOS(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac OS")) {
            return "macOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Unknown";
    }

    private String extractBrowser(String userAgent) {
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        }
        return "Other";
    }

    public String getOS() {
        return oS;
    }

    public String getBrowser() {
        return browser;
    }

    public boolean isBot() {
        return browser.toLowerCase().contains("bot");
    }
}