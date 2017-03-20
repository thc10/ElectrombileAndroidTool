package com.xiaoantech.imeidatasearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xiaoantech.imeidatasearch.Camera.ViewfinderView;
import com.xiaoantech.imeidatasearch.Camera.InactivityTimer;
import com.xiaoantech.imeidatasearch.Camera.CaptureActivityHandler;
import com.xiaoantech.imeidatasearch.Camera.CameraManager;
import com.xiaoantech.imeidatasearch.LocalDataManage;
import java.util.Vector;

/**
 * Created by 73843 on 2017/3/20.
 */

public class CaptureActivity extends Activity implements SurfaceHolder.Callback{
    private ViewfinderView viewfinderView;
    private InactivityTimer inactivityTimer;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private SurfaceHolder surfaceHolder;
    private boolean hasSurface;


    private CaptureActivityHandler handler;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (handler != null){
                handler.restartPreviewAndDecode();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_imei);
        initView();
        CameraManager.init(getApplication());


        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);

        Message msg = Message.obtain();
        mHandler.sendMessageDelayed(msg,3000);

    }

    private void initView(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
//            EventBus.getDefault().register(this);
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            surfaceHolder = surfaceView.getHolder();

            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }

            decodeFormats = null;
            characterSet = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        if (!hasSurface){
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraManager.get().closeDriver();
        hasSurface = false;
    }

    private void initCamera(SurfaceHolder surfaceHolder){
        try{
            CameraManager.get().openDriver(surfaceHolder);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        if (handler == null){
            handler = new CaptureActivityHandler(this,decodeFormats,characterSet);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder(){
        viewfinderView.drawViewfinder();
    }

    public void handleDecode(Result result, Bitmap barcode){
        inactivityTimer.onActivity();


        String resultString = result.getText();
        if (resultString.charAt(8) != '"'){
            StringBuilder stringBuilder = new StringBuilder(resultString);
            stringBuilder.insert(8,'"');
            resultString = stringBuilder.toString();
        }

        String IMEI;
        if (resultString.contains("IMEI")){
            try{
                IMEI = JSONUtil.ParseJSON(resultString,"IMEI");
            }catch (Exception e){
                Toast.makeText(this,"扫描失败，请重新扫描！",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            if (IMEI == null || IMEI.length() != 15){
                Toast.makeText(this,"IMEI的长度错误或者为空",Toast.LENGTH_SHORT).show();
            }else {
                LocalDataManage.getInstance().setimei(IMEI);
                Log.e("ERROR", IMEI);
            }
        }

    }
}
