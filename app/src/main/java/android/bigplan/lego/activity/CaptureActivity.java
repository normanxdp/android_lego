package android.bigplan.lego.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.bigplan.lego.http.entity.mine.content.AbstractContentBody;
import android.bigplan.lego.util.AbStrUtil;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import android.bigplan.lego.R;
import android.bigplan.lego.app.AppApplication;
import android.bigplan.lego.decode.camera.CameraManager;
import android.bigplan.lego.decode.decoding.CaptureActivityHandler;
import android.bigplan.lego.decode.decoding.InactivityTimer;
import android.bigplan.lego.decode.view.ViewfinderView;
import android.bigplan.lego.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import android.bigplan.lego.global.AbConstant;
import android.bigplan.lego.http.AbHttpUtil;
import android.bigplan.lego.http.AbRequest;
import android.bigplan.lego.http.AbRequestParams;
import android.bigplan.lego.http.AbStringHttpResponseListener;
import android.bigplan.lego.util.AbDialogFragmentUtil;
import android.bigplan.lego.util.AbToastUtil;
import android.bigplan.lego.util.Logger;
import android.bigplan.lego.view.AbTitleBar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public class CaptureActivity extends BaseTActivity implements Callback{
    
    private CaptureActivityHandler handler;
    
    private ViewfinderView viewfinderView;
    
    private boolean hasSurface;
    
    private Vector<BarcodeFormat> decodeFormats;
    
    private String characterSet;
    
    private InactivityTimer inactivityTimer;
    
    private MediaPlayer mediaPlayer;
    
    private boolean playBeep;
    
    private static final float BEEP_VOLUME = 0.10f;

	private static final String TAG = CaptureActivity.class.getSimpleName();
    
    private boolean vibrate;
    
    public static Activity activity;
    


    protected String getToolBarTitle() {
        return null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        activity = this;
        AppApplication.getInstance().addActivity(this);
        
        // ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
		mAbHttpUtil = AbHttpUtil.getInstance(this);
        viewfinderView = (ViewfinderView)findViewById(R.id.viewfinder_view);
        
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        }
        else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        
        playBeep = true;
        AudioManager audioService = (AudioManager)getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }
    
    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    protected String getInitParam(String result){
        String keyWord = result;
        try {

            keyWord = URLDecoder.decode(result, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        result = AbRequest.getOriginalData(keyWord);
        return result;
    }


    /**
     * 处理扫描结果
     * 
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        Logger.i("FingerArt", resultString);
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(mContext, "Scan failed!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else if (resultString.contains(AbConstant.KEY_CAPTURE_FLAG)){
			//http://39.108.107.221:1317/member/register?params=
            // V5ET5YVl0i3zLtjkJqbFG2bDzHVE8Wor8oAaTFzmaZ83oWZnZQQXEXPEXXMQDrsjd8fv5BKZv3FfgDQ2XpnTA4ya0Wjy2eyN
             //V5ET5YVl0i3zLtjkJqbFG2bDzHVE8Wor8oAaTFzmaZ83oWZnZQQXEXPEXXMQDrsjd8fv5BKZv3FfgDQ2XpnTA4ya0Wjy2eyN
			//解析params的字段了
            String[] split = resultString.split("params=");
            String params = split[1];
            String data = getInitParam(params);

            if (!TextUtils.isEmpty(data)){
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String type = jsonObject.getString("type");
                    String objid = jsonObject.getString("objid");
                    int nType = Integer.parseInt(type);
                    Intent intent = null;

                    switch (nType){
                        case 1://个人的二维码
                            AbToastUtil.showToast(mContext, getString(R.string.qrcode_invitecode));
                            onBackPressed();
                            break;
                        case 2://转账二维码支付
                            if (AppApplication.getInstance().isUserLogin){
                                String pwd = AppApplication.getInstance().getUser().getPayPwd();
                                if (TextUtils.isEmpty(pwd)){
                                    AbToastUtil.showToast(mContext, "您还没设置支付密码，请先设置支付密码！");
                                    onBackPressed();
                                }else{
                                    intent = new Intent(mContext, ShopPayActivity.class);
                                    intent.putExtra("staffid", objid);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                intent = new Intent(mContext, LoginRegisterActivity.class);
                                startActivity(intent);
                            }

                            break;
                        case 3: //商家二维码，跳转到商店详情
                            if (AppApplication.getInstance().isUserLogin){
                                intent = new Intent(mContext, StoreDetailActivity.class);
                                intent.putExtra("store_memberid",  objid);
                                intent.setClass(mContext, StoreDetailActivity.class);
                                startActivity(intent);
                            }else{
                                intent = new Intent(mContext, LoginRegisterActivity.class);
                                startActivity(intent);
                            }

                            break;

                    }
                }catch (Exception e){
                    AbToastUtil.showToast(mContext, getString(R.string.qrcode_invitecode));
                    onBackPressed();
                }
            }else{
                AbToastUtil.showToast(mContext, getString(R.string.qrcode_invitecode));
                onBackPressed();
            }
			
		}else{
            AbToastUtil.showToast(mContext, getString(R.string.qrcode_invitecode));
            onBackPressed();
		}
		
    }

    /**
     * 活动扫描
     * @param resultString
     */
	private void scanAct(String resultString) {
    	final Intent dataIntent = new Intent();
    	setResult(RESULT_CANCELED, dataIntent);


	}
    
    /**
     * 课程签到
     * @param resultString 扫描到的内容
     */
    private void scanCourse(String resultString) {
    	final Intent dataIntent = new Intent();
    	setResult(RESULT_CANCELED, dataIntent);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
        	showPerssionDialog();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
        int height) {
        
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
        
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
        
    }
    
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }
    
    public Handler getHandler() {
        return handler;
    }
    
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
        
    }
    
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            }
            catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }
    
    private static final long VIBRATE_DURATION = 200L;
    
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener(){
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

	private AbHttpUtil mAbHttpUtil;

	private String toast_msg = "签到失败, 请重试";

    public void showPerssionDialog(){
    	AbDialogFragmentUtil.showAlertDialog(mContext,
                 R.drawable.ic_alert, "拍照和录像权限没有开启","系统设置 — 权限管理\r\n找到本应用，设置其拍照和录像权限为开启",
                 new AbDialogOnClickListener(){
                     
                     @Override
                     public void onPositiveClick() {
                    	 onBackPressed();
                     }
                     
                     @Override
                     public void onNegativeClick() {
                    	 onBackPressed();
                     }
                 },true);
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	overridePendingTransition(R.anim.activity_static, R.anim.activity_exit_bottom);
    }
    
}
