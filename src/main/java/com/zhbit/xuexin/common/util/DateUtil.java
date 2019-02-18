package com.zhbit.xuexin.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date formatDate(String dateStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = dateFormat.parse(dateStr);
//            date.setDate(date.getDate() + 1);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
