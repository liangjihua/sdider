package sdider;

import com.sdider.crawler.Response;
import com.sdider.crawler.ResponseParser;
import com.sdider.crawler.Result;

/**
 * ParserContainer实现了{@link ResponseParser}，管理一组命名的{@link ResponseParser}，
 * 在解析response时从中选取与request指定的parser相符合的parser，将实际的解析委托给它。
 * @author yujiaxin
 */
public interface ParserContainer extends ResponseParser {
    String PARSER_KEY = "parser";

    @Override
    default Result parse(Response response) {
        ResponseParser parser = null;
        Object customParser = null;
        if (response.getRequest().has(PARSER_KEY)) {
            customParser = response.getRequest().get(PARSER_KEY);
        }
        if (customParser != null) {
            if (customParser instanceof String) {
                parser = getByName(customParser.toString());
            } else if(customParser instanceof ResponseParser) {
                parser = (ResponseParser) customParser;
            }
        }
        if (parser == null) {
            parser = getMainParser();
        }
        return parser.parse(response);
    }

    /**
     * 添加一个命名为name的{@link ResponseParser}。
     * @param name name
     * @param parser parser
     * @throws IllegalArgumentException 已存在该name的parser时
     */
    void add(String name, ResponseParser parser) throws IllegalArgumentException;

    /**
     * 返回命名为name的{@link ResponseParser}
     * @param name name
     * @return {@link ResponseParser}，如果该name未注册，返回null
     */
    ResponseParser getByName(String name);

    /**
     * 返回main parser。main parser是默认的parser
     * @return main parser
     */
    ResponseParser getMainParser();
}
