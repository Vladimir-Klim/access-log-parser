import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private List<LogEntry> entries;

    public Statistics() {
        this.totalTraffic = 0;
        this.entries = new ArrayList<>();
        this.minTime = LocalDateTime.MAX;
        this.maxTime = LocalDateTime.MIN;
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
}