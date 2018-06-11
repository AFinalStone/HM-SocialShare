package com.hm.iou.socialshare.business;

import android.Manifest;
import android.app.Activity;

import com.hm.iou.tools.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * @author : syl
 * @Date : 2018/6/8 22:57
 * @E-Mail : shiyaolei@dafy.com
 */
public class FileUtil {

    /**
     * @param pictureUrl 图片链接地址
     * @param activity
     * @param pictureUrl
     */
    public static void savePicture(final Activity activity, String pictureUrl) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //保存图片
                } else {
                    ToastUtil.showMessage(activity, "权限受阻，无法进行保存");
                }
            }
        });
    }

}
