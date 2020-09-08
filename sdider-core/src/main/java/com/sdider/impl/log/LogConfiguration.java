package com.sdider.impl.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.net.URI;

/**
 *
 * @author yujiaxin
 */
public class LogConfiguration {
    private static final Config CONFIG = new Config();
    private LogConfiguration() {}

    public static Config createConfig() {
        return new Config();
    }

    public static void configure(Config config) {
        CONFIG.setConfigName(config.getConfigName());
        CONFIG.setFileName(config.getFileName());
        CONFIG.setStatus(config.getStatus());
        CONFIG.setPattern(config.getPattern());
        CONFIG.setLevel(config.getLevel());
        if (config.getConfigLocation() != null) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.setConfigLocation(config.getConfigLocation());
        }
    }

    public static String getConfigName() {
        return CONFIG.getConfigName();
    }

    public static String getStatus() {
        return CONFIG.getStatus();
    }

    public static String getPattern() {
        return CONFIG.getPattern();
    }

    public static String getLevel() {
        return CONFIG.getLevel();
    }

    public static String getFileName() {
        return CONFIG.getFileName();
    }

    public static class Config {
        private String configName;
        private String status;
        private String pattern;
        private String level;
        private String fileName;
        private URI configLocation;
        private Config() {}

        public URI getConfigLocation() {
            return configLocation;
        }

        public void setConfigLocation(URI configLocation) {
            this.configLocation = configLocation;
        }

        public String getConfigName() {
            return configName;
        }

        public void setConfigName(String configName) {
            this.configName = configName;
        }

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

        public void configName(String configName) {
            setConfigName(configName);
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

}
