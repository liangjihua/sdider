package com.sdider.impl.log;

import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.net.URI;

/**
 *
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public class LogConfigAction {
    public static final String CONFIGURATION_NAME = "configurationName";
    public static final String LOG_FILENAME = "logFilename";

    private Closure configAction;
    private URI log4j2Config;
    private final String configName;
    private final String logFile;

    public LogConfigAction(String defaultConfigName, String defaultLogFilename) {
        this.configName = defaultConfigName;
        this.logFile = defaultLogFilename;
    }

    public void setConfigAction(Closure loggerConfig) {
        configAction = loggerConfig;
    }

    public void setConfigLocation(URI configLocation) {
        this.log4j2Config = configLocation;
    }

    public void doConfigure() {
        if (configAction != null) {
            ClosureUtils.delegateRun(LogConfiguration.DEFAULT_CONFIGURATION, configAction);
        }
        if (log4j2Config != null) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.setConfigLocation(log4j2Config);
        } else {
            System.setProperty("configurationName", configName);
            System.setProperty("logFilename", logFile);
        }
    }
}
