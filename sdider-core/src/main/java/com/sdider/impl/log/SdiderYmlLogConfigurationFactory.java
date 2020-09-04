package sdider.impl.log;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.plugins.Plugin;

/**
 *
 * @author yujiaxin
 */
@Plugin(name = "sdiderYmlLogConfigurationFactory", category = "ConfigurationFactory")
@Order(999)
public class SdiderYmlLogConfigurationFactory extends ConfigurationFactory {


    @Override
    protected String[] getSupportedTypes() {
        return new String[]{".yml", ".yaml"};
    }

    @Override
    public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
        return new SdiderYmlLogConfiguration(loggerContext, source);
    }
}
