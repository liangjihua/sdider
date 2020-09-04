package sdider.impl.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;

import java.util.Objects;

import static com.sdider.impl.log.LogConfiguration.DEFAULT_CONFIGURATION;

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
        if (DEFAULT_CONFIGURATION.getStatus() != null) {
            return Level.toLevel(DEFAULT_CONFIGURATION.getStatus(), null);
        }
        return super.getDefaultStatus();
    }

    @Override
    public void setup() {
        super.setup();
        if (DEFAULT_CONFIGURATION.getPattern() != null) {
            resetNodeValue(rootNode, "Pattern", DEFAULT_CONFIGURATION.getPattern());
        }
        if (DEFAULT_CONFIGURATION.getFileName() != null) {
            resetNodeValue(rootNode, "fileName", DEFAULT_CONFIGURATION.getFileName());
        }
        if (DEFAULT_CONFIGURATION.getLevel() != null) {
            resetNodeValue(rootNode, "level", DEFAULT_CONFIGURATION.getLevel());
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
