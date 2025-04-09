package com.wallstreetcn.helper.utils.file;

import android.content.Context;
import android.content.res.AssetManager;

import com.wallstreetcn.helper.utils.UtilsContextManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by zhangyang on 16/4/29.
 */
public class CacheUtils {

    public static final String CACHE_RESOURCE = "resource";
    public static final String CACHE_TEMPLATE = "template";//清除缓存忽略

    private static long DEFAULT_CACHE_TIME = 1000 * 60 * 60;
    final static int BUFFER_SIZE = 4096;

    public static boolean isExpired(ICache iCache) {
        return isExpired(iCache, DEFAULT_CACHE_TIME);
    }

    public static boolean isExpired(ICache iCache, long cacheTime) {
        return System.currentTimeMillis() - iCache.getTimeMills() > cacheTime;
    }

    public static InputStream getFileFromAssets(String path) throws IOException {
        AssetManager manager = UtilsContextManager.getInstance().getApplication().getAssets();
        return manager.open(path);
    }

    public static String InputStreamToString(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }
        return new String(outStream.toByteArray(), "UTF-8");
    }


    public static boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        Context context = getApplication();
        try {
            fos = context.openFileOutput(file, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Serializable readObject(String file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Context context = getApplication();
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static Context getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }
}
