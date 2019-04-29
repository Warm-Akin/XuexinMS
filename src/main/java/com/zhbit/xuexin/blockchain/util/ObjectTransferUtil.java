package com.zhbit.xuexin.blockchain.util;

import com.zhbit.xuexin.model.Company;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectTransferUtil {

    public String[] convertToStringArray(Company object) {
        String[] result = null;
        Field[] fields = object.getClass().getDeclaredFields();
        int fieldLength = fields.length;
        result = new String[fieldLength];
        for (int i = 0; i < fieldLength; i++) {
            result[i] = getFieldValueByName(fields[i].getName(), object);
        }
        System.out.println(result);
        return result;
    }

    private String getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            String value = method.invoke(o, new Object[] {}).toString();
            return value;
        } catch (Exception e) {
            return "";
        }
    }

}
