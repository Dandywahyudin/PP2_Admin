package Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CourierUtil {
    // Method untuk generate Courier ID otomatis
    public static String generateCourierId() {
        // Mendapatkan timestamp saat ini
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "C-" + sdf.format(new Date());
    }
}
