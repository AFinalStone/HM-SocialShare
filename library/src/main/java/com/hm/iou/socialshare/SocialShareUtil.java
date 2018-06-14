package com.hm.iou.socialshare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.weixin.net.WXAuthUtils;

/**
 * @author : syl
 * @Date : 2018/6/12 10:21
 * @E-Mail : shiyaolei@dafy.com
 */
public class SocialShareUtil {

    public static final String PACKAGE_OF_QQ = "com.tencent.mobileqq";
    public static final String ACTIVITY_OF_QQ = "com.tencent.mobileqq.activity.SplashActivity";

    public static void toastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 复制文字到剪切板，打开微信，发送文字
     *
     * @param context
     * @param textMsg 需要发送的文字
     */
    public static void sendMsgToWeixin(Context context, String textMsg) {
        PlatformConfig.APPIDPlatform platform = (PlatformConfig.APPIDPlatform) PlatformConfig.getPlatform(SHARE_MEDIA.WEIXIN);
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, platform.appId);
        if (!wxapi.isWXAppInstalled()) {
            toastMsg(context, "您还未安装微信客户端");
            return;
        }

        //拷贝到剪切板
        putTextIntoClip(context, textMsg);
        wxapi.openWXApp();
    }


    /**
     * 复制文字到剪切板，打开QQ，粘贴发送文字
     *
     * @param context
     * @param textMsg 需要发送的文字
     */
    public static void sendMsgToQQ(Context context, String textMsg) {
        putTextIntoClip(context, textMsg);

        if (isAppInstalled(context, PACKAGE_OF_QQ)) {
            try {
                ComponentName component = new ComponentName(PACKAGE_OF_QQ, ACTIVITY_OF_QQ);
                Intent intent = new Intent();
                intent.setComponent(component);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String msg = context.getString(R.string.shareData_notInstallQQ);
            toastMsg(context, msg);
        }
    }

    /**
     * 通过短信发送文字
     *
     * @param context
     * @param textMsg 需要发送的文字
     */
    public static void sendMsgBySms(Context context, String textMsg) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        sendIntent.putExtra("sms_body", textMsg);
        sendIntent.setType("vnd.android-dir/mms-sms");
        context.startActivity(sendIntent);
    }

    /**
     * 通过邮箱发送文字
     *
     * @param context
     * @param subJect 邮箱主题
     * @param textMsg 文字
     */
    public static void sendMsgByMail(Context context, String subJect, String textMsg) {
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("plain/text");
        //邮件主题
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, subJect);
        //邮件内容
        email.putExtra(android.content.Intent.EXTRA_TEXT, textMsg);
        context.startActivity(Intent.createChooser(email, "请选择邮件发送内容"));
    }

    /**
     * 调用系统的分享功能
     *
     * @param context
     * @param subject 主题
     * @param testMsg 文字内容
     */
    public static void sendMsgBySys(Context context, String subject, String testMsg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, testMsg);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "share"));
    }

    /**
     * 复制文字到剪切板
     *
     * @param text
     */
    private static void putTextIntoClip(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
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
