package com.hm.iou.socialshare.business;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.hm.iou.socialshare.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
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

    public static final String PACKAGE_NAME_OF_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_NAME_OF_QQ_LACH_ACTIVITY = "com.tencent.mobileqq.activity.SplashActivity";


    static UMShareUtil INSTANCE;
    Activity mActivity;
    UMShareListener mUMShareListener;

    private UMShareUtil(Activity activity) {
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

    public static void init(Activity activity) {
        INSTANCE = new UMShareUtil(activity);
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static UMShareUtil getInstance() {
        if (INSTANCE == null) {
            throw new IllegalArgumentException("Must call init() method before call this.");
        }
        return INSTANCE;
    }


    /**
     * 分享图片
     *
     * @param shareMedia
     * @param pictureUrl
     */
    public void sharePicture(final SHARE_MEDIA shareMedia, final String pictureUrl) {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //分享图片
                    UMImage umImage = new UMImage(mActivity, pictureUrl);
                    new ShareAction(mActivity).withMedia(umImage).setPlatform(shareMedia).setCallback(mUMShareListener).share();
                } else {
                    Toast.makeText(mActivity, "权限受阻，无法进行分享", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
     * @param urlTitle
     * @param urlDesc
     * @param webUrl
     */
    public void shareWebH5Url(final SHARE_MEDIA shareMedia, String urlTitle, String urlDesc, String webUrl) {
        UMImage thumb = new UMImage(mActivity, getAppLogo());
        final UMWeb web = new UMWeb(webUrl);
        web.setThumb(thumb);
        if (!TextUtils.isEmpty(urlTitle)) {
            web.setTitle(urlTitle);
        }
        if (!TextUtils.isEmpty(urlDesc)) {
            web.setDescription(urlDesc);
        }
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //分享图片
                    new ShareAction(mActivity).withMedia(web).setPlatform(shareMedia).setCallback(mUMShareListener).share();
                } else {
                    Toast.makeText(mActivity, "权限受阻，无法进行分享", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 分享文字
     *
     * @param shareMedia
     * @param shareText
     */
    public void shareText(SHARE_MEDIA shareMedia, String shareText) {
        putTextIntoClip(shareText);
        if (SHARE_MEDIA.QQ == shareMedia) {
            //QQ不支持分享文字
            openQQView();
            return;
        }
        new ShareAction(mActivity).withText(shareText).setPlatform(shareMedia).setCallback(mUMShareListener).share();
    }

    /**
     * 打开QQ
     */
    private void openQQView() {
        if (isAppInstalled(mActivity, PACKAGE_NAME_OF_QQ)) {
            ComponentName componet = new ComponentName(PACKAGE_NAME_OF_QQ, PACKAGE_NAME_OF_QQ_LACH_ACTIVITY);
            //pkg 就是第三方应用的包名
            //cls 就是第三方应用的进入的第一个Activity
            Intent intent = new Intent();
            intent.setComponent(componet);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } else {
            String msg = mActivity.getString(R.string.shareData_notInstallQQ);
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 复制文字到剪切板
     *
     * @param text
     */
    private void putTextIntoClip(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        //创建ClipData对象
        ClipData clipData = ClipData.newPlainText("LabelText", text);
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData);
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
