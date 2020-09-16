package com.sdider.impl.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;

import java.util.Map;


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
        if (rootNode.hasChildren() && rootNode.getChildren().get(0).getName().equalsIgnoreCase("Properties")) {
            final Node first = rootNode.getChildren().get(0);
            for (Node child : first.getChildren()) {
                Map<String, String> attrs = child.getAttributes();
                String value = null;
                switch(attrs.get("name")) {
                    case "fileName":
                        value =  LogConfiguration.getFileName();
                        break;
                    case "level":
                        value = LogConfiguration.getLevel();
                        break;
                    case "pattern":
                        value = LogConfiguration.getPattern();
                        break;
                }
                if (value != null) {
                    attrs.put("value", value);
                }
            }
        }
    }
}
