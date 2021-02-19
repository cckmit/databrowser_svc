package com.csf.databrowser.extract;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CompanyHelper {

    public static <T> T getCompany(List<String> list, Class<T> obj, T resp){
        T instance = null;

        try {
            instance = obj.newInstance();
            for (String s : list) {
                // 首字母大写
                String replace = s.substring(0, 1).toUpperCase() + s.substring(1);
                //获得setter,getter方法
                Method setMethod = obj.getMethod("set" + replace, String.class);
                Method getMethod = obj.getMethod("get" + replace);

                //进行赋值
                String result = (String) getMethod.invoke(resp);
                if (StringUtils.isNotEmpty(result)){
                    setMethod.invoke(instance,result);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return instance;
    }


}
