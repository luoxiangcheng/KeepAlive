package com.lxc.keepalive.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by luoxiangcheng on 2018/10/30 10:49
 */

public class AppUtil {

    /**
     * 判断是否安装了某个应用(循环所有应用)
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager(); // 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0); // 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断是否安装了某个应用(比循环所有应用省时)
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppAvilibleSimple(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (packageInfo == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断指定服务是否是运行状态
     * 注意：文档中说明该方法已被废弃，而且获取到的包括系统服务(怎么过滤系统服务还没找到)，
     * 查询的最大服务数值目前设置为100，因为包括系统服务，以自己小米Note为例，查询出的就有88项，
     * 数值太小的话runningServicesList中有可能不存在自己写的服务，从而导致判断失误
     *
     * @param context
     * @param serviceName
     * @return
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServicesList = manager.getRunningServices(100);
        if (runningServicesList.size() <= 0) {
            return false;
        }

        for (int i = 0; i < runningServicesList.size(); i++) {
            String name = runningServicesList.get(i).service.getClassName().toString();
            if (serviceName.equals(name)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
