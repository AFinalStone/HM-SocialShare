package com.hm.iou.socialshare.api;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.socialshare.bean.StatisticReqBean;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjy on 2018/6/28.
 */

public class ShareApi {

    private static ShareService getService() {
        return HttpReqManager.getInstance().getService(ShareService.class);
    }

    public static Flowable<BaseResponse<Object>> addStatisticShare(String iouId, int iouKind, int platformType) {
        StatisticReqBean reqBean = new StatisticReqBean();
        reqBean.setIouId(iouId);
        reqBean.setIouKind(iouKind);
        reqBean.setPlatformType(platformType);
        return getService().addStatisticShare(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}