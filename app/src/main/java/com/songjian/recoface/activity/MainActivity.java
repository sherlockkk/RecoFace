package com.songjian.recoface.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.songjian.recoface.R;
import com.songjian.recoface.utils.BitmapUtil;
import com.songjian.recoface.utils.NetworkUtil;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAPTURE_CHOOSE = 1;
    private static final int PICTURE_CHOOSE = 2;

    private FloatingActionsMenu faMemu;
    private FloatingActionButton faButton_a;
    private FloatingActionButton faButton_b;
    private FloatingActionButton faButton_c;

    private ImageView imageView;
    private TextView textView;

    private Bitmap imageBitmap;

    private String age;
    private String gender = "";
    private String range;
    private String smile;
    private Dialog dialog;


    // 首先在您的Activity中添加如下成员变量
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

// 设置分享图片，参数2为本地图片的资源引用
//mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
// 设置分享图片，参数2为本地图片的路径(绝对路径)
//mController.setShareMedia(new UMImage(getActivity(),
//                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

// 设置分享音乐
//UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
//uMusic.setAuthor("GuGu");
//uMusic.setTitle("天籁之音");
// 设置音乐缩略图
//uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
//mController.setShareMedia(uMusic);

// 设置分享视频
//UMVideo umVideo = new UMVideo(
//          "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
// 设置视频缩略图
//umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
//umVideo.setTitle("友盟社会化分享!");
//mController.setShareMedia(umVideo);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isNetworkConnected();
        initWidget();

        umengSocialShare();

    }

    private void umengSocialShare() {

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();
        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());






        // 设置分享内容
        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");

        // 设置分享图片, 参数2为图片的url地址
        //mController.setShareMedia(new UMImage(this, "http://www.umeng.com/images/pic/banner_module_social.png"));

        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        mController.setShareMedia(new UMImage(this, BitmapFactory.decodeFile("/mnt/sdcard/test.png")));

    }

    // 判断是否连网
    private void isNetworkConnected() {
        // TODO Auto-generated method stub
        NetworkUtil networkUtil = new NetworkUtil();
        if (false == networkUtil.isNetworkConnectioned(this)) {
            Toast.makeText(MainActivity.this, "亲爱哒~~~需要网络才可以哟╮(╯▽╰)╭",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initWidget() {
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        faMemu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        faButton_a = (FloatingActionButton) findViewById(R.id.action_a);
        faButton_a.setImageResource(R.mipmap.button_a);
        faButton_b = (FloatingActionButton) findViewById(R.id.action_b);
        faButton_b.setImageResource(R.mipmap.button_b);
        faButton_c = (FloatingActionButton) findViewById(R.id.action_c);
        faButton_c.setImageResource(R.mipmap.button_c);
        registerListener();
    }

    private void registerListener() {
        faButton_a.setOnClickListener(this);
        faButton_b.setOnClickListener(this);
        faButton_c.setOnClickListener(this);
        faButton_c.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_a:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(),
                        "temp.jpg");
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAPTURE_CHOOSE);
                break;
            case R.id.action_b:
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, PICTURE_CHOOSE);
                break;
            case R.id.action_c:
                dialog = new Dialog(this);
                dialog.setTitle("分析中...");
                dialog.show();

                DetectFace detectFace = new DetectFace();
                detectFace.detect(imageBitmap);
                detectFace.setDetectCallback(new DetectCallback() {

                    @Override
                    public void detectResult(JSONObject result) {
                        // TODO Auto-generated method stub
                        // 设置红色的线条框
                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStrokeWidth(Math.max(imageBitmap.getWidth(),
                                imageBitmap.getHeight()) / 100f);
                        // 对位图的预处理
                        final Bitmap bitmap = Bitmap.createBitmap(
                                imageBitmap.getWidth(), imageBitmap.getHeight(),
                                imageBitmap.getConfig());
                        // 将处理后的位图放置到画布中
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawBitmap(imageBitmap, new Matrix(), null);
                        // 搜索图中所有的脸
                        try {
                            String face = result.getString("face");
                            if (face != null) {
                                int count = result.getJSONArray("face").length();
                                for (int i = 0; i < count; i++) {
                                    float x, y, w, h;
                                    // 获取中心点坐标
                                    x = (float) result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("position")
                                            .getJSONObject("center").getDouble("x");
                                    y = (float) result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("position")
                                            .getJSONObject("center").getDouble("y");
                                    // 获取脸的尺寸大小
                                    w = (float) result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("position")
                                            .getDouble("width");
                                    h = (float) result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("position")
                                            .getDouble("height");

                                    age = result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("attribute")
                                            .getJSONObject("age")
                                            .getString("value");
                                    range = result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("attribute")
                                            .getJSONObject("age")
                                            .getString("range");
                                    gender = result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("attribute")
                                            .getJSONObject("gender")
                                            .getString("value");
                                    smile = result.getJSONArray("face")
                                            .getJSONObject(i)
                                            .getJSONObject("attribute")
                                            .getJSONObject("smiling")
                                            .getString("value");

                                    // 重新以百分比计算图片参数
                                    x = x / 100 * imageBitmap.getWidth();
                                    w = w / 100 * imageBitmap.getWidth() * 0.7f;
                                    y = y / 100 * imageBitmap.getHeight();
                                    h = h / 100 * imageBitmap.getHeight() * 0.7f;
                                    // 绘制人脸框
                                    canvas.drawLine(x - w, y - h, x - w, y + h,
                                            paint);
                                    canvas.drawLine(x - w, y - h, x + w, y - h,
                                            paint);
                                    canvas.drawLine(x + w, y + h, x - w, y + h,
                                            paint);
                                    canvas.drawLine(x + w, y + h, x + w, y - h,
                                            paint);

                                    System.out.println(x + " " + y + " " + w + " "
                                            + h + " ");

                                }
                                MainActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                        // 显示已绘制好的人脸框
                                        imageView.setImageBitmap(bitmap);
                                        String msg = null;
                                        try {
                                            if (Integer.valueOf(age) <= 15) {
                                                msg = Integer.valueOf(age)
                                                        + Integer.valueOf(range)
                                                        + "";
                                            } else if (Integer.valueOf(age) <= 18
                                                    && Integer.valueOf(age) >= 15) {
                                                msg = age;
                                            } else if (Integer.valueOf(age) > 25
                                                    && Integer.valueOf(age) <= 35) {
                                                int i = (Integer.valueOf(age) - 25)
                                                        / Integer.valueOf(range);
                                                msg = Integer.valueOf(age) - i + "";
                                            } else if (Integer.valueOf(age) > 35) {
                                                msg = Integer.valueOf(age)
                                                        - Integer.valueOf(range)
                                                        + "";
                                            } else {
                                                msg = age;
                                            }

                                            // age = msg;

                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            Toast.makeText(MainActivity.this,
                                                    "说好的一起装逼，你现在知道不要脸了？！！！→_→",
                                                    Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        if (gender.equals("Male")) {
                                            gender = "男";
                                        } else if (gender.equals("Female")) {
                                            gender = "女";
                                        }
                                        msg = "分析结果:" + gender + "   " + msg + "岁"
                                                + "    " + "笑容度：" + smile;
                                        Toast.makeText(MainActivity.this, msg,
                                                Toast.LENGTH_LONG).show();
                                        textView.setText(msg);
                                    }

                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            MainActivity.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(MainActivity.this,
                                            "JSONException!", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                        System.out.println(result);
                    }
                });

                break;
        }
    }

    private class DetectFace {
        DetectCallback callback = null;

        public void setDetectCallback(DetectCallback detectCallback) {
            callback = detectCallback;
        }

        public void detect(final Bitmap image) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // 向服务器发起请求，打开输出流
                    HttpRequests httpRequests = new HttpRequests(
                            "1a477e1c8f45f6a233dc509bac154f17",
                            "vkG34ggOrwMjSnsQXq2zxK-2T2HUe0QH", true, false);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    // 将位图进行压缩
                    float scale = Math.min(600f / (float) image.getWidth(),
                            (float) 600f / image.getHeight());
                    Matrix matrix = new Matrix();
                    matrix.postScale(scale, scale);

                    Bitmap imageSmall = Bitmap.createBitmap(image, 0, 0,
                            image.getWidth(), image.getHeight(), matrix, false);

                    // 将压缩后的位图写入指定的outputstream
                    imageSmall.compress(Bitmap.CompressFormat.JPEG, 100,
                            byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        // 发送参数，服务器进行参数分析
                        JSONObject result = httpRequests
                                .detectionDetect(new PostParameters()
                                        .setAttribute(
                                                "age,gender,race,smiling,glass,pose")
                                        .setImg(byteArray));
                        // 完成分析，执行回调
                        if (callback != null) {
                            callback.detectResult(result);
                        }

                    } catch (FaceppParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        MainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this,
                                        "啊哦~网络连接失败,请重试π__π", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    // 返回json
    private interface DetectCallback {
        void detectResult(JSONObject result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAPTURE_CHOOSE) {
                File file = new File(Environment.getExternalStorageDirectory(),
                        "temp.jpg");
                String filePath = file.getAbsolutePath();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                imageBitmap = BitmapFactory.decodeFile(filePath, options);
                // 测量图片大小
                options.inSampleSize = (int) Math.max(1, Math.ceil(Math.max(
                        (double) options.outWidth / 1024f,
                        (double) options.outHeight / 1024f)));
                options.inJustDecodeBounds = false;
                imageBitmap = BitmapFactory.decodeFile(filePath, options);
                Matrix matrix = new Matrix();
                matrix.postRotate(BitmapUtil.getExifOrientation(file.getPath()));
                int width = imageBitmap.getWidth();
                int height = imageBitmap.getHeight();
                imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, width,
                        height, matrix, true);
                imageView.setImageBitmap(imageBitmap);
                faButton_c.setEnabled(true);
            } else {
                if (requestCode == PICTURE_CHOOSE) {
                    if (data != null) {
                        Cursor cursor = getContentResolver().query(
                                data.getData(), null, null, null, null);
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        String fileSrc = cursor.getString(index);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        imageBitmap = BitmapFactory
                                .decodeFile(fileSrc, options);

                        options.inSampleSize = Math.max(1, (int) Math.ceil(Math
                                .max((double) options.outWidth / 1024f,
                                        (double) options.outHeight / 1024f)));
                        options.inJustDecodeBounds = false;

                        imageBitmap = BitmapFactory
                                .decodeFile(fileSrc, options);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(BitmapUtil
                                .getExifOrientation(fileSrc));
                        int width = imageBitmap.getWidth();
                        int height = imageBitmap.getHeight();
                        imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                                width, height, matrix, true);
                        imageView.setImageBitmap(imageBitmap);
                        faButton_c.setEnabled(true);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mController.openShare(this, false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
