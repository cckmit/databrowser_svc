package com.csf.databrowser.extract;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

public class ExcelHelper {

    public static <T> void getExcel(List<String> list, Class<T> obj){
         T instance=null;

        try {
            for (String s : list) {
                //获取目标对象的属性值
                //Field field = instance.getClass().getDeclaredField(s);
                Field field = obj.getDeclaredField(s);
                //获取注解反射对象
                Excel excelAnnon = field.getAnnotation(Excel.class);
                //获取代理
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(excelAnnon);
                Field excelField = invocationHandler.getClass().getDeclaredField("memberValues");
                excelField.setAccessible(true);
                Map memberValues = (Map) excelField.get(invocationHandler);
                memberValues.put("isColumnHidden", false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
