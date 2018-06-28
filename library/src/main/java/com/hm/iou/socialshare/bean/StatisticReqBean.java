package com.hm.iou.socialshare.bean;

/**
 * Created by hjy on 2018/6/28.
 */

public class StatisticReqBean {

    private int iouKind;
    private int platformType;
    private String iouId;

    public int getIouKind() {
        return iouKind;
    }

    public void setIouKind(int iouKind) {
        this.iouKind = iouKind;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public String getIouId() {
        return iouId;
    }

    public void setIouId(String iouId) {
        this.iouId = iouId;
    }
}
