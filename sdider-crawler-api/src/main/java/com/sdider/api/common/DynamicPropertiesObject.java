package com.sdider.api.common;

import com.sdider.api.exception.NoSuchPropertyException;

import java.util.Map;

/**
 * 一个DynamicPropertiesObject可以动态的追加额外的属性，用于设置和读取。
 * @author yujiaxin
 */
public interface DynamicPropertiesObject<V> {

    /**
     * 返回此对象是否具有指定的属性
     * @param name 属性名
     * @return {@code true}如果此对象存在该属性（无论该属性的值是否为{@code null}），
     * {@code false}如果此对象不存在该属性
     */
    boolean has(String name);

    /**
     * 返回此对象指定{@code name}的属性的值。这个值可能为{@code null}，
     * 如果它被设置为null的话。若此对象不存在名称为{@code name}的属性，抛出异常。
     * @param name 属性名
     * @return 此对象上指定属性的值
     * @throws NoSuchPropertyException 如果此对象不包含名称为{@code name}的属性
     */
    V get(String name);

    /**
     * 在此对象上设置一个属性。
     * @param name 属性名
     * @param value 属性值 可为null
     */
    void set(String name, V value);
    /**
     * 返回在此对象上设置的所有属性作为一个Map。该Map是安全的，
     * 对该Map做出的任何修改都不会影响到源对象本身的属性。
     * @return 包含所有已设置属性的map，永远不会返回null
     */
    Map<String, V> getProperties();
}
