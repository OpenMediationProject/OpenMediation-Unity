// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.unity;

import android.app.Activity;
import android.text.TextUtils;

import com.openmediation.sdk.InitCallback;
import com.openmediation.sdk.InitConfiguration;
import com.openmediation.sdk.OmAds;
import com.openmediation.sdk.interstitial.InterstitialAd;
import com.openmediation.sdk.interstitial.InterstitialAdListener;
import com.openmediation.sdk.promotion.PromotionAd;
import com.openmediation.sdk.promotion.PromotionAdListener;
import com.openmediation.sdk.promotion.PromotionAdRect;
import com.openmediation.sdk.utils.error.Error;
import com.openmediation.sdk.utils.model.Scene;
import com.openmediation.sdk.video.RewardedVideoAd;
import com.openmediation.sdk.video.RewardedVideoListener;
import com.unity3d.player.UnityPlayer;


public class OmBridge {

    /*********Init***********/
    private static final String EVENT_SDK_INIT_SUCCESS = "onSdkInitSuccess";
    private static final String EVENT_SDK_INIT_FAILED = "onSdkInitFailed";

    /*********RewardedVideo**************/
    private static final String EVENT_RV_AVAILABLE_CHANGE = "onRewardedVideoAvailabilityChanged";
    private static final String EVENT_RV_SHOWED = "onRewardedVideoShowed";
    private static final String EVENT_RV_SHOWED_FAILED = "onRewardedVideoShowFailed";
    private static final String EVENT_RV_CLICKED = "onRewardedVideoClicked";
    private static final String EVENT_RV_CLOSED = "onRewardedVideoClosed";
    private static final String EVENT_RV_STARTED = "onRewardedVideoStarted";
    private static final String EVENT_RV_ENDED = "onRewardedVideoEnded";
    private static final String EVENT_RV_REWARDED = "onRewardedVideoRewarded";

    /*********Interstitial**************/
    private static final String EVENT_IS_AVAILABLE_CHANGE = "onInterstitialAvailabilityChanged";
    private static final String EVENT_IS_SHOWED = "onInterstitialShowed";
    private static final String EVENT_IS_SHOWED_FAILED = "onInterstitialShowFailed";
    private static final String EVENT_IS_CLICKED = "onInterstitialClicked";
    private static final String EVENT_IS_CLOSED = "onInterstitialClosed";

    /*********Promotion**************/
    private static final String EVENT_CP_AVAILABLE_CHANGE = "onPromotionAdAvailabilityChanged";
    private static final String EVENT_CP_SHOWED = "onPromotionAdShowed";
    private static final String EVENT_CP_SHOWED_FAILED = "onPromotionAdShowFailed";
    private static final String EVENT_CP_CLICKED = "onPromotionAdClicked";
    private static final String EVENT_CP_HIDDEN = "onPromotionAdHidden";

    public static void init(String appkey) {
        OmAds.init(getActivity(), new InitConfiguration.Builder()
                .appKey(appkey)
                .build(), new InitListener());
    }

    public static void init(String appkey, String host) {
        OmAds.init(getActivity(), new InitConfiguration.Builder()
                .appKey(appkey)
                .initHost(host)
                .build(), new InitListener());
    }

    public static void init(String appkey, String host, String channel) {
        OmAds.init(getActivity(), new InitConfiguration.Builder()
                .appKey(appkey)
                .initHost(host)
                .channel(channel)
                .build(), new InitListener());
    }

    public static boolean isInit() {
        return OmAds.isInit();
    }

    public static void sendAFConversionData(String conversionData) {
        OmAds.sendAFConversionData(conversionData);
    }

    public static void sendAFDeepLinkData(String conversionData) {
        OmAds.sendAFDeepLinkData(conversionData);
    }

    public static void Debug(boolean debug) {
        OmAds.setLogEnable(debug);
    }

    public static void setIAP(float count, String currency) {
        OmAds.setIAP(count, currency);
    }

    public static void setGDPRConsent(boolean consent) {
        OmAds.setGDPRConsent(consent);
    }

    public static void setAgeRestricted(boolean restricted) {
        OmAds.setAgeRestricted(restricted);
    }

    public static void setUserAge(int age) {
        OmAds.setUserAge(age);
    }

    public static void setUserGender(String gender) {
        OmAds.setUserGender(gender);
    }

    public static void setUSPrivacyLimit(boolean value) {
        OmAds.setUSPrivacyLimit(value);
    }

    public static boolean getGDPRConsent() {
        Boolean result = OmAds.getGDPRConsent();
        if (result == null) {
            return false;
        }
        return result;
    }

    public static void setRewardedVideoExtId(String extId) {
        setRewardedVideoExtId("", extId);
    }

    public static void setRewardedVideoExtId(String scene, String extId) {
        if (!TextUtils.isEmpty(extId)) {
            RewardedVideoAd.setExtId(scene, extId);
        }
    }

    public static void showRewardedVideo() {
        RewardedVideoAd.showAd();
    }

    public static void showRewardedVideo(String scene) {
        RewardedVideoAd.showAd(scene);
    }

    public static boolean isRewardedVideoReady() {
        return RewardedVideoAd.isReady();
    }

    public static void showInterstitial() {
        InterstitialAd.showAd();
    }

    public static void showInterstitial(String scene) {
        InterstitialAd.showAd(scene);
    }

    public static boolean isInterstitialReady() {
        return InterstitialAd.isReady();
    }

    public static void loadBanner(String placementId, int sizeType, int position) {
        BannerSingleTon.getInstance().loadBanner(getActivity(), placementId, sizeType, position);
    }

    public static void destroyBanner(String placementId) {
        BannerSingleTon.getInstance().destroyBanner(placementId);
    }

    public static void displayBanner(String placementId) {
        BannerSingleTon.getInstance().displayBanner(placementId);
    }

    public static void hideBanner(String placementId) {
        BannerSingleTon.getInstance().hideBanner(placementId);
    }

    public static boolean isPromotionAdReady() {
        return PromotionAd.isReady();
    }

    public static void showPromotionAd(int width, int height, float scaleX, float scaleY, float angle) {
        PromotionAdRect rect = new PromotionAdRect();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setScaleX(scaleX);
        rect.setScaleY(scaleY);
        rect.setAngle(angle);
        PromotionAd.showAd(getActivity(), rect);
    }

    public static void showPromotionAd(String scene, int width, int height, float scaleX, float scaleY, float angle) {
        PromotionAdRect rect = new PromotionAdRect();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setScaleX(scaleX);
        rect.setScaleY(scaleY);
        rect.setAngle(angle);
        PromotionAd.showAd(getActivity(), rect, scene);
    }

    public static void hidePromotionAd() {
        PromotionAd.hideAd();
    }

    private static Activity getActivity() {
        return UnityPlayer.currentActivity;
    }

    static void sendUnityEvent(String event) {
        sendUnityEvent(event, "");
    }

    static void sendUnityEvent(String event, String params) {
        try {
            if (getActivity() != null) {
                String paramsStr;
                if (TextUtils.isEmpty(params)) {
                    paramsStr = "";
                } else {
                    paramsStr = params;
                }
                UnityPlayer.UnitySendMessage("OmEvents", event, paramsStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class RvListener implements RewardedVideoListener {

        @Override
        public void onRewardedVideoAvailabilityChanged(boolean available) {
            sendUnityEvent(EVENT_RV_AVAILABLE_CHANGE, String.valueOf(available));
        }

        @Override
        public void onRewardedVideoAdShowed(Scene scene) {
            sendUnityEvent(EVENT_RV_SHOWED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onRewardedVideoAdShowFailed(Scene scene, Error error) {
            sendUnityEvent(EVENT_RV_SHOWED_FAILED, (scene != null ? scene.getN() : "").concat(error != null ? error.toString() : ""));
        }

        @Override
        public void onRewardedVideoAdClicked(Scene scene) {
            sendUnityEvent(EVENT_RV_CLICKED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onRewardedVideoAdClosed(Scene scene) {
            sendUnityEvent(EVENT_RV_CLOSED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onRewardedVideoAdStarted(Scene scene) {
            sendUnityEvent(EVENT_RV_STARTED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onRewardedVideoAdEnded(Scene scene) {
            sendUnityEvent(EVENT_RV_ENDED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onRewardedVideoAdRewarded(Scene scene) {
            sendUnityEvent(EVENT_RV_REWARDED, scene != null ? scene.getN() : "");
        }
    }

    private static class IsListener implements InterstitialAdListener {
        @Override
        public void onInterstitialAdAvailabilityChanged(boolean available) {
            sendUnityEvent(EVENT_IS_AVAILABLE_CHANGE, String.valueOf(available));
        }

        @Override
        public void onInterstitialAdShowed(Scene scene) {
            sendUnityEvent(EVENT_IS_SHOWED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onInterstitialAdShowFailed(Scene scene, Error error) {
            sendUnityEvent(EVENT_IS_SHOWED_FAILED, (scene != null ? scene.getN() : "").concat(error != null ? error.toString() : ""));
        }

        @Override
        public void onInterstitialAdClosed(Scene scene) {
            sendUnityEvent(EVENT_IS_CLOSED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onInterstitialAdClicked(Scene scene) {
            sendUnityEvent(EVENT_IS_CLICKED, scene != null ? scene.getN() : "");
        }
    }

    private static class CpListener implements PromotionAdListener {

        @Override
        public void onPromotionAdAvailabilityChanged(boolean available) {
            sendUnityEvent(EVENT_CP_AVAILABLE_CHANGE, String.valueOf(available));
        }

        @Override
        public void onPromotionAdShowed(Scene scene) {
            sendUnityEvent(EVENT_CP_SHOWED, scene != null ? scene.getN() : "");
        }

        @Override
        public void onPromotionAdShowFailed(Scene scene, Error error) {
            sendUnityEvent(EVENT_CP_SHOWED_FAILED, (scene != null ? scene.getN() : "").concat(error != null ? error.toString() : ""));
        }

        @Override
        public void onPromotionAdHidden(Scene scene) {
            sendUnityEvent(EVENT_CP_HIDDEN, scene != null ? scene.getN() : "");
        }

        @Override
        public void onPromotionAdClicked(Scene scene) {
            sendUnityEvent(EVENT_CP_CLICKED, scene != null ? scene.getN() : "");
        }
    }

    private static class InitListener implements InitCallback {
        @Override
        public void onSuccess() {
            sendUnityEvent(EVENT_SDK_INIT_SUCCESS);
            RewardedVideoAd.setAdListener(new RvListener());
            InterstitialAd.setAdListener(new IsListener());
            PromotionAd.setAdListener(new CpListener());
        }

        @Override
        public void onError(Error result) {
            String error = result != null ? result.toString() : "OM init failed";
            sendUnityEvent(EVENT_SDK_INIT_FAILED, error);
        }
    }
}
