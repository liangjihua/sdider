package sdider.impl.response;

import com.sdider.ResponseConverter;
import com.sdider.SdiderResponse;
import com.sdider.crawler.Response;
import groovy.lang.GroovyObjectSupport;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author yujiaxin
 */
public class DefaultResponseConverter implements ResponseConverter {

    @Override
    public SdiderResponse convert(Response response) {
        DefaultResponse defaultResponse = new DefaultResponse(response);
        defaultResponse.getExtensions().add("css", new Css(defaultResponse));
        return defaultResponse;
    }

    private static class Css extends GroovyObjectSupport {
        private Document doc;
        private final DefaultResponse response;

        private Css(DefaultResponse response) {
            this.response = response;
        }

        @Override
        public Object invokeMethod(String name, Object args) {
            if (doc == null) {
                doc = Jsoup.parse(response.getText());
            }
            return InvokerHelper.invokeMethod(doc, name, args);
        }
    }
}
