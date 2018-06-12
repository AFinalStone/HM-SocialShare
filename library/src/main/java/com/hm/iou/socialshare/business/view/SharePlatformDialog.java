package com.hm.iou.socialshare.business.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.socialshare.R;
import com.hm.iou.socialshare.bean.PlatFormBean;
import com.hm.iou.socialshare.business.FileUtil;
import com.hm.iou.socialshare.business.UMShareUtil;
import com.hm.iou.socialshare.dict.PlatformEnum;

import java.util.ArrayList;
import java.util.List;


/**
 * @author syl
 * @time 2018/5/22 下午5:57
 */
public class SharePlatformDialog extends Dialog {

    private SharePlatformDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        Activity mActivity;
        String mText;
        String mPicUrl;
        String mWebUrl;
        String mWebUrlTitle;
        String mWebUrlDesc;
        List<PlatFormBean> mPlatforms;

        public Builder(Activity activity) {
            this.mActivity = activity;
        }

        public Builder setText(String text) {
            this.mText = text;
            return this;
        }

        public Builder setPicUrl(String picUrl) {
            this.mPicUrl = picUrl;
            return this;
        }

        public Builder setWebUrl(String webUrl) {
            this.mWebUrl = webUrl;
            return this;
        }

        public Builder setWebUrlTitle(String webUrlTitle) {
            this.mWebUrlTitle = webUrlTitle;
            return this;
        }

        public Builder setWebUrlDesc(String webUrlDesc) {
            this.mWebUrlDesc = webUrlDesc;
            return this;
        }

        public Builder setPlatforms(List<PlatFormBean> platforms) {
            this.mPlatforms = platforms;
            return this;
        }

        private SharePlatformDialog CreateDialog() {

            UMShareUtil.init(mActivity);

            final SharePlatformDialog mDialog = new SharePlatformDialog(mActivity, R.style.UikitAlertDialogStyle);

            View view = LayoutInflater.from(mActivity).inflate(R.layout.socialshare_dialog_share_data, null);
            View background = view.findViewById(R.id.ll_background);
            RecyclerView recyclerView = view.findViewById(R.id.rl_platform);
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            if (TextUtils.isEmpty(mPicUrl)) {
                int posSave = -1;
                for (int i = 0; i < mPlatforms.size(); i++) {
                    if (mPlatforms.get(i).getSharePlatform() == PlatformEnum.SAVE) {
                        posSave = i;
                        break;
                    }
                }
                mPlatforms.remove(posSave);
            }
            PlatformItemListAdapter adapter = new PlatformItemListAdapter((ArrayList) mPlatforms);
            adapter.bindToRecyclerView(recyclerView);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PlatFormBean platFormBean = (PlatFormBean) adapter.getItem(position);
                    //分享文字
                    if (!TextUtils.isEmpty(mText)) {
                        if (PlatformEnum.SAVE != platFormBean.getSharePlatform()) {
                            UMShareUtil.getInstance().shareText(platFormBean.getUMSharePlatform(), mText);
                        }
                        return;
                    }
                    //分享图片
                    if (!TextUtils.isEmpty(mPicUrl)) {
                        if (PlatformEnum.SAVE == platFormBean.getSharePlatform()) {
                            FileUtil.savePicture(mActivity, mPicUrl);
                        } else {
                            UMShareUtil.getInstance().sharePicture(platFormBean.getUMSharePlatform(), mPicUrl);
                        }
                        return;
                    }
                    //分享Web的url
                    if (!TextUtils.isEmpty(mWebUrl)) {
                        if (PlatformEnum.SAVE != platFormBean.getSharePlatform()) {
                            UMShareUtil.getInstance().shareWebH5Url(platFormBean.getUMSharePlatform(), mWebUrlTitle, mWebUrlDesc, mWebUrl);
                        }
                    }
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager(mActivity, mPlatforms.size()));
            recyclerView.setAdapter(adapter);
            // 定义Dialog布局和参数
            // 调整dialog背景大小
            Window dialogWindow = mDialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            mDialog.setContentView(view);

            // 调整dialog背景大小
            WindowManager windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            background.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT));

            return mDialog;
        }

        public void show() {
            CreateDialog().show();
        }

    }

    public class PlatformEnmu {

    }


}
