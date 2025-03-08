import java.time.LocalDateTime;
import java.util.*;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private List<LogEntry> entries;
    private Set<String> existingPages;
    private Map<String, Integer> osFrequency;
    private Set<String> nonExistingPages;
    private Map<String, Integer> browserFrequency;
    private int errorRequestsCount;

    public Statistics() {
        this.totalTraffic = 0;
        this.entries = new ArrayList<>();
        this.minTime = LocalDateTime.MAX;
        this.maxTime = LocalDateTime.MIN;
        this.existingPages = new HashSet<>();
        this.osFrequency = new HashMap<>();
        this.nonExistingPages = new HashSet<>();
        this.browserFrequency = new HashMap<>();
        this.errorRequestsCount = 0;
    }

    public void addEntry(LogEntry entry) {
        this.entries.add(entry);
        this.totalTraffic += entry.getResponseSize();

        if (entry.getTime().isBefore(minTime)) {
            minTime = entry.getTime();
        }
        if (entry.getTime().isAfter(maxTime)) {
            maxTime = entry.getTime();
        }
        if (entry.getResponseCode() == 200) {
            existingPages.add(entry.getPath());
        }
        String os = entry.getAgent().getOS();
        osFrequency.put(os, osFrequency.getOrDefault(os, 0) + 1);

        if (entry.getResponseCode() == 404) {
            nonExistingPages.add(entry.getPath());
        }
        String browser = entry.getAgent().getBrowser();
        browserFrequency.put(browser, browserFrequency.getOrDefault(browser, 0) + 1);

        if (entry.getResponseCode() >= 400 && entry.getResponseCode() < 600) {
            errorRequestsCount++;
        }
    }

    public double getTrafficRate() {
        if (minTime.equals(LocalDateTime.MAX) || maxTime.equals(LocalDateTime.MIN)) {
            return 0.0;
        }
        long hoursBetween = java.time.Duration.between(minTime, maxTime).toHours();
        return hoursBetween == 0 ? totalTraffic : (double) totalTraffic / hoursBetween;
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public Set<String> getExistingPages() {
        return existingPages;
    }

    public Map<String, Double> getOsStatistics() {
        Map<String, Double> osStats = new HashMap<>();

        int totalOsCount = 0;
        for (int count : osFrequency.values()) {
            totalOsCount += count;
        }

        for (Map.Entry<String, Integer> entry : osFrequency.entrySet()) {
            double percentage = (double) entry.getValue() / totalOsCount;
            osStats.put(entry.getKey(), percentage);
        }

        return osStats;
    }

    public Set<String> getNonExistingPages() {
        return nonExistingPages;
    }

    public Map<String, Double> getBrowserStatistics() {
        Map<String, Double> browserStats = new HashMap<>();

        int totalBrowserCount = 0;
        for (int count : browserFrequency.values()) {
            totalBrowserCount += count;
        }

        for (Map.Entry<String, Integer> entry : browserFrequency.entrySet()) {
            double percentage = (double) entry.getValue() / totalBrowserCount;
            browserStats.put(entry.getKey(), percentage);
        }

        return browserStats;
    }

    public double getAverageVisitsPerHour() {
        long totalVisits = entries.stream()
                .filter(entry -> !entry.getAgent().isBot())
                .count();

        long hoursBetween = java.time.Duration.between(minTime, maxTime).toHours();
        return hoursBetween == 0 ? totalVisits : (double) totalVisits / hoursBetween;
    }

    public double getAverageErrorRequestsPerHour() {
        long hoursBetween = java.time.Duration.between(minTime, maxTime).toHours();
        if (hoursBetween == 0) return 0.0;

        return (double) errorRequestsCount / hoursBetween;
    }

    public double getAverageVisitsPerUser() {
        long uniqueUserCount = entries.stream()
                .filter(entry -> !entry.getAgent().isBot())
                .map(LogEntry::getIpAddr)
                .distinct()
                .count();

        long totalVisits = entries.stream()
                .filter(entry -> !entry.getAgent().isBot())
                .count();

        return uniqueUserCount == 0 ? 0.0 : (double) totalVisits / uniqueUserCount;
    }
}