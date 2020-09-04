package sdider;

import com.sdider.crawler.Item;
import com.sdider.crawler.Pipeline;
import com.sdider.crawler.PipelineFilter;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyObject;

/**
 * SdiderItem是请求响应被解析后产生数据项目。这些SdiderItem
 * 随后会被符合它们自身{@link #filter(Closure)}条件的{@link Pipeline}消费。
 * @author yujiaxin
 */
public interface SdiderItem extends GroovyObject, Item {

    /**
     * 添加一个{@link PipelineFilter}至此item中，给定闭包用于执行{@link PipelineFilter}的过滤动作，
     * 被过滤的{@link Pipeline}作为delegate传递给闭包执行。该闭包必须返回一个boolean值。
     * @param closure 用于过滤的闭包。
     */
    void filter(@DelegatesTo(Pipeline.class) Closure<Boolean> closure);

    /**
     * 返回自身，这在嵌套的闭包中比较有用，可以明确调用SdiderItem的方法和属性。
     * @return 自身
     */
    default SdiderItem getItem() {
        return this;
    }
}
