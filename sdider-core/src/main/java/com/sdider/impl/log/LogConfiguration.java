package sdider.impl.log;

/**
 *
 * @author yujiaxin
 */
public class LogConfiguration {
    public static final LogConfiguration DEFAULT_CONFIGURATION = new LogConfiguration();

    private String status;
    private String pattern;
    private String level;
    private String fileName;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void status(String status) {
        setStatus(status);
    }

    public void pattern(String pattern) {
        setPattern(pattern);
    }

    public void level(String level) {
        setLevel(level);
    }

    public void fileName(String fileName) {
        setFileName(fileName);
    }
}
