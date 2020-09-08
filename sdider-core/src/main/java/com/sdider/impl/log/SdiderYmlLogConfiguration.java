package com.sdider.impl.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;

import java.util.Objects;


/**
 *
 * @author yujiaxin
 */
public class SdiderYmlLogConfiguration extends YamlConfiguration {

    public SdiderYmlLogConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
        super(loggerContext, configSource);
    }

    @Override
    protected Level getDefaultStatus() {
        if (LogConfiguration.getStatus() != null) {
            return Level.toLevel(LogConfiguration.getStatus(), null);
        }
        return super.getDefaultStatus();
    }

    @Override
    public void setup() {
        super.setup();
        if (LogConfiguration.getConfigName() != null) {
            rootNode.getAttributes().put("name", LogConfiguration.getConfigName());
        }
        if (LogConfiguration.getStatus() != null) {
            rootNode.getAttributes().put("status", LogConfiguration.getStatus());
        }
        if (LogConfiguration.getPattern() != null) {
            resetNodeValue(rootNode, "Pattern", LogConfiguration.getPattern());
        }
        if (LogConfiguration.getFileName() != null) {
            resetNodeValue(rootNode, "fileName", LogConfiguration.getFileName());
        }
        if (LogConfiguration.getLevel() != null) {
            resetNodeValue(rootNode, "level", LogConfiguration.getLevel());
        }
    }

    private void resetNodeValue(Node node, String propertyName, String newValue) {
        for (String key : node.getAttributes().keySet()) {
            if (Objects.equals(key, propertyName)) {
                node.getAttributes().put(key, newValue);
                return;
            }
        }
        for (Node child : node.getChildren()) {
            resetNodeValue(child, propertyName, newValue);
        }
    }

    @Override
    protected void doConfigure() {
        super.doConfigure();
    }
}
