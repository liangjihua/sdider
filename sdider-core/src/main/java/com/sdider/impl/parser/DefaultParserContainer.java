package sdider.impl.parser;

import com.sdider.ParserContainer;
import com.sdider.crawler.ResponseParser;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author yujiaxin
 */
public class DefaultParserContainer extends GroovyObjectSupport implements ParserContainer {
    private final Map<String, ResponseParser> container = new HashMap<>();
    public static final String MAIN_PARSER_NAME = "_MAIN_PARSER";

    @Override
    public void add(String name, ResponseParser parser) throws IllegalArgumentException {
        if (container.containsKey(name)) {
            throw new IllegalArgumentException(String.format("parser named %s already exists", name));
        }
        container.put(name, parser);
    }

    @Override
    public ResponseParser getByName(String name) {
        return container.get(name);
    }

    @Override
    public ResponseParser getMainParser() {
        return container.get(MAIN_PARSER_NAME);
    }

    public void setMainParser(ResponseParser mainParser) {
        add(MAIN_PARSER_NAME, mainParser);
    }

    @Override
    public Object getProperty(String propertyName) {
        if (Objects.equals("mainParser", propertyName)) {
            return getMainParser();
        }
        ResponseParser parser = getByName(propertyName);
        if (parser == null) {
            throw new MissingPropertyException(propertyName, this.getClass());
        }
        return parser;
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {
        if (!(newValue instanceof ResponseParser) ) {
            throw new MissingPropertyException(propertyName, this.getClass());
        }
        add(propertyName, (ResponseParser) newValue);
    }
}
