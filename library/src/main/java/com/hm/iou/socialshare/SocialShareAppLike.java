package com.hm.iou.socialshare;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.umeng.socialize.PlatformConfig;

/**
 * @author syl
 * @time 2018/6/9 上午1:34
 */
public class SocialShareAppLike {


    private static SocialShareAppLike mApp;

    public static SocialShareAppLike getInstance() {
        if (mApp == null) {
            throw new RuntimeException("MsgCenterAppLike should init first.");
        }
        return mApp;
    }

    public void onCreate(Context context) {
        mApp = this;
        init(context);
    }

    /**
     * 分享组件的初始化
     *
     * @param context
     */
    private void init(Context context) {
        //友盟错误分析

        //友盟推送也需要初始化，在这里暂时移除掉，在主工程里初始化
        //UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, "");

        PlatformConfig.setWeixin(getMetaData(context, "WEIXIN_ID"), getMetaData(context, "WEIXIN_SECRET"));
        PlatformConfig.setQQZone(getMetaData(context, "TENCENT_QQ_ID"), getMetaData(context, "TENCENT_QQ_SECRET"));
        PlatformConfig.setSinaWeibo(getMetaData(context, "WEIBO_ID"), getMetaData(context, "WEIBO_SECRET"), getMetaData(context, "WEIBO_CALLBACK_URL"));
    }

    private String getMetaData(Context context, String key) {
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}
