package Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserUtil {

    public static String generateUserId() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "M-" + sdf.format(new Date());
    }
}
