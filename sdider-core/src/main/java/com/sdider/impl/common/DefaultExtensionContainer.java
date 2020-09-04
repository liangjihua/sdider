package sdider.impl.common;

import com.sdider.ExtensionContainer;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yujiaxin
 */
public class DefaultExtensionContainer extends GroovyObjectSupport implements ExtensionContainer {

    private final Map<String, Object> extensions = new HashMap<>();

    @Override
    public void add(String name, Object extension) throws IllegalArgumentException {
        if (extension == null) {
            throw new NullPointerException("extension is null");
        }
        if (extensions.containsKey(name)) {
            throw new IllegalArgumentException(String.format("extension named %s already exists", name));
        }
        extensions.put(name, extension);
    }

    @Override
    public Object getByName(String name) throws MissingPropertyException {
        Object o = extensions.get(name);
        if (o == null) {
            throw new MissingPropertyException(name, this.getClass());
        }
        return o;
    }

    @Override
    public boolean contains(String extensionName) {
        return extensions.containsKey(extensionName);
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {
        add(propertyName, newValue);
    }

    @Override
    public Object getProperty(String propertyName) {
        return getByName(propertyName);
    }
}
