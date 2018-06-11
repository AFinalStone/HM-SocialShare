package com.hm.iou.socialshare.business.view;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.socialshare.R;

import java.util.List;

/**
 * @author syl
 * @time 2018/6/8 下午6:00
 */
public class PlatformItemListAdapter extends BaseQuickAdapter<IPlatformItem, BaseViewHolder> {

    public PlatformItemListAdapter(@Nullable List<IPlatformItem> data) {
        super(R.layout.socialshare_item_share_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IPlatformItem item) {
        helper.setImageResource(R.id.iv_platForm, item.getPlatformIcon());
        helper.setText(R.id.tv_platForm, item.getPlatformName());
    }

}
