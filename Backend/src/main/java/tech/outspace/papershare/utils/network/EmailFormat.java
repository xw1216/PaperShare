package tech.outspace.papershare.utils.network;

import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class EmailFormat {
    private static final String emailPattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    public static boolean checkEmailFormat(String email) {
        return Pattern.matches(emailPattern, email);
    }

    public static String emailContGen(String code, String mailNo, Date date) {
        return "您注册 PaperShare 的验证码为：\n\n" +
                "\t" + code + "\n\n" +
                "有效期：20 分钟\n" +
                "发送时间：" + date + "\n" +
                "邮件编号：" + mailNo;
    }

    public static String genCheckCode() {
        Random random = new Random();
        int checkCode = (random.nextInt(900000) + 100000);
        return Integer.toString(checkCode);
    }

}
