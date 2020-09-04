package com.sdider;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MissingMethodException;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * Extensible指示一个对象可在运行时进行扩展。<b>对象扩展</b>，
 * 指的是在运行时为对象添加一些只读的额外功能属性，这些被添加的属性是一些普通
 * 对象。它们一旦被添加至扩展对象，扩展对象就可以直接使用这些属性：<br/>
 * <pre>
 *     //直接读取扩展属性
 *     def obj = //some Extensible obj
 *     obj.extensions.add('foo', 'bar')
 *     assert obj.foo == 'bar'
 *
 *     //使用扩展属性代理闭包
 *     class Foo {
 *         String bar
 *         Foo(String bar){
 *             this.bar = bar
 *         }
 *     }
 *     obj.extensions.add('foo', new Foo('bar'))
 *     obj.foo {
 *         assert bar == 'bar'
 *         bar = 'bar 2'
 *     }
 *
 *     //当扩展属性本身是一个闭包，直接执行该闭包
 *     obj.extension.add('foo', {"hello, bar"})
 *     assert 'hello, bar' == obj.foo()
 *     //闭包扩展拥有多个参数，所有参数都会被传递至闭包扩展
 *     obj.extension.add('foo', {name, clo -> clo(name) })
 *     assert 'hello, bar' == obj.foo('bar') {
 *         "hello, $it"
 *     }
 * </pre>
 * 扩展属性是只读的，不能被修改。<br/>
 * 一个扩展对象按照如下的顺序搜索属性：
 * <ul>
 *     <li>该对象自身的字段</li>
 *     <li>该对象类的Groovy元编程实现。例如propertyMissing()等方法</li>
 *     <li>该对象的扩展容器：{@link #getExtensions()}</li>
 * </ul>
 * @author yujiaxin
 */
@SuppressWarnings("rawtypes")
public interface Extensible extends GroovyObject {

    default Object propertyMissing(String name) {
        return getExtensions().getByName(name);
    }

    default Object methodMissing(String name, Object args) {
        Object[] argsArray = (Object[]) args;
        if (!getExtensions().contains(name)) {
            throw new MissingMethodException(name, this.getClass(), argsArray);
        }
        Object extension = getExtensions().getByName(name);
        if (extension instanceof Closure) {
            Closure cl = (Closure)extension;

            return (argsArray!= null && argsArray.length > 0) ? InvokerHelper.invokeClosure(cl, args) : cl.call();
        }
        if (argsArray.length == 1 && argsArray[0] instanceof Closure) {
            Closure closure = (Closure) argsArray[0];
            closure.setDelegate(extension);
            return closure.call(extension);
        }
        throw new MissingMethodException(name, this.getClass(), argsArray);
    }

    ExtensionContainer getExtensions();
}
