import java.util.HashMap;
import java.util.Map;

/**
 * Created by benchu on 15/10/25.
 */
public class JsonEntry {
    private Map<String,Object> map = new HashMap<>();
    private Object[] args;

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
