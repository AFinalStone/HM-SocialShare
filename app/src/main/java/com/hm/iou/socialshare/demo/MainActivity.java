package com.hm.iou.socialshare.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.socialshare.SocialShareAppLike;
import com.hm.iou.socialshare.SocialShareUtil;
import com.hm.iou.socialshare.bean.PlatFormBean;
import com.hm.iou.socialshare.business.view.SharePlatformDialog;
import com.hm.iou.socialshare.dict.PlatformEnum;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocialShareAppLike socialShareAppLike = new SocialShareAppLike();
        socialShareAppLike.onCreate(this);

        findViewById(R.id.btn_share_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PlatFormBean> listData = new ArrayList<>();
                listData.add(new PlatFormBean(PlatformEnum.SAVE));
                listData.add(new PlatFormBean(PlatformEnum.QQ));
                listData.add(new PlatFormBean(PlatformEnum.WEIXIN));
                new SharePlatformDialog
                        .Builder(MainActivity.this)
                        .setText("借条管家")
                        .setPlatforms(listData)
                        .show();
            }
        });
        findViewById(R.id.btn_share_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PlatFormBean> listData = new ArrayList<>();
                listData.add(new PlatFormBean(PlatformEnum.SAVE));
                listData.add(new PlatFormBean(PlatformEnum.QQ));
                listData.add(new PlatFormBean(PlatformEnum.WEIXIN));
                new SharePlatformDialog
                        .Builder(MainActivity.this)
                        .setPicUrl("https://upload-images.jianshu.io/upload_images/972352-9911637db5512613.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700")
                        .setPlatforms(listData)
                        .show();
            }
        });
        findViewById(R.id.btn_share_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PlatFormBean> listData = new ArrayList<>();
                listData.add(new PlatFormBean(PlatformEnum.SAVE));
                listData.add(new PlatFormBean(PlatformEnum.QQ));
                listData.add(new PlatFormBean(PlatformEnum.WEIXIN));
                new SharePlatformDialog
                        .Builder(MainActivity.this)
                        .setWebUrl("https://www.jianshu.com/p/b343fcff51b0")
                        .setWebUrlTitle("借条管家")
                        .setWebUrlDesc("BRVAH官方使用指南（持续更新）")
                        .setPlatforms(listData)
                        .show();
            }
        });

        findViewById(R.id.btn_send_msg_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialShareUtil.sendMsgToQQ(MainActivity.this, "借条管家");
            }
        });

        findViewById(R.id.btn_send_msg_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialShareUtil.sendMsgToWeixin(MainActivity.this, "借条管家");
            }
        });
        findViewById(R.id.btn_send_msg_by_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialShareUtil.sendMsgBySms(MainActivity.this, "借条管家");
            }
        });
        findViewById(R.id.btn_send_msg_by_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialShareUtil.sendMsgByMail(MainActivity.this, "【主题】", "借条管家");
            }
        });
        findViewById(R.id.btn_send_msg_by_sys).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialShareUtil.sendMsgBySys(MainActivity.this, "【主题】", "借条管家");
            }
        });
    }

}
