package com.sdider.api.exception;

/**
 * 动态属性不存在时抛出此异常。
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public class NoSuchPropertyException extends RuntimeException{
    private final String property;
    private final Class type;

    public NoSuchPropertyException(String property, Class type) {
        super(createMessage(property, type));
        this.property = property;
        this.type = type;
    }

    public NoSuchPropertyException(String property, Class type, Throwable t) {
        super(createMessage(property, type), t);
        this.property = property;
        this.type = type;
    }

    private static String createMessage(String property, Class type) {
        return String.format("No such property '%s' on class %s ", property, type);
    }

    public String getProperty() {
        return property;
    }

    public Class getType() {
        return type;
    }
}
