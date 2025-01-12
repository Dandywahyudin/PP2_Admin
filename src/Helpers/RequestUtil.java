package Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestUtil {
    public static String generateRequestId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "R-" + sdf.format(new Date());
    }
}