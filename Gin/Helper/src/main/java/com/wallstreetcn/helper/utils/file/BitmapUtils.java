package com.wallstreetcn.helper.utils.file;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.view.View;

import com.wallstreetcn.helper.utils.UtilsContextManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zhangyang on 16/4/21.
 */
public class BitmapUtils {
    public static Bitmap imageZoom(Bitmap bitmap) {
        //图片允许最大空间   单位：KB
        double maxSize = 500.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            try {
                bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i),
                        bitmap.getHeight() / Math.sqrt(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    /***
     * 图片的缩放方法
     *
     * @param bigImage  ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bigImage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bigImage.getWidth();
        float height = bigImage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bigImage, 0, 0, (int) width, (int) height, matrix, true);
    }

    public static void saveImage(Bitmap bitmap, String destPath, int quality) {
        try {
            FileUtil.deleteFile(destPath);
            if (FileUtil.createFile(destPath)) {
                FileOutputStream out = new FileOutputStream(destPath);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                    out.flush();
                    out.close();
                }
            }
            // 保存后要扫描一下文件，及时更新到系统目录（一定要加绝对路径，这样才能更新）
            try {
                File file = new File(destPath);
                Context context = UtilsContextManager.getInstance().getApplication();
                MediaScannerConnection.scanFile(UtilsContextManager.getInstance().getApplication(),
                        new String[]{file.getAbsolutePath()}, null, null);
                // 最后通知图库更新
                Uri contentUri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getBase64(byte[] bytes) {
        return new String(Base64.encode(bytes, Base64.DEFAULT));
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        quality = quality > 100 ? 100 : quality;
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return baos.toByteArray();
    }

    public static String getBitmapBase64(Bitmap bm, int quality) {
        return getBase64(Bitmap2Bytes(bm, quality));
    }


    private static Context getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }

    /**
     * 保存到指定目录，但能立即更新到系统相册中（红米2）
     *
     * @param context    上下文环境
     * @param faceBitmap 位图资源
     * @return 保存图片的路径
     */
    public static final String TAG = BitmapUtils.class.getSimpleName();


    public static Bitmap takeScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Resources resources = UtilsContextManager.getInstance().getApplication().getResources();
        BitmapFactory.decodeResource(resources, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, resId, options);
    }

    public static Bitmap blurBitmap(Context context, int radius, Bitmap bitmap) {
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript script = RenderScript.create(context);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(script, Element.U8_4(script));
        Allocation in = Allocation.createFromBitmap(script, bitmap);
        Allocation out = Allocation.createFromBitmap(script, result);
        blur.setRadius(radius);
        blur.setInput(in);
        blur.forEach(out);
        out.copyTo(result);
        script.destroy();
        return result;
    }

    public static Bitmap roundBitmapByXfermode(Bitmap bitmap, int outWidth, int outHeight, int radius) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        // 等比例缩放拉伸
        float widthScale = outWidth * 1.0f / bitmap.getWidth();
        float heightScale = outHeight * 1.0f / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        Bitmap newBt = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawARGB(0, 0, 0, 0);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        RectF rectF = new RectF(0f, 0f, (float) outWidth, (float) outHeight);

        // 在画布上绘制圆角图
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // 设置叠加模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 在画布上绘制原图片
        Rect ret = new Rect(0, 0, outWidth, outHeight);
        canvas.drawBitmap(newBt, ret, ret, paint);

        return targetBitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int cornerRadius) {
        if (bitmap == null) {
            return null;
        }

        // 创建一个新的Bitmap对象，用于绘制圆角
        Bitmap roundedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 创建Canvas对象，用于在新的Bitmap上绘制
        Canvas canvas = new Canvas(roundedBitmap);

        // 创建BitmapShader，用于绘制Bitmap
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // 创建Paint对象，设置Shader
        Paint paint = new Paint();
        paint.setShader(shader);

        // 创建RectF对象，用于定义圆角矩形的边界
        RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 绘制圆角矩形
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
        return roundedBitmap;
    }
}
