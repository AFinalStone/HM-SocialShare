package com.hm.iou.socialshare.business.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.socialshare.R;
import com.hm.iou.socialshare.api.ShareApi;
import com.hm.iou.socialshare.bean.PlatFormBean;
import com.hm.iou.socialshare.business.FileUtil;
import com.hm.iou.socialshare.business.UMShareUtil;
import com.hm.iou.socialshare.dict.PlatformEnum;
import com.hm.iou.tools.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * @author syl
 * @time 2018/5/22 下午5:57
 */
public class SharePlatformDialog extends Dialog {

    private UMShareUtil mUmShareUtil;

    private SharePlatformDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    /**
     * 释放资源
     */
    public void onDestroy() {
        if (mUmShareUtil != null) {
            mUmShareUtil.onDestroy();
            mUmShareUtil = null;
        }
    }

    /**
     * 支持三种分享方式：文本分享、图片分享、链接分享<br/>
     * 如果同时设置了这3种分享的内容，会按照图片、链接、文本的顺序来判断选择哪种分享方式
     */
    public static class Builder {

        private Activity mActivity;

        private String mDialogTitle;

        private String mText;               //文本分享

        private String mPicUrl;             //图片分享
        private Bitmap mBitmap;             //图片分享
        private boolean mShowImg;           //是否显示图片预览

        private String mWebUrl;             //链接分享
        private String mWebUrlTitle;
        private String mWebUrlDesc;
        private Bitmap mWebUrlBitmap;        //H5的缩略图片

        private List<PlatFormBean> mPlatforms;

        private UMShareUtil mShareUtil;

        private String mIouId;
        private int mIouKind;
        private String mTraceType;

        private UMShareListener mShareListener;

        public Builder(Activity activity) {
            this.mActivity = activity;
        }

        public Builder setShareListener(UMShareListener listener) {
            this.mShareListener = listener;
            return this;
        }

        public Builder setTitle(String title) {
            mDialogTitle = title;
            return this;
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
         * 设置埋点的类型来源
         *
         * @param traceType
         */
        public Builder setTraceType(String traceType) {
            this.mTraceType = traceType;
            return this;
        }

        /**
         * 图片分享，可以选择设置图片的url，或者是Bitmap
         *
         * @param picUrl
         * @return
         */
        public Builder setPicUrl(String picUrl) {
            this.mPicUrl = picUrl;
            return this;
        }

        /**
         * 图片分享，可以选择设置图片的url，或者是Bitmap
         *
         * @param bitmap 要分享的图片
         * @return
         */
        public Builder setBitmap(Bitmap bitmap) {
            this.mBitmap = bitmap;
            return this;
        }

        public Builder setShowImage(boolean show) {
            this.mShowImg = show;
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

        /**
         * 设置分享链接的缩略图
         *
         * @param webUrlBitmap
         * @return
         */
        public Builder setWebUrlBitmap(Bitmap webUrlBitmap) {
            this.mWebUrlBitmap = webUrlBitmap;
            return this;
        }

        public Builder setPlatforms(List<PlatFormBean> platforms) {
            this.mPlatforms = platforms;
            return this;
        }

        public Builder setShareData(String iouId, int iouKind) {
            mIouId = iouId;
            mIouKind = iouKind;
            return this;
        }

        private SharePlatformDialog createDialog() {
            mShareUtil = new UMShareUtil(mActivity);
            mShareUtil.setShareListener(mShareListener);

            final SharePlatformDialog mDialog = new SharePlatformDialog(mActivity, R.style.UikitAlertDialogStyle_FromBottom);
            View view = LayoutInflater.from(mActivity).inflate(R.layout.socialshare_dialog_share_data, null);
            ImageView ivImagePreview = view.findViewById(R.id.iv_dialog_preview);

            final TextView tvTitle= view.findViewById(R.id.tv_dialog_title);
            if (!TextUtils.isEmpty(mDialogTitle)) {
                tvTitle.setText(mDialogTitle);
            }
            final View container = view.findViewById(R.id.ll_background);
            RecyclerView recyclerView = view.findViewById(R.id.rl_platform);

            if (TextUtils.isEmpty(mPicUrl) && mBitmap == null) {
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
                    int platformType = 0;
                    if (platFormBean.getSharePlatform() == PlatformEnum.WEIXIN) {
                        if (!TextUtils.isEmpty(mTraceType)) {
                            MobclickAgent.onEvent(mActivity, mTraceType + "_wx_click");
                        }
                        platformType = 1;
                    } else if (platFormBean.getSharePlatform() == PlatformEnum.WEIXIN_CIRCLE) {
                        if (!TextUtils.isEmpty(mTraceType)) {
                            MobclickAgent.onEvent(mActivity, mTraceType + "_wxcircle_click");
                        }
                        platformType = 2;
                    } else if (platFormBean.getSharePlatform() == PlatformEnum.QQ) {
                        if (!TextUtils.isEmpty(mTraceType)) {
                            MobclickAgent.onEvent(mActivity, mTraceType + "_qq_click");
                        }
                        platformType = 6;
                    } else if (platFormBean.getSharePlatform() == PlatformEnum.WEIBO) {
                        if (!TextUtils.isEmpty(mTraceType)) {
                            MobclickAgent.onEvent(mActivity, mTraceType + "_weibo_click");
                        }
                        platformType = 3;
                    } else if (platFormBean.getSharePlatform() == PlatformEnum.EMAIL) {
                        if (!TextUtils.isEmpty(mTraceType)) {
                            MobclickAgent.onEvent(mActivity, mTraceType + "_email_clcik");
                        }
                        platformType = 5;
                    } else if (platFormBean.getSharePlatform() == PlatformEnum.SMS) {
                        if (!TextUtils.isEmpty(mTraceType)) {
                            MobclickAgent.onEvent(mActivity, mTraceType + "_sms_clcik");
                        }
                        platformType = 4;
                    }
                    if (platformType > 0 && !TextUtils.isEmpty(mIouId)) {
                        //统计分享
                        ShareApi.addStatisticShare(mIouId, mIouKind, platformType)
                                .subscribe(new Consumer<BaseResponse<Object>>() {
                                    @Override
                                    public void accept(BaseResponse<Object> objectBaseResponse) throws Exception {

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {

                                    }
                                });
                    }

                    //分享图片
                    if (!TextUtils.isEmpty(mPicUrl)) {
                        if (PlatformEnum.SAVE == platFormBean.getSharePlatform()) {
                            if (!TextUtils.isEmpty(mTraceType)) {
                                MobclickAgent.onEvent(mActivity, mTraceType + "_save_click");
                            }
                            FileUtil.savePicture(mActivity, mPicUrl);
                        } else {
                            mShareUtil.sharePicture(platFormBean.getUMSharePlatform(), mPicUrl);
                        }
                        return;
                    }

                    //直接分享图片
                    if (mBitmap != null) {
                        if (PlatformEnum.SAVE == platFormBean.getSharePlatform()) {
                            FileUtil.savePicture(mActivity, mBitmap);
                        } else {
                            mShareUtil.sharePicture(platFormBean.getUMSharePlatform(), mBitmap);
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
                        mShareUtil.shareWebH5Url(platFormBean.getUMSharePlatform(), mWebUrlTitle, mWebUrlDesc, mWebUrl, mWebUrlBitmap);
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

            if (mShowImg) {
                if (!TextUtils.isEmpty(mPicUrl)) {
                    ImageLoader.getInstance(mActivity).displayImage(mPicUrl, ivImagePreview);
                } else if (mBitmap != null) {
                    ivImagePreview.setImageBitmap(mBitmap);
                }
                View content = view.findViewById(R.id.ll_content);
                content.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });


                view.findViewById(R.id.view_dialog_home_divider).setVisibility(View.VISIBLE);
                TextView tvHome = view.findViewById(R.id.tv_dialog_home);
                tvHome.setVisibility(View.VISIBLE);
                tvHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/main/index")
                                .navigation(mActivity);
                    }
                });
            } else {
                ivImagePreview.setVisibility(View.GONE);
            }

            // 调整dialog背景大小
            int width = mActivity.getResources().getDisplayMetrics().widthPixels;
            container.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));

            mDialog.mUmShareUtil = mShareUtil;
            return mDialog;
        }

        public SharePlatformDialog show() {
            SharePlatformDialog dialog = createDialog();
            dialog.show();
            return dialog;
        }

    }

}
