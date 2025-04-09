package com.richzjc.dialoglib.util;

import java.lang.reflect.Field;

public class DialogReflectUtil {
    public static int getId(String defaultType, String resourceName, String nameSpace){
        try {
            if(resourceName.startsWith("R")){
                String[] spl = resourceName.split("\\.");
                if(spl != null && spl.length == 3){
                    return getRealId(spl[1], spl[2], nameSpace);
                }else{
                    return getRealId(defaultType, resourceName, nameSpace);
                }
            }else{
               return getRealId(defaultType, resourceName, nameSpace);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getRealId(String type, String resourceName, String nameSpace) throws Exception{
        StringBuilder builder = new StringBuilder();
        builder.append(nameSpace)
                .append(".R$")
                .append(type);
        Class cls = Class.forName(builder.toString());
        Field field = cls.getDeclaredField(resourceName);
        field.setAccessible(true);
        return (int) field.get(null);
    }
}
