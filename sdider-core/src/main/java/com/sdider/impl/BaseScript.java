package com.sdider.impl;


import com.sdider.Sdider;
import groovy.lang.Script;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 *
 * @author yujiaxin
 */
public abstract class BaseScript extends Script {
    private Sdider sdider;

    public Object propertyMissing(String name) {
        return sdider.getProperty(name);
    }

    public void propertyMissing(String name, Object arg) {
        sdider.setProperty(name, arg);
    }

    public Object methodMissing(String name, Object args) {
        return InvokerHelper.invokeMethod(sdider, name, args);
    }

    public abstract void scriptBody();

    @Override
    public Object run() {
        sdider = (Sdider)getBinding().getVariable("sdider");
        scriptBody();
        return sdider;
    }
}
