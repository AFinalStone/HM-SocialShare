package com.hm.iou.socialshare;

import android.content.Context;

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

        //友盟分享
        PlatformConfig.setWeixin("wx54a8a6252c69ea7c", "fbecfb41d780a864653fd03ca1faa550");
        PlatformConfig.setQQZone("1106653157", "830ggCEDlDxI7ZjD");
        PlatformConfig.setSinaWeibo("22876744", "fbecfb41d780a864653fd03ca1faa550", "http://www.baidu.com");
    }

}
