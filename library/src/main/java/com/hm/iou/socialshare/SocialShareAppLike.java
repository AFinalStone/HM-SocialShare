package com.hm.iou.socialshare;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

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

        String wxId = null;
        String wxSecret = null;
        String qqId = null;
        String qqSecret = null;
        String weiboId = null;
        String weiboSecret = null;
        String weiboCallback = null;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle data = info.metaData;
            wxId = data.getString("WEIXIN_ID");
            wxSecret = data.getString("WEIXIN_SECRET");
            qqId = data.getString("TENCENT_QQ_ID");
            if (TextUtils.isEmpty(qqId)) {
                qqId = data.getInt("TENCENT_QQ_ID") + "";
            }
            qqSecret = data.getString("TENCENT_QQ_SECRET");
            weiboId = data.getString("WEIBO_ID");
            if (TextUtils.isEmpty(weiboId)) {
                weiboId = data.getInt("WEIBO_ID") + "";
            }
            weiboSecret = data.getString("WEIBO_SECRET");
            weiboCallback = data.getString("WEIBO_CALLBACK_URL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        PlatformConfig.setWeixin(wxId, wxSecret);
        PlatformConfig.setQQZone(qqId, qqSecret);
        PlatformConfig.setSinaWeibo(weiboId, weiboSecret, weiboCallback);
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
