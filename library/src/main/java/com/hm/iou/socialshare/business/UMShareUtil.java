package com.hm.iou.socialshare.business;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.hm.iou.socialshare.SocialShareUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.functions.Consumer;

/**
 * @author : syl
 * @Date : 2018/6/8 22:57
 * @E-Mail : shiyaolei@dafy.com
 */
public class UMShareUtil {

    private Activity mActivity;
    private UMShareListener mUMShareListener;

    public UMShareUtil(Activity activity) {
        mActivity = activity;
        mUMShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Toast.makeText(mActivity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        };
    }

    /**
     * 分享图片
     *
     * @param shareMedia
     * @param pictureUrl
     */
    public void sharePicture(final SHARE_MEDIA shareMedia, final String pictureUrl) {
        if (!checkShareChannel(mActivity, shareMedia)) {
            return;
        }

        //QQ分享需要读写SD卡权限
        if (shareMedia == SHARE_MEDIA.QQ) {
            RxPermissions rxPermissions = new RxPermissions(mActivity);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        UMImage umImage = new UMImage(mActivity, pictureUrl);
                        new ShareAction(mActivity).withMedia(umImage).setPlatform(shareMedia).setCallback(mUMShareListener).share();
                    } else {
                        Toast.makeText(mActivity, "分享失败，请开启读写手机存储权限", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return;
        }

        UMImage umImage = new UMImage(mActivity, pictureUrl);
        new ShareAction(mActivity).withMedia(umImage).setPlatform(shareMedia).setCallback(mUMShareListener).share();
    }


    /**
     * 获取Assets文件夹里面的软件logo
     *
     * @return
     */
    private Bitmap getAppLogo() {
        InputStream is = null;
        try {
            is = mActivity.getAssets().open("socialshare_logo.png");
            is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    /**
     * 分享链接
     *
     * @param shareMedia
     * @param urlTitle   标题
     * @param urlDesc    描述
     * @param webUrl     链接地址
     */
    public void shareWebH5Url(final SHARE_MEDIA shareMedia, String urlTitle, String urlDesc, String webUrl) {
        if (!checkShareChannel(mActivity, shareMedia)) {
            return;
        }

        UMImage thumb = new UMImage(mActivity, getAppLogo());
        final UMWeb web = new UMWeb(webUrl);
        web.setThumb(thumb);
        if (!TextUtils.isEmpty(urlTitle)) {
            web.setTitle(urlTitle);
        }
        if (!TextUtils.isEmpty(urlDesc)) {
            web.setDescription(urlDesc);
        }
        if (shareMedia == SHARE_MEDIA.QQ) {
            RxPermissions rxPermissions = new RxPermissions(mActivity);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        new ShareAction(mActivity).withMedia(web).setPlatform(shareMedia).setCallback(mUMShareListener).share();
                    } else {
                        Toast.makeText(mActivity, "分享失败，请开启读写手机存储权限", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return;
        }

        new ShareAction(mActivity).withMedia(web).setPlatform(shareMedia).setCallback(mUMShareListener).share();
    }

    /**
     * 分享文字
     *
     * @param shareMedia
     * @param shareText  分享内容
     */
    public void shareText(SHARE_MEDIA shareMedia, String shareText) {
        if (!checkShareChannel(mActivity, shareMedia)) {
            return;
        }

        if (SHARE_MEDIA.QQ == shareMedia) {
            //QQ不支持分享文字
            SocialShareUtil.sendMsgToQQ(mActivity, shareText);
            return;
        }
        new ShareAction(mActivity).withText(shareText).setPlatform(shareMedia).setCallback(mUMShareListener).share();
    }

    private boolean checkShareChannel(Context context, SHARE_MEDIA shareMedia) {
        if (shareMedia == SHARE_MEDIA.WEIXIN || shareMedia == SHARE_MEDIA.WEIXIN_CIRCLE) {
            PlatformConfig.APPIDPlatform platform = (PlatformConfig.APPIDPlatform) PlatformConfig.getPlatform(SHARE_MEDIA.WEIXIN);
            IWXAPI wxapi = WXAPIFactory.createWXAPI(context, platform.appId);
            if (!wxapi.isWXAppInstalled()) {
                SocialShareUtil.toastMsg(context, "您还未安装微信客户端");
                return false;
            }
        } else if (shareMedia == SHARE_MEDIA.QQ) {
            if (!SocialShareUtil.isAppInstalled(context, SocialShareUtil.PACKAGE_OF_QQ)) {
                SocialShareUtil.toastMsg(context, "您还未安装QQ客户端");
                return false;
            }
        }
        return true;
    }

}
