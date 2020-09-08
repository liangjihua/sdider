package com.sdider.impl;

import com.sdider.api.Configuration;
import com.sdider.impl.common.DefaultDynamicPropertiesObject;
import com.sdider.impl.log.Logger;
import com.sdider.util.Args;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.Map;
import java.util.Set;

/**
 * @author yujiaxin
 */
public class DefaultConfigurationImpl extends GroovyObjectSupport implements Configuration {
    private static final Logger logger = Logger.getInstance(DefaultConfigurationImpl.class);
    private final DefaultDynamicPropertiesObject<Object> properties = new DefaultDynamicPropertiesObject<>();

    @Override
    public boolean has(String name) {
        return properties.has(name);
    }

    @Override
    public Object get(String name) {
        return properties.get(name);
    }

    @Override
    public void set(String name, Object value) {
        properties.set(name, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties.getProperties();
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
    public <T> void configure(T configured) {
        Args.notNull(configured, "configured");
        Set<String> keySet = getProperties().keySet();
        for (String key : keySet) {
            setPropertySafe(configured, key, get(key));
        }
    }

    private static void setPropertySafe(Object obj, String property, Object value) {
        try {
            InvokerHelper.setProperty(obj, property, value);
        } catch (MissingPropertyException ignored) {
            // ignore missing property, because we use ConfigurationKey as property here.
        } catch (Exception exception) {
            logger.warn("set property fail: {'targetObject':'{}', 'property':'{}', 'value':'{}', 'reason':'{}'}",
                    obj, property, value, exception.getMessage(), exception);
        }
    }

    @Override
    public <T> void configure(String name, T configured) {
        Args.notNull(configured, "configured");
        if (!has(name)) {
            logger.warn("configure skipped: {'key':'{}', 'configObj':'{}'," +
                    " 'reason':'specified configuration key not exists'}", name, configured);
            return;
        }
        Object value = get(name);
        if (value instanceof Closure) {
            ClosureUtils.delegateRun(configured, (Closure<?>) value);
        } else {
            setPropertySafe(configured, name, value);
        }
    }
}
