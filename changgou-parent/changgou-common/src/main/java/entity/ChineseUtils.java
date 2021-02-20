package entity;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

/***
 *
 * @Author: Xu Rui
 * @Description:itheima
 *
 ****/
public class ChineseUtils {

    private static Random random = null;

    private static Random getRandomInstance() {
        if (random == null) {
            random = new Random(System.currentTimeMillis());
        }
        return random;
    }

    public static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = getRandomInstance();
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = 161 + Math.abs(random.nextInt(93));
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();
        try {
            str = new String(b, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getFixedLengthChinese(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = length; i > 0; i--) {
            str.append(ChineseUtils.getChinese());
        }
        return str.toString();
    }

    public static String getRandomLengthChiness(int start, int end) {
        StringBuilder str = new StringBuilder();
        int length = new Random().nextInt(end + 1);
        if (length < start) {
            str = new StringBuilder(getRandomLengthChiness(start, end));
        } else {
            for (int i = 0; i < length; i++) {
                str.append(getChinese());
            }
        }
        return str.toString();
    }

    public static void main(String[] args) {
        System.out.println(ChineseUtils.getChinese());
        System.out.println(ChineseUtils.getFixedLengthChinese(20));
        System.out.println(ChineseUtils.getRandomLengthChiness(2, 5));
    }
}
