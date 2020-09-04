package sdider.impl;

import com.sdider.crawler.Configuration;
import com.sdider.crawler.exception.NoSuchPropertyException;
import com.sdider.impl.common.DefaultDynamicPropertiesObject;
import com.sdider.impl.request.DefaultRequestConfigImpl;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author yujiaxin
 */
public class DefaultConfigurationImpl extends GroovyObjectSupport implements Configuration {
    private static final Logger logger = LoggerFactory.getLogger(DefaultConfigurationImpl.class);
    private final DefaultDynamicPropertiesObject<Object> properties = new DefaultDynamicPropertiesObject<>();

    @Override
    public boolean has(String name) {
        return properties.has(name);
    }

    @Override
    public Object get(String name) {
        try {
            return properties.get(name);
        } catch (NoSuchPropertyException ignored) {
            return null;
        }
    }

    @Override
    public void set(String name, Object value) {
        properties.set(name, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties.getProperties();
    }

    @SuppressWarnings("rawtypes")
    public void requests(Closure requestConfig) { //todo 这种隐式的自动配置的设计不够直观，非常不好，需要做出调整
        ClosureUtils.delegateRun(DefaultRequestConfigImpl.GLOBAL_REQUEST_CONFIG, requestConfig);
    }

    public Object methodMissing(String name, Object args) {
        return properties.methodMissing(name, args);
    }

    @Override
    public Object getProperty(String propertyName) {
        if ("properties".equals(propertyName)) {
            return getProperties();
        } else {
            if (has(propertyName)) {
                return get(propertyName);
            }
        }
        throw new MissingPropertyException(propertyName, this.getClass());
    }

    @Override
    public <T> void configure(T config) {
        Set<String> keySet = getProperties().keySet();
        for (String key : keySet) {
            setPropertySafe(config, key, get(key));
        }
    }

    private static void setPropertySafe(Object obj, String property, Object value) {
        try {
            InvokerHelper.setProperty(obj, property, value);
        } catch (MissingPropertyException mpe) {
            // Ignore
        }
    }

    @Override
    public <T> void configure(String name, T config) {
        if (!has(name)) {
            logger.warn("configure skipped: {'key':'{}', 'configObj':'{}', 'reason':'specified configuration key not exists'}", name, config);
            return;
        }
        Object value = get(name);
        if (value instanceof Closure) {
            ClosureUtils.delegateRun(config, (Closure<?>) value);
        } else {
            setPropertySafe(config, name, value);
        }
    }
}
