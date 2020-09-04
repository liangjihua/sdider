package sdider.main;

import com.sdider.impl.AbstractSdider;
import com.sdider.impl.DefaultSdider;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author yujiaxin
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("指定要运行的爬虫脚本");
            return;
        }
        String script = args[0];
        AbstractSdider sdider = new DefaultSdider(new File(script));
        sdider.execute();
    }
}
