package sdider.impl.request;

import com.sdider.SdiderRequest;
import com.sdider.utils.ClosureUtils;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

/**
 * @author yujiaxin
 */
public class DefaultRequestContainer extends AbstractRequestContainer {

    @Override
    public void request(@DelegatesTo(SdiderRequest.class)Closure<?> requestConfig) {
        SdiderRequest request = new DefaultRequestImpl();
        ClosureUtils.delegateRun(request, requestConfig);
        addRequest(request);
    }

    @Override
    public void request(String... urls) {
        for (String url : urls) {
            if (url == null || url.trim().equals("")){
                continue;
            }
            SdiderRequest request = new DefaultRequestImpl();
            request.GET(url);
            addRequest(request);
        }
    }
}
