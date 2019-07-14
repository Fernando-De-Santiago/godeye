package cn.hikyson.godeye.core.internal.modules.pageload;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.v4.app.Fragment;

import cn.hikyson.godeye.core.internal.Install;
import cn.hikyson.godeye.core.internal.ProduceableSubject;
import cn.hikyson.godeye.core.utils.L;

/**
 * 页面加载模块
 * 安装卸载可以任意线程
 * 发射数据在主线程
 * Created by kysonchao on 2018/1/25.
 */
public class Pageload2 extends ProduceableSubject<PageLifecycleEventInfo> implements Install<PageloadContext> {
    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private PageloadContext mConfig;
    private boolean mInstalled = false;
    private Handler mHandler;

    @Override
    public synchronized void install(PageloadContext config) {
        if (mInstalled) {
            L.d("pageload already installed, ignore.");
            return;
        }
        this.mConfig = config;
        HandlerThread handlerThread = new HandlerThread("godeye-pageload-main", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
        PageLifecycleRecords pageLifecycleRecords = new PageLifecycleRecords();
        // TODO KYSON IMPL
        PageInfoProvider pageInfoProvider = new DefaultPageInfoProvider();
        this.mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks(pageLifecycleRecords, pageInfoProvider, this, mHandler);
        this.mConfig.application().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        this.mInstalled = true;
        L.d("pageload installed.");
    }

    @Override
    public synchronized void uninstall() {
        if (!mInstalled) {
            L.d("pageload already uninstalled, ignore.");
            return;
        }
        this.mConfig.application().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        mActivityLifecycleCallbacks = null;
        this.mHandler.getLooper().quit();
        this.mHandler = null;
        this.mInstalled = false;
        L.d("pageload uninstalled.");
    }

    public synchronized void onActivityLoad(Activity activity) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityLoad(activity);
        }
    }

    public synchronized void onFragmentV4Show(Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentV4Show(f);
        }
    }

    public synchronized void onFragmentV4Hide(Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentV4Hide(f);
        }
    }

    public synchronized void onFragmentV4Load(Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentV4Load(f);
        }
    }

    public synchronized void onFragmentShow(android.app.Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentShow(f);
        }
    }

    public synchronized void onFragmentHide(android.app.Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentHide(f);
        }
    }

    public synchronized void onFragmentLoad(android.app.Fragment f) {
        if (mInstalled && mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onFragmentLoad(f);
        }
    }

}