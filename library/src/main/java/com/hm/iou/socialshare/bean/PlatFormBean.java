package com.hm.iou.socialshare.bean;

import com.hm.iou.socialshare.R;
import com.hm.iou.socialshare.business.view.IPlatformItem;
import com.hm.iou.socialshare.dict.PlatformEnum;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author : syl
 * @Date : 2018/6/8 22:12
 * @E-Mail : shiyaolei@dafy.com
 */
public class PlatFormBean implements IPlatformItem {

    PlatformEnum sharePlatform;

    public PlatFormBean(PlatformEnum sharePlatform) {
        this.sharePlatform = sharePlatform;
    }

    public PlatformEnum getSharePlatform() {
        return sharePlatform;
    }

    @Override
    public int getPlatformIcon() {
        int icon = 0;
        if (PlatformEnum.QQ == sharePlatform) {
            icon = R.mipmap.icon_share_qq;
        } else if (PlatformEnum.EMAIL == sharePlatform) {
            icon = R.mipmap.icon_share_email;
        } else if (PlatformEnum.SMS == sharePlatform) {
            icon = R.mipmap.icon_share_sms;
        } else if (PlatformEnum.SAVE == sharePlatform) {
            icon = R.mipmap.icon_share_save;
        } else if (PlatformEnum.WEIBO == sharePlatform) {
            icon = R.mipmap.icon_share_weibo;
        } else if (PlatformEnum.WEIXIN == sharePlatform) {
            icon = R.mipmap.icon_share_weixin;
        } else if (PlatformEnum.WEIXIN_CIRCLE == sharePlatform) {
            icon = R.mipmap.icon_share_weixin_circle;
        }
        return icon;
    }

    @Override
    public String getPlatformName() {
        return sharePlatform.getPlatformName();
    }

    @Override
    public SHARE_MEDIA getUMSharePlatform() {
        SHARE_MEDIA shareMedia = null;
        if (PlatformEnum.QQ == sharePlatform) {
            shareMedia = SHARE_MEDIA.QQ;
        } else if (PlatformEnum.EMAIL == sharePlatform) {
            shareMedia = SHARE_MEDIA.EMAIL;
        } else if (PlatformEnum.SMS == sharePlatform) {
            shareMedia = SHARE_MEDIA.SMS;
        } else if (PlatformEnum.WEIBO == sharePlatform) {
            shareMedia = SHARE_MEDIA.SINA;
        } else if (PlatformEnum.WEIXIN == sharePlatform) {
            shareMedia = SHARE_MEDIA.WEIXIN;
        } else if (PlatformEnum.WEIXIN_CIRCLE == sharePlatform) {
            shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        return shareMedia;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
