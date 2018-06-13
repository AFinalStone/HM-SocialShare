package com.hm.iou.socialshare;

import java.io.Serializable;

/**
 * Created by hjy on 2018/6/13.
 */
public enum ShareDataTypeEnum implements Serializable {
    shareAgentIOU("平台借条"), sharePaperBorrowIOU("纸质借条"), sharePaperRecvIOU("纸质收条"),
    shareFunIOU("娱乐借条"), shareMoneyIOU("资金借条"), sharePrepareToCreateMoneyIOU("准备资料"),
    shareMyCard("个人名片"), shareUrl("分享网页链接");

    String value;

    ShareDataTypeEnum(String value) {
        this.value = value;
    }

    public String getTypeName() {
        return value;
    }
}
