package com.hm.iou.socialshare.business.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

    /**
     * 支持三种分享方式：文本分享、图片分享、链接分享<br/>
     * 如果同时设置了这3种分享的内容，会按照图片、链接、文本的顺序来判断选择哪种分享方式
     */
    public static class Builder {

        private Activity mActivity;

        private String mText;               //文本分享
        private String mPicUrl;             //图片分享

        private String mWebUrl;             //链接分享
        private String mWebUrlTitle;
        private String mWebUrlDesc;

        private List<PlatFormBean> mPlatforms;

        private UMShareUtil mShareUtil;

        public Builder(Activity activity) {
            this.mActivity = activity;
        }

        /**
         * 设置纯文本分享的内容，短信、邮箱分享会用到
         *
         * @param text
         * @return
         */
        public Builder setText(String text) {
            this.mText = text;
            return this;
        }

        /**
         * 图片分享
         *
         * @param picUrl
         * @return
         */
        public Builder setPicUrl(String picUrl) {
            this.mPicUrl = picUrl;
            return this;
        }

        /**
         * 设置分享链接url地址
         *
         * @param webUrl
         * @return
         */
        public Builder setWebUrl(String webUrl) {
            this.mWebUrl = webUrl;
            return this;
        }

        /**
         * 设置分享链接标题
         *
         * @param webUrlTitle
         * @return
         */
        public Builder setWebUrlTitle(String webUrlTitle) {
            this.mWebUrlTitle = webUrlTitle;
            return this;
        }

        /**
         * 设置分享链接描述
         *
         * @param webUrlDesc
         * @return
         */
        public Builder setWebUrlDesc(String webUrlDesc) {
            this.mWebUrlDesc = webUrlDesc;
            return this;
        }

        public Builder setPlatforms(List<PlatFormBean> platforms) {
            this.mPlatforms = platforms;
            return this;
        }

        private SharePlatformDialog createDialog() {
            mShareUtil = new UMShareUtil(mActivity);

            final SharePlatformDialog mDialog = new SharePlatformDialog(mActivity, R.style.UikitAlertDialogStyle_FromBottom);
            View view = LayoutInflater.from(mActivity).inflate(R.layout.socialshare_dialog_share_data, null);
            final View container = view.findViewById(R.id.ll_background);
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
                if (posSave != -1) {
                    mPlatforms.remove(posSave);
                }
            }
            PlatformItemListAdapter adapter = new PlatformItemListAdapter((ArrayList) mPlatforms);
            adapter.bindToRecyclerView(recyclerView);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PlatFormBean platFormBean = (PlatFormBean) adapter.getItem(position);
                    //分享图片
                    if (!TextUtils.isEmpty(mPicUrl)) {
                        if (PlatformEnum.SAVE == platFormBean.getSharePlatform()) {
                            FileUtil.savePicture(mActivity, mPicUrl);
                        } else {
                            mShareUtil.sharePicture(platFormBean.getUMSharePlatform(), mPicUrl);
                        }
                        return;
                    }

                    //分享Web的url
                    if (!TextUtils.isEmpty(mWebUrl)) {
                        if (PlatformEnum.SAVE == platFormBean.getSharePlatform()) {
                            return;
                        }
                        if (PlatformEnum.SMS == platFormBean.getSharePlatform()
                                || PlatformEnum.EMAIL == platFormBean.getSharePlatform()) {
                            //如果是短信分享、邮箱分享，需要特殊处理
                            String content = mText;
                            if (TextUtils.isEmpty(content)) {
                                content = mWebUrlTitle + mWebUrl;
                            }
                            mShareUtil.shareText(platFormBean.getUMSharePlatform(), content);
                            return;
                        }
                        mShareUtil.shareWebH5Url(platFormBean.getUMSharePlatform(), mWebUrlTitle, mWebUrlDesc, mWebUrl);
                        return;
                    }

                    //分享文字
                    if (!TextUtils.isEmpty(mText)) {
                        if (PlatformEnum.SAVE == platFormBean.getSharePlatform()) {
                            return;
                        }
                        mShareUtil.shareText(platFormBean.getUMSharePlatform(), mText);
                        return;
                    }
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager(mActivity, mPlatforms.size()));
            recyclerView.setAdapter(adapter);

            // 定义Dialog布局和参数
            Window dialogWindow = mDialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            mDialog.setContentView(view);

            // 调整dialog背景大小
            int width = mActivity.getResources().getDisplayMetrics().widthPixels;
            container.setLayoutParams(new FrameLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));

            return mDialog;
        }

        public SharePlatformDialog show() {
            SharePlatformDialog dialog = createDialog();
            dialog.show();
            return dialog;
        }

    }

}
