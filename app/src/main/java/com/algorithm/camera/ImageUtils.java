package com.algorithm.camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImageUtils {
    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_ALGORRITHM = 2;

    public static List list = new ArrayList<>();

//    将输入的 Bitmap 对象和饱和度值作为参数，并返回调整后的 Bitmap 对象。算法首先获取输入 Bitmap 的像素数组，
//    然后对每个像素进行饱和度调整。调整过程中，算法计算每个像素的 RGB 值，并计算其饱和度。然后，算法将饱和度调整为指定的值，
//    并将 RGB 值更新为新的值。最后，算法将更新后的像素数组设置为输出 Bitmap 的像素数组，并返回输出 Bitmap。
    public static Bitmap adjustSaturation(Bitmap bitmap, float saturation) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int pixelCount = width * height;
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixelCount; i++) {
            int r = (pixels[i] >> 16) & 0xFF;
            int g = (pixels[i] >> 8) & 0xFF;
            int b = pixels[i] & 0xFF;

            float newSaturation = Math.min(1f, Math.max(0f, (1f - (saturation - 0.5f) * 2f)));
            float delta = newSaturation - ((r + g + b) / 3f) / 255f;

            r += Math.round(delta * 255f);
            g += Math.round(delta * 255f);
            b += Math.round(delta * 255f);

            pixels[i] = 0xFF000000 | (r << 16) | (g << 8) | b;
        }

        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return outputBitmap;
    }

    public static Bitmap adjustedHue(Bitmap o, float deg)
    {
        Bitmap srca = o;
        Bitmap bitmap = srca.copy(Bitmap.Config.ARGB_8888, true);
        for(int x = 0;x < bitmap.getWidth();x++)
            for(int y = 0;y < bitmap.getHeight();y++){
                int newPixel = hueChange(bitmap.getPixel(x,y),deg);
                bitmap.setPixel(x, y, newPixel);
            }

        return bitmap;
    }
    public static int hueChange(int startpixel, float deg){
        float[] hsv = new float[3];       //array to store HSV values
        Color.colorToHSV(startpixel,hsv); //get original HSV values of pixel
        hsv[0]=hsv[0]+deg;                //add the shift to the HUE of HSV array
        hsv[0]=hsv[0]%360;                //confines hue to values:[0,360]
        return Color.HSVToColor(Color.alpha(startpixel),hsv);
    }

//    该算法将输入的 Bitmap 对象和亮度值作为参数，并返回调整后的 Bitmap 对象。
//    算法首先获取输入 Bitmap 的像素数组，然后对每个像素进行亮度调整。
//    调整过程中，算法将每个像素的 RGB 值与亮度值相加，并将结果限制在 0 到 255 的范围内。
//    最后，算法将更新后的像素数组设置为输出 Bitmap 的像素数组，并返回输出 Bitmap。
    public static Bitmap adjustBrightness(Bitmap bitmap, float brightness) {
        brightness = brightness * 255;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int pixelCount = width * height;
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixelCount; i++) {
            int r = (pixels[i] >> 16) & 0xFF;
            int g = (pixels[i] >> 8) & 0xFF;
            int b = pixels[i] & 0xFF;

            int newR = (int)Math.min(255, Math.max(0, r + brightness));
            int newG = (int)Math.min(255, Math.max(0, g + brightness));
            int newB = (int)Math.min(255, Math.max(0, b + brightness));

            pixels[i] = 0xFF000000 | (newR << 16) | (newG << 8) | newB;
        }

        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return outputBitmap;
    }

//    该算法将输入的 Bitmap 对象和 gamma 值作为参数，并返回应用 gamma 校正后的 Bitmap 对象。
//    算法首先获取输入 Bitmap 的像素数组，然后对每个像素进行 gamma 校正。
//    校正过程中，算法将每个像素的 RGB 值除以 gamma 的平方根的倒数，以实现 gamma 校正。
//    最后，算法将更新后的像素数组设置为输出 Bitmap 的像素数组，并返回输出 Bitmap。
    public static Bitmap applyGammaCorrection(Bitmap bitmap, float gamma) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int pixelCount = width * height;
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixelCount; i++) {
            int r = (pixels[i] >> 16) & 0xFF;
            int g = (pixels[i] >> 8) & 0xFF;
            int b = pixels[i] & 0xFF;

            r = (int)Math.min(255, Math.max(0, (float) r / (1.0f / Math.pow(gamma, 2.2f))));
            g = (int)Math.min(255, Math.max(0, (float) g / (1.0f / Math.pow(gamma, 2.2f))));
            b = (int)Math.min(255, Math.max(0, (float) b / (1.0f / Math.pow(gamma, 2.2f))));

            pixels[i] = 0xFF000000 | (r << 16) | (g << 8) | b;
        }

        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return outputBitmap;
    }

//使用灰度图像直方图均衡化来增强图像对比度。它将输入图像转换为灰度图像，计算灰度直方图，计算累计直方图，
// 计算均衡化的LUT（查找表），并将输入图像上的每个像素映射到均衡化的LUT中。最后，它返回增强的位图。
    public static Bitmap equalize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;

        int[] pixels = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] histogram = new int[256];
        int[] cumHistogram = new int[256];
        int[] equalizedLUT = new int[256];

        for (int i = 0; i < size; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            int gray = (int) (r * 0.3 + g * 0.59 + b * 0.11);
            histogram[gray]++;
        }

        int sum = 0;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < histogram.length; i++) {
            sum += histogram[i];
            cumHistogram[i] = sum;
            if (cumHistogram[i] < minValue && histogram[i] > 0) {
                minValue = cumHistogram[i];
            }
        }

        int totalLevels = 256;
        int normalized = totalLevels - 1;

        for (int i = 0; i < histogram.length; i++) {
            equalizedLUT[i] = (int) (((float) cumHistogram[i] - minValue) / ((float) size - minValue) * normalized);
        }

        for (int i = 0; i < size; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            int gray = (int) (r * 0.3 + g * 0.59 + b * 0.11);
            int equalized = equalizedLUT[gray];

            r = Math.min(Math.max(equalized, 0), 255);
            g = Math.min(Math.max(equalized, 0), 255);
            b = Math.min(Math.max(equalized, 0), 255);

            pixels[i] = Color.rgb(r, g, b);
        }

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, width, 0, 0, width, height);

        return result;
    }


    //    用Sobel算子来进行图像锐化。它将输入图像的每个像素与Sobel算子进行卷积，
//    然后将结果像素值作为输入图像的像素值。这个算法可以增强边缘和细节，从而使图像更加清晰。
//    直方图均衡
    public static Bitmap applyHistogramEqualization(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;

        int[] pixels = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] histogram = new int[256];
        int[] cumHistogram = new int[256];
        int[] equalizedLUT = new int[256];

        for (int i = 0; i < size; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            int gray = (int) (r * 0.3 + g * 0.59 + b * 0.11);
            histogram[gray]++;
        }

        int sum = 0;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < histogram.length; i++) {
            sum += histogram[i];
            cumHistogram[i] = sum;
            if (cumHistogram[i] < minValue && histogram[i] > 0) {
                minValue = cumHistogram[i];
            }
        }

        int totalLevels = 256;
        int normalized = totalLevels - 1;

        for (int i = 0; i < histogram.length; i++) {
            equalizedLUT[i] = (int) (((float) cumHistogram[i] - minValue) / ((float) size - minValue) * normalized);
        }

        for (int i = 0; i < size; i++) {
            int color = pixels[i];
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            int gray = (int) (r * 0.3 + g * 0.59 + b * 0.11);
            int equalized = equalizedLUT[gray];

            r = Math.min(Math.max(equalized, 0), 255);
            g = Math.min(Math.max(equalized, 0), 255);
            b = Math.min(Math.max(equalized, 0), 255);

            pixels[i] = Color.rgb(r, g, b);
        }

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, width, 0, 0, width, height);

        return result;
    }


    public static Bitmap applyHistogramEqualization1(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap processedImage = Bitmap.createBitmap(width, height, bitmap.getConfig());

        int A = 0,R,G,B;
        int pixel;
        float[][] Y = new float[width][height];
        float[][] U = new float[width][height];
        float[][] V = new float [width][height];
        int [] histogram = new int[256];
        Arrays.fill(histogram, 0);

        int [] cdf = new int[256];
        Arrays.fill(cdf, 0);
        float min = 257;
        float max = 0;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = bitmap.getPixel(x, y);
                //Log.i("TEST","("+x+","+y+")");
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                Y[x][y] = 0.299f * R + 0.587f * G + 0.114f * B;
                U[x][y] = 0.565f * (B-Y[x][y]);
                V[x][y] = 0.713f * (R-Y[x][y]);
                // create a histogram
                histogram[(int) Y[x][y]]+=1;
                // get min and max values
                if (Y[x][y] < min){
                    min = Y[x][y];
                }
                if (Y[x][y] > max){
                    max = Y[x][y];
                }
            }
        }

        cdf[0] = histogram[0];
        for (int i=1;i<=255;i++){
            cdf[i] = cdf[i-1] + histogram[i];
        }

        float minCDF = cdf[(int)min];
        float denominator = width*height - minCDF;
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = bitmap.getPixel(x, y);
                A = Color.alpha(pixel);
                Y[x][y] = ((cdf[ (int) Y[x][y]] - minCDF)/(denominator)) * 255;

                R = minMaxCalc(Y[x][y] + 1.140f * V[x][y]);
                G = minMaxCalc (Y[x][y] - 0.344f * U[x][y] - 0.714f * V[x][y]);
                B = minMaxCalc (Y[x][y] + 1.77f * U[x][y]);

                processedImage.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return processedImage;
    }

    public static int minMaxCalc(float input) {
        return (int)Math.min(Math.max(input, 0), 255.f);
    }

    public static Bitmap medianFilter(Bitmap inputBitmap) {
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        int filterRadius = 3; // 中值滤波半径

        // 遍历图像的每个像素
        for (int y = 0; y < outputBitmap.getHeight(); y++) {
            for (int x = 0; x < outputBitmap.getWidth(); x++) {
                int pixelIndex = y * outputBitmap.getWidth() + x;

                // 获取当前像素周围的像素点
                ArrayList<Integer> pixelValues = new ArrayList<Integer>();
                for (int i = y - filterRadius; i <= y + filterRadius; i++) {
                    for (int j = x - filterRadius; j <= x + filterRadius; j++) {
                        if (i >= 0 && i < outputBitmap.getHeight() && j >= 0 && j < outputBitmap.getWidth()) {
                            pixelValues.add(inputBitmap.getPixel(j, i));
                        }
                    }
                }

                // 对像素点进行排序并计算中值
                Collections.sort(pixelValues);
                int medianValue = pixelValues.get(pixelValues.size() / 2);

                // 将中值赋值给输出图像的当前像素
                outputBitmap.setPixel(x, y, medianValue);
            }
        }

        return outputBitmap;
    }


}
