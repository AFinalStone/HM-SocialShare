package com.hm.iou.socialshare.api;

import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.socialshare.bean.StatisticReqBean;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hjy on 2018/6/28.
 */

public interface ShareService {

    @POST("/api/iou/front/v1/share/addStatisticShare")
    Flowable<BaseResponse<Object>> addStatisticShare(@Body StatisticReqBean reqBean);


}
