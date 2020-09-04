package sdider;

import groovy.lang.MissingPropertyException;

/**
 * 扩展容器
 * @author yujiaxin
 */
public interface ExtensionContainer {

    /**
     * 添加一个扩展
     * @param name 扩展名 not empty
     * @param extension 扩展对象 not null
     * @throws IllegalArgumentException 当该名称的扩展已存在时
     */
    void add(String name, Object extension) throws IllegalArgumentException;

    /**
     * @throws MissingPropertyException 当找不到给定名称的扩展时
     */
    Object getByName(String name) throws MissingPropertyException;

    boolean contains(String extensionName);
}
