package com.hm.iou.socialshare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author : syl
 * @Date : 2018/6/12 10:21
 * @E-Mail : shiyaolei@dafy.com
 */
public class SocialShareUtil {

    public static final String PACKAGE_OF_QQ = "com.tencent.mobileqq";
    public static final String ACTIVITY_OF_QQ = "com.tencent.mobileqq.activity.SplashActivity";
    public static final String PACKAGE_OF_WEIXIN = "com.tencent.mm";
    public static final String ACTIVITY_OF_WEIXIN = "com.tencent.mm.ui.LauncherUI";

    /**
     * 复制文字到剪切板，打开微信，发送文字
     *
     * @param context
     * @param textMsg 需要发送的文字
     */
    public static void sendMsgToWeixin(Context context, String textMsg) {
        //拷贝到剪切板
        putTextIntoClip(context, textMsg);
        ComponentName componet = new ComponentName(PACKAGE_OF_WEIXIN, ACTIVITY_OF_WEIXIN);
        //pkg 就是第三方应用的包名
        //cls 就是第三方应用的进入的第一个Activity
        Intent intent = new Intent();
        intent.setComponent(componet);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 复制文字到剪切板，打开QQ，粘贴发送文字
     *
     * @param context
     * @param textMsg 需要发送的文字
     */
    public static void sendMsgToQQ(Context context, String textMsg) {
        putTextIntoClip(context, textMsg);
        ComponentName componet = new ComponentName(PACKAGE_OF_QQ, ACTIVITY_OF_QQ);
        //pkg 就是第三方应用的包名
        //cls 就是第三方应用的进入的第一个Activity
        Intent intent = new Intent();
        intent.setComponent(componet);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        //短信内容
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

}
