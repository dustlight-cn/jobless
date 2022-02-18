package cn.dustlight.jobless.core;

public class Utils {
    public static String getSuffix(String input) {
        int index = input.indexOf('-');
        if (index > -1)
            return input.substring(index + 1);
        return input;
    }
}
