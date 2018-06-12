### 功能说明
微信、QQ、微博等分享功能




打开第三方应用：

```java
ComponentName componet = new ComponentName(pkg, cls);
                //pkg 就是第三方应用的包名
                //cls 就是第三方应用的进入的第一个Activity
                Intent intent = new Intent();
                intent.setComponent(componet);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
```

QQ，微博，微信的包名和启动页Activity名称

应用名称 | 包名| Activity名称
:----:|:----:|:-----:|
QQ | com.tencent.mobileqq | com.tencent.mobileqq.activity.SplashActivity|
腾讯微博 | com.tencent.WBlog | com.tencent.WBlog.activity.WBlogFirstRun|
微信 |com.tencent.mm| com.tencent.mm.ui.LauncherUI|
新浪微博 |com.sina.weibo| com.sina.weibo.SplashActivity|

如何获取pkg和cls
```java
Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPackageManager = this.getPackageManager();
        List<ResolveInfo> mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        //按报名排序
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

        for(ResolveInfo res : mAllApps){
            //该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;
            Log.d("pkg_cls","pkg---" +pkg +"  cls---" + cls );

        }
```

