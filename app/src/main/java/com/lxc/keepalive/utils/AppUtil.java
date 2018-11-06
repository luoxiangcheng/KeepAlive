package com.lxc.keepalive.utils;

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
}
