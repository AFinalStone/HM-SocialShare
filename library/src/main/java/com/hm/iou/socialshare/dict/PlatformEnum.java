//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hm.iou.socialshare.dict;

public enum PlatformEnum {
    SAVE("保存"),
    SMS("短信"),
    EMAIL("邮箱"),
    WEIBO("微博"),
    QQ("QQ"),
    WEIXIN("微信"),
    WEIXIN_CIRCLE("朋友圈");

    private String platformName;

    PlatformEnum(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformName() {
        return platformName;
    }

}
