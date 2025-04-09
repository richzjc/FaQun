package com.wallstreetcn.helper.utils.hotfix;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class HotFixUtil {

    public static void hotFix(String pluginPath, Context context) {
        try {
            //宿主的dexElements
            Class cls = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = cls.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            ClassLoader loader = context.getClassLoader();
            Object pathList = pathListField.get(loader);
            Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            Object dexElements = dexElementsField.get(pathList);

            //  获取插件里面的dexElements
            Class plugincls = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pluginpathListField = plugincls.getDeclaredField("pathList");
            pluginpathListField.setAccessible(true);
            File fileDir = context.getDir("pluginDir", Context.MODE_PRIVATE);
            ClassLoader dexClassLoader = new DexClassLoader(pluginPath, fileDir.getAbsolutePath(), null, context.getClassLoader());
            Object pluginpathList = pluginpathListField.get(dexClassLoader);
            Field plugindexElementsField = pluginpathList.getClass().getDeclaredField("dexElements");
            plugindexElementsField.setAccessible(true);
            Object plugindexElements = plugindexElementsField.get(pathList);

            //合并dexElements
            int originLength = Array.getLength(dexElements);
            int pluginLength = Array.getLength(plugindexElements);
            int totalLength = originLength + pluginLength;
            Object newElements = Array.newInstance(dexElements.getClass().getComponentType(), totalLength);
            for(int i = 0; i < totalLength; i++){
                if(i < pluginLength){
                    Array.set(newElements, i, Array.get(plugindexElements, i));
                }else{
                    Array.set(newElements, i, Array.get(dexElements, i - pluginLength));
                }
            }

            //更新
            dexElementsField.set(pathList, newElements);

//            hotFixSource(pluginPath, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static void hotFixSource(String pluginPath, Context context) {
//        Resources resources;
//        AssetManager assetManager;
//    }
}
