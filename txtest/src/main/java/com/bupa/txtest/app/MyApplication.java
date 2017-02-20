package com.bupa.txtest.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import com.bupa.txtest.BuildConfig;
import com.bupa.txtest.R;
import com.bupa.txtest.adapter.EMMessageListenerAdapter;
import com.bupa.txtest.database.DatabaseManager;
import com.bupa.txtest.utils.UIUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * 作者: l on 2017/2/6 07:50
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class MyApplication extends Application {

    private SoundPool mSoundPool;
    private int mDuan;
    private int mYulu;


    /**
     * 当app有多个进程时，onCreate方法就会走几次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initEaseMob();
        initBmob();
        initSoundPool();
        UIUtils.init(this);
        DatabaseManager.getInstance().init(getApplicationContext());

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
    }

    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mDuan = mSoundPool.load(this, R.raw.duan, 1);
        mYulu = mSoundPool.load(this, R.raw.yulu, 1);

    }

    private void initBmob() {
        Bmob.initialize(this, "bd6e83ca7d9b2b93de907aa57697b7f4");
    }

    private void initEaseMob() {
        int pid = android.os.Process.myPid();//获取当前进程id
        String processAppName = getAppName(pid);//获取进程id对应的进程名字

        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认主进程的名字即包名，app其他进程名不是包

        //如果进程名不是包名，表示当前进程不是app的主进程，就不要去初始化环信SDK
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {


            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMOptions options = new EMOptions();
        //设置true,默认接受好友请求，自动添加好友
        options.setAcceptInvitationAlways(true);
        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        if (BuildConfig.DEBUG) {
            EMClient.getInstance().setDebugMode(true);
        }
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();//获取正在运行的进程
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        //遍历当前运行的所有进程
        while (i.hasNext()) {
            //获取进程的信息
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                //找到当前进程
                if (info.pid == pID) {
                    processName = info.processName;//获取进程名字
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            if (isForeGround()) {
                //在前台
                mSoundPool.play(mDuan, 1, 1, 0, 0, 1);
            } else {
                //在后台
                mSoundPool.play(mYulu, 1, 1, 0, 0, 1);
                //弹出Notification
                showNotification(list.get(0));
            }
        }
    };

    private void showNotification(EMMessage emMessage) {
        Notification.Builder builder = new Notification.Builder(this);

        String msg = "";
        if (emMessage.getBody() instanceof EMTextMessageBody) {
            msg = ((EMTextMessageBody) emMessage.getBody()).getMessage();
        } else {
            msg = getString(R.string.no_text_message);
        }

        Notification notification = builder.setContentTitle(getString(R.string.receive_new_message))
                .setContentText(msg)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.avatar5))
                .setSmallIcon(R.mipmap.ic_contact_selected_2)
                .getNotification();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public boolean isForeGround() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (int i = 0; i < runningAppProcesses.size(); i++) {
            //找出当前进程，判断当前进程是否在前台
            if (runningAppProcesses.get(i).processName.equals(getPackageName()) &&
                    runningAppProcesses.get(i).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
