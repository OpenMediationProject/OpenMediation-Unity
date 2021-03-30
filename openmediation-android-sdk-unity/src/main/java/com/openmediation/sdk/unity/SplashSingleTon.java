package com.openmediation.sdk.unity;

import android.view.View;
import android.widget.FrameLayout;

import com.openmediation.sdk.splash.SplashAd;
import com.openmediation.sdk.splash.SplashAdListener;
import com.openmediation.sdk.utils.DeveloperLog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SplashSingleTon {
    /*********SplashAd**************/
    private static final String EVENT_SPLASH_LOADED = "onSplashAdLoadSuccess";
    private static final String EVENT_SPLASH_LOAD_FAILED = "onSplashAdLoadFailed";
    private static final String EVENT_SPLASH_SHOWED = "onSplashAdShowed";
    private static final String EVENT_SPLASH_SHOW_FAILED = "onSplashAdShowFailed";
    private static final String EVENT_SPLASH_CLICKED = "onSplashAdClick";
    private static final String EVENT_SPLASH_TICKED = "onSplashAdTicked";
    private static final String EVENT_SPLASH_DISMISSED = "onSplashAdClosed";

    private Map<String, SplashListener> listenerMap;
    private FrameLayout mSplashContainer;

    private static class SplashHolder {
        private static final SplashSingleTon INSTANCE = new SplashSingleTon();
    }

    private SplashSingleTon() {
        listenerMap = new ConcurrentHashMap<>();
    }

    public static SplashSingleTon getInstance() {
        return SplashHolder.INSTANCE;
    }

    private void setListener(String placementId, SplashListener listener) {
        listenerMap.put(placementId, listener);
        SplashAd.setSplashAdListener(placementId, listener);
    }

    private boolean containsListener(String placementId) {
        return listenerMap.containsKey(placementId);
    }

    private void removeListener(String placementId) {
        listenerMap.remove(placementId);
    }

    public void loadSplashAd(String placementId) {
        if (!containsListener(placementId)) {
            setListener(placementId, new SplashListener());
        }
        SplashAd.loadAd(placementId);
    }

    public boolean isSplashAdReady(String placementId) {
        return SplashAd.isReady(placementId);
    }

    public void showSplashAd(String placementId) {
        SplashAd.showAd(OmBridge.getActivity(), placementId, mSplashContainer);
    }

    private void initContainer() {
        if (mSplashContainer == null) {
            mSplashContainer = new FrameLayout(OmBridge.getActivity().getApplicationContext());
            mSplashContainer.setBackgroundColor(0);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            OmBridge.getActivity().addContentView(mSplashContainer, layoutParams);
        } else {
            mSplashContainer.removeAllViews();
            mSplashContainer.setVisibility(View.VISIBLE);
        }
    }

    private void releaseSplash() {
        if (mSplashContainer != null) {
            mSplashContainer.removeAllViews();
            mSplashContainer.setVisibility(View.GONE);
        }
    }

    private static class SplashListener implements SplashAdListener {

        @Override
        public void onSplashAdLoad(String placementId) {
            DeveloperLog.LogD("Unity plugin onSplashAdLoad");
            getInstance().initContainer();
            OmBridge.sendUnityEvent(EVENT_SPLASH_LOADED, placementId);
        }

        @Override
        public void onSplashAdFailed(String placementId, String error) {
            DeveloperLog.LogD("Unity plugin onSplashAdFailed : " + error);
            OmBridge.sendUnityEvent(EVENT_SPLASH_LOAD_FAILED, placementId);
        }

        @Override
        public void onSplashAdClicked(String placementId) {
            DeveloperLog.LogD("Unity plugin onSplashAdClicked");
            OmBridge.sendUnityEvent(EVENT_SPLASH_CLICKED, placementId);
        }

        @Override
        public void onSplashAdShowed(String placementId) {
            DeveloperLog.LogD("Unity plugin onSplashAdShowed");
            OmBridge.sendUnityEvent(EVENT_SPLASH_SHOWED, placementId);
        }

        @Override
        public void onSplashAdShowFailed(String placementId, String error) {
            DeveloperLog.LogD("Unity plugin onSplashAdShowFailed ： " + error);
            getInstance().releaseSplash();
            OmBridge.sendUnityEvent(EVENT_SPLASH_SHOW_FAILED, placementId);
        }

        @Override
        public void onSplashAdTick(String placementId, long millisUntilFinished) {
            DeveloperLog.LogD("Unity plugin onSplashAdTick");
            OmBridge.sendUnityEvent(EVENT_SPLASH_TICKED, placementId);
        }

        @Override
        public void onSplashAdDismissed(String placementId) {
            DeveloperLog.LogD("Unity plugin onSplashAdDismissed");
            getInstance().releaseSplash();
            OmBridge.sendUnityEvent(EVENT_SPLASH_DISMISSED, placementId);
        }
    }
}
