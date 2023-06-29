package com.algorithm.camera;

import static com.algorithm.camera.ImageUtils.SELECT_ALGORRITHM;
import static com.algorithm.camera.ImageUtils.TAKE_PHOTO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private AlgorithmBean curentBean;
    private Button algorithmBtn;
    private Button takePhoto;
    private ImageView picture1, picture2;

    Uri imageUri;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhoto = (Button) findViewById(R.id.btn_take_photo);
        algorithmBtn = (Button) findViewById(R.id.btn_select_algorithm);
        picture1 = (ImageView) findViewById(R.id.picture1);
        picture2 = (ImageView) findViewById(R.id.picture2);

        ImageUtils.list.removeAll(ImageUtils.list);
        AlgorithmBean bean1 = new AlgorithmBean("饱和度", "adjustSaturation", 0.8f);
        ImageUtils.list.add(bean1);

        AlgorithmBean bean2 = new AlgorithmBean("色调", "adjustedHue", 0.8f);
        ImageUtils.list.add(bean2);

        AlgorithmBean bean3 = new AlgorithmBean("亮度", "adjustBrightness", 0.01f);
        ImageUtils.list.add(bean3);

        AlgorithmBean bean4 = new AlgorithmBean("gamma校正", "applyGammaCorrection", 0.91f);
        ImageUtils.list.add(bean4);

        AlgorithmBean bean5 = new AlgorithmBean("黑白直方图均衡", "applyHistogramEqualization");
        ImageUtils.list.add(bean5);

        AlgorithmBean bean6 = new AlgorithmBean("彩色直方图均衡", "applyHistogramEqualization1");
        ImageUtils.list.add(bean6);

        AlgorithmBean bean7 = new AlgorithmBean("中值滤波", "medianFilter");
        ImageUtils.list.add(bean7);

        curentBean = bean1;
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建file对象，用于存储拍照后的图片；
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "com.gyq.cameraalbumtest.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }

                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });


        picture1.setImageResource(R.drawable.dog);

        opererImage();
    }

    public void selectAlgorithmClick(View view) {
        //选择算法
        Intent intent = new Intent(this, SelectAlgorithmActivity.class);
        startActivityForResult(intent, SELECT_ALGORRITHM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture1.setImageBitmap(bm);
                        opererImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

            case SELECT_ALGORRITHM:
                Bundle bundle = data.getExtras();
                curentBean = (AlgorithmBean)bundle.getSerializable("algorithm");
                opererImage();
                break;
            default:
                break;
        }

    }

    private void opererImage(){
        algorithmBtn.setText("选择算法--" + curentBean.getName());
        BitmapDrawable bd = (BitmapDrawable) picture1.getDrawable();
        if (bd == null || bd.getBitmap() == null) {
            Toast.makeText(this, "请先拍照！", Toast.LENGTH_LONG).show();
            return;
        }
        LoadingDialog.getInstance(this).show();//显示

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(">>>>>>>>>>", Thread.currentThread().getName());
                try {
                    Method method;
                    Bitmap newImage;
                    if (curentBean.isHasParams()) {
                        method = ImageUtils.class.getMethod(curentBean.getMethod(), Bitmap.class, float.class);
                        newImage = (Bitmap)method.invoke(null, bd.getBitmap(), curentBean.getParams());
                    } else  {
                        method = ImageUtils.class.getMethod(curentBean.getMethod(), Bitmap.class);
                        newImage = (Bitmap)method.invoke(null, bd.getBitmap());
                    }

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //已在主线程中，可以更新UI
                            picture2.setImageBitmap(newImage);
                            LoadingDialog.getInstance(MainActivity.this).hide();//隐藏
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    public void selectPictureClick(View view) {
        BitmapDrawable bd = (BitmapDrawable) picture2.getDrawable();
        picture1.setImageBitmap(bd.getBitmap());
    }
}
