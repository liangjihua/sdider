package sdider.impl.common;

import com.sdider.crawler.common.DynamicPropertiesObject;
import com.sdider.crawler.exception.NoSuchPropertyException;
import groovy.lang.GString;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link DynamicPropertiesObject}的默认实现。并且增加了以下特性：
 * 属性也可以被当作方法调用，用来设置该属性的值：</br>
 * <pre>
 * def object = new DefaultDynamicPropertiesObject()
 * object.name 'monica'
 * assert object.name == 'monica'
 * //这在将对象作为闭包代理时很有用
 * with(object) {
 *     name 'Monica'
 *     age 16
 *     husband 'Chandler'
 * }
 * </pre>
 * @see DynamicPropertiesObject
 * @author yujiaxin
 */
public final class DefaultDynamicPropertiesObject<V> extends GroovyObjectSupport implements DynamicPropertiesObject<V> {

    private volatile Map<String, V> propertiesContainer;

    @Override
    public boolean has(String name) {
        return getContainer().containsKey(name);
    }

    @Override
    public V get(String name) {
        Map<String, V> container = getContainer();
        if (!container.containsKey(name)) {
            throw new NoSuchPropertyException(name, this.getClass());
        }
        return container.get(name);
    }

    @Override
    public Object getProperty(String propertyName) {
        if (Objects.equals("properties", propertyName)) {
            return getProperties();
        }
        if (!has(propertyName)) {
            throw new MissingPropertyException(propertyName, this.getClass());
        }
        try {
            return get(propertyName);
        } catch (NoSuchPropertyException e) {
            throw new MissingPropertyException(propertyName, this.getClass());
        }
    }

    @Override
    public void set(String name, V value) {
        getContainer().put(name, value);
    }

    @Override
    public Map<String, V> getProperties() {
        return propertiesContainer == null ? Collections.emptyMap() : new HashMap<>(propertiesContainer);
    }

    private Map<String, V> getContainer() {
        if (propertiesContainer == null) {
            synchronized(this) {
                if (propertiesContainer == null) {
                    propertiesContainer = new HashMap<>();
                }
            }
        }
        return propertiesContainer;
    }

    @SuppressWarnings("unchecked")
    public Object methodMissing(String name, Object args) {
        Object[] argsArray = (Object[]) args;
        if (argsArray != null && argsArray.length == 1) {
            Object temp = argsArray[0];
            if (temp instanceof GString) {
                temp = temp.toString();
            }
            set(name, (V) temp);
            return null;
        } else {
            throw new MissingMethodException(name, this.getClass(), argsArray);
        }
    }

    @Override
    public String toString() {
        return getContainer().toString();
    }
}
