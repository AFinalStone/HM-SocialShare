package com.hm.iou.socialshare.business.view;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author syl
 * @time 2018/5/30 上午11:51
 */
public interface IPlatformItem {

    /**
     * 获取分享的平台Icon
     *
     * @return
     */
    int getPlatformIcon();

    /**
     * 获取分享的平台名称
     *
     * @return
     */
    String getPlatformName();

    /**
     * 获取友盟平台分享的类型
     *
     * @return
     */
    SHARE_MEDIA getUMSharePlatform();
}
