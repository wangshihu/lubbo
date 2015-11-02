import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
/**
 * Created by benchu on 15/10/21.
 */
public class TestJson {


    public static void main(String[] st) {
        JsonEntry entry = new JsonEntry();
        entry.getMap().put("date",new Date());
        entry.getMap().put("entry", new JsonEntry());
        Object[] args = new Object[3];
        args[0]="123";
        args[1]=new Date();
        entry.setArgs(args);
       String str = JSON.toJSONString(entry);
        JsonEntry enjao = JSON.parseObject(str, JsonEntry.class);

        System.out.println(str);


    }
}
