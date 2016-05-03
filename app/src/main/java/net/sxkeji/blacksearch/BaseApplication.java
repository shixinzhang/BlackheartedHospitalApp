package net.sxkeji.blacksearch;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

import java.util.Iterator;
import java.util.List;

/**
 * BaseApplicaition
 * Created by zhangshixin on 5/3/2016.
 */
public class BaseApplication extends Application {
    private final String LEANCLOUD_APPID = "jMHkno2fRLrblBkCFoMJ50wH-gzGzoHsz";
    private final String LEANCLOUD_KEY = "KMUtfpCseSlzdvqchUYtnvMt";

    @Override
    public void onCreate() {
        super.onCreate();
        if (checkIfIsAppRunning(getPackageName())) {
            initLeanCloud();
        }
    }

    /**
     * 初始化LeanCloud
     */

    private void initLeanCloud() {
        //如果使用美国节点，请加上这行代码 AVOSCloud.useAVCloudUS();
        AVOSCloud.initialize(this, LEANCLOUD_APPID, LEANCLOUD_KEY);
//        AVAnalytics.trackAppOpened(getIntent());
    }

    /**
     * 判断程序是否运行
     *
     * @param processName
     * @return
     */
    private boolean checkIfIsAppRunning(String processName) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningList = am.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> iterator = mRunningList.iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) iterator.next();
            if (info.pid == pid) {
                if (processName.equals(info.processName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
