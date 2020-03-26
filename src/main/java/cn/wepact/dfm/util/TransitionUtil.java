package cn.wepact.dfm.util;

import java.util.Arrays;
import java.util.List;

public class TransitionUtil {

    public static List<String> stringToList(String strs){
        String[] str = strs.split(",");
        return Arrays.asList(str);

    }
}
