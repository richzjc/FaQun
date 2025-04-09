package com.wallstreetcn.helper.utils.file;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions3.RxPermissionsNew;
import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.snack.MToastHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * 与文件相关的类,主要负责文件的读写
 */
public final class FileUtil {

    // ------------------------------ 手机系统相关 ------------------------------
    public static final String NEWLINE = System.getProperty("line.separator");// 系统的换行符
    public static final String APPROOT = "wallstreetcn";// 程序的根目录
    public static final String ASSERT_PATH = "file:///android_asset";//apk的assert目录
    public static final String RES_PATH = "file:///android_res";//apk的assert目录

    //----------------------------------存放文件的路径后缀------------------------------------
    public static final String CACHE_IMAGE_SUFFIX = File.separator + APPROOT + File.separator + "images" + File.separator;
    public static final String CACHE_FILE_SUFFIX = File.separator + APPROOT + File.separator + "files" + File.separator;
    public static final String CACHE_VOICE_SUFFIX = File.separator + APPROOT + File.separator + "voice" + File.separator;
    public static final String CACHE_MATERIAL_SUFFIX = File.separator + APPROOT + File.separator + "material" + File.separator;
    public static final String LOG_SUFFIX = File.separator + APPROOT + File.separator + "Log" + File.separator;

    // ------------------------------------数据的缓存目录-------------------------------------------------------
    public static String SDCARD_PATH;// SD卡路径
    public static String LOCAL_PATH;// 本地路径,即/data/data/目录下的程序私有目录
    public static String CURRENT_PATH = "";// 当前的路径,如果有SD卡的时候当前路径为SD卡，如果没有的话则为程序的私有目录

    static {
        init();
    }

    public static void init() {
        TLog.e("fileUtil", "fileUtil + init");
        File file = getApplication().getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
        if (file != null)
            SDCARD_PATH = file.getPath();
        LOCAL_PATH = getApplication().getCacheDir().getPath();// 本地路径,即/data/data/目录下的程序私有目录

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !TextUtils.isEmpty(SDCARD_PATH)) {
            CURRENT_PATH = SDCARD_PATH;
        } else {
            CURRENT_PATH = LOCAL_PATH;
        }
        TLog.i("FileUtil", CURRENT_PATH);
    }

    public static String getCacheParentFile() {
        return CURRENT_PATH + File.separator + APPROOT;
    }

    public static String getCacheFileSuffix() {
        return CURRENT_PATH + CACHE_FILE_SUFFIX;
    }

    public static String getCacheImageSuffix() {
        return CURRENT_PATH + CACHE_IMAGE_SUFFIX;
    }

    private static Context getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }


    public static File getPatchFile() {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "patch", "7zip.zip");
            if (file.exists())
                return file;
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 得到与当前存储路径相反的路径(当前为/data/data目录，则返回/sdcard目录;当前为/sdcard，则返回/data/data目录)
     *
     * @return
     */
    public static String getDiffPath() {
        if (CURRENT_PATH.equals(SDCARD_PATH)) {
            return LOCAL_PATH;
        }
        return SDCARD_PATH;
    }


    public static String getDiffPath(String pathIn) {
        return pathIn.replace(CURRENT_PATH, getDiffPath());
    }

    // ------------------------------------文件的相关方法--------------------------------------------
    public static boolean saveFile(String filepath, String content) {
        byte[] date = content.getBytes();
        return writeFile(filepath, date, 0, date.length);
    }

    /**
     * 将数据写入一个文件
     *
     * @param destFilePath 要创建的文件的路径
     * @param data         待写入的文件数据
     * @param startPos     起始偏移量
     * @param length       要写入的数据长度
     * @return 成功写入文件返回true, 失败返回false
     */
    public static boolean writeFile(String destFilePath, byte[] data, int startPos, int length) {
        try {
            if (!createFile(destFilePath)) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(destFilePath);
            fos.write(data, 0, data.length);
            fos.flush();
            if (null != fos) {
                fos.close();
                fos = null;
            }
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从一个输入流里写文件
     *
     * @param destFilePath 要创建的文件的路径
     * @param in           要读取的输入流
     * @return 写入成功返回true, 写入失败返回false
     */
    public static boolean writeFile(String destFilePath, InputStream in) {
        try {
            if (!createFile(destFilePath)) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(destFilePath);
            int readCount;
            int len = 1024;
            byte[] buffer = new byte[len];
            while ((readCount = in.read(buffer)) != -1) {
                fos.write(buffer, 0, readCount);
            }
            fos.flush();
            fos.close();
            in.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Bitmap addPigWatermark(Context context, Bitmap bitmap) {
        if (bitmap == null)
            return bitmap;
        Drawable drawable = context.getResources().getDrawable(R.drawable.app_water_mark);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        if (bd == null) return bitmap;
        final Bitmap mark = bd.getBitmap();
        if (mark == null) return bitmap;
        mark.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        int w = mark.getWidth();
        int h = mark.getHeight();
        int mBitmapWidth = bitmap.getWidth();
        int mBitmapHeight = bitmap.getHeight();
        Bitmap resize = null;
        if (mBitmapWidth / w < 2) {
            int newW = (int) (mBitmapWidth * 0.3);
            int newH = (int) (h * newW / w);
            w = newW;
            h = newH;
        }
        if (mBitmapHeight / h < 2) {
            int newH = (int) (mBitmapHeight * 0.3);
            int newW = (int) (w * newH / h);
            w = newW;
            h = newH;
        }
        resize = Bitmap.createScaledBitmap(mark, w, h, false);
        if (resize == null)
            resize = mark;
        Bitmap newBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(resize, mBitmapWidth - resize.getWidth() - 10, mBitmapHeight - resize.getHeight() - 10, null);
        return newBitmap;
    }

    public static void saveBitmapToCamera(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.helper_save_failure));
            return;
        }

        realSaveBitmap(context, bitmap);
    }

    private static void realSaveBitmap(Context context, Bitmap bitmap) {
        String fileName = null;
        String galleryPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = null;
        FileOutputStream outStream = null;

        try {
            file = new File(galleryPath, System.currentTimeMillis() + "");
            fileName = file.toString();
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            }

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        MToastHelper.showToast("保存成功");
    }


    /**
     * 读取文件，返回以byte数组形式的数据
     *
     * @param filePath 要读取的文件路径名
     * @return
     */
    public static byte[] readFile(String filePath) throws Exception {
        try {
            if (isFileExist(filePath)) {
                FileInputStream fi = new FileInputStream(filePath);
                return readInputStream(fi);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从一个数量流里读取数据,返回以byte数组形式的数据。
     * </br></br>
     * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，但如果是用于网络操作，就经常会遇到一些麻烦(available()方法的问题)。所以如果是网络流不应该使用这个方法。
     *
     * @param in 要读取的输入流
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream in) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] b = new byte[in.available()];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                os.write(b, 0, length);
            }

            b = os.toByteArray();

            in.close();
            in = null;

            os.close();
            os = null;

            return b;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将一个文件拷贝到另外一个地方
     *
     * @param sourceFile    源文件地址
     * @param destFile      目的地址
     * @param shouldOverlay 是否覆盖
     * @return
     */
    public static boolean copyFiles(String sourceFile, String destFile, boolean shouldOverlay) {
        try {
            if (shouldOverlay) {
                deleteFile(destFile);
            }
            FileInputStream fi = new FileInputStream(sourceFile);
            writeFile(destFile, fi);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 路径名
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建一个文件，创建成功返回true
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除一个文件
     *
     * @param filePath 要删除的文件路径名
     * @return true if this file was deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
     *
     * @param dir
     */
    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                deleteDirectory(listFiles[i]);
            }
        }
        dir.delete();
    }

    /**
     * 复制一个目录及其子目录、文件到另外一个目录
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    /**
     * @param context 图片路径-具体到文件
     * @param imgStr  base64编码字符
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(Context context, String imgStr) {
        if (TextUtils.isEmpty(imgStr))
            return false;
        try {
            byte[] decode = Base64.decode(imgStr, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            saveBitmapToCamera(context, bitmap);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}