package com.sdider.impl;

import com.sdider.Sdider;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * @author yujiaxin
 */
class SdiderScriptExecutor {
    private final GroovyShell gs;


    public SdiderScriptExecutor(Sdider sdider) {
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(BaseScript.class.getName());
        compilerConfiguration.setSourceEncoding("UTF-8");
        Binding binding = new Binding();
        binding.setVariable("sdider", sdider);
        gs = new GroovyShell(this.getClass().getClassLoader(), binding, compilerConfiguration);
    }

    public void inject(File scriptFile) throws IOException {
        Script script = gs.parse(scriptFile);
        script.run();
    }

    /**
     * 执行脚本。参数classpathUrl是从classpath查找的脚本路径
     * @param classpathUrl 脚本路径，classpath必须包含该路径
     */
    public void inject(String classpathUrl) throws IOException, URISyntaxException {
        Script script = gs.parse(Objects.requireNonNull(gs.getClassLoader().getResource(classpathUrl)).toURI());
        script.run();
    }
}
