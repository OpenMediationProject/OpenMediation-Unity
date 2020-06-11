// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.unity;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.openmediation.sdk.InitCallback;
import com.openmediation.sdk.OmAds;
import com.openmediation.sdk.interstitial.InterstitialAd;
import com.openmediation.sdk.interstitial.InterstitialAdListener;
import com.openmediation.sdk.utils.AdLog;
import com.openmediation.sdk.utils.DeveloperLog;
import com.openmediation.sdk.utils.error.Error;
import com.openmediation.sdk.utils.model.Scene;
import com.openmediation.sdk.video.RewardedVideoAd;
import com.openmediation.sdk.video.RewardedVideoListener;


public class OmBridge {

    private static boolean isDebug = false;

    private static String TAG = "OmBridgeAPI";

    // ------------------------------------ sdk init ------------------------------------
    public static void init(Activity activity, String appkey) {
        LogD("init(appkey):" + appkey);
        init(activity, appkey, null);
    }

    public static void init(Activity activity, String appkey, final InitListener callback) {
        LogD("init(appkey,callback):" + appkey);
        OmAds.init(activity, appkey, new InitCallback() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(Error result) {
                if (callback != null) {
                    callback.onError(result.toString());
                }
            }
        });
    }

    public static boolean isInit() {
        boolean init = OmAds.isInit();
        LogD("isInit:" + init);
        return init;
    }

    public static void setIAP(float count, String currency) {
        OmAds.setIAP(count, currency);
    }

    // ------------------------------------ RewardedVideo ------------------------------------
    public static void setRewardedVideoListener(final VideoListener videoListener) {
        LogD("setRewardedVideoListener");
        RewardedVideoAd.setAdListener(new RewardedVideoListener() {
            @Override
            public void onRewardedVideoAvailabilityChanged(boolean available) {
                videoListener.onRewardedVideoAvailabilityChanged(available);
            }

            @Override
            public void onRewardedVideoAdShowed(Scene scene) {
                videoListener.onRewardedVideoAdShowed(scene.getN());
            }

            @Override
            public void onRewardedVideoAdShowFailed(Scene scene, Error error) {
                videoListener.onRewardedVideoAdShowFailed(scene.getN(), error.toString());
            }

            @Override
            public void onRewardedVideoAdClicked(Scene scene) {
                videoListener.onRewardedVideoAdClicked(scene.getN());
            }

            @Override
            public void onRewardedVideoAdClosed(Scene scene) {
                videoListener.onRewardedVideoAdClosed(scene.getN());
            }

            @Override
            public void onRewardedVideoAdStarted(Scene scene) {
                videoListener.onRewardedVideoAdStarted(scene.getN());
            }

            @Override
            public void onRewardedVideoAdEnded(Scene scene) {
                videoListener.onRewardedVideoAdEnded(scene.getN());
            }

            @Override
            public void onRewardedVideoAdRewarded(Scene scene) {
                videoListener.onRewardedVideoAdRewarded(scene.getN());
            }
        });
    }

    public static void setExtId(String extId) {
        setExtId("", extId);
    }


    public static void setExtId(String scene, String extId) {
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

    // ------------------------------------ Interstitial ------------------------------------
    public static void setInterstitialListener(final InterstitialListener interstitialAdListener) {
        LogD("setInterstitialListener");
        InterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialAdAvailabilityChanged(boolean available) {
                interstitialAdListener.onInterstitialAdAvailabilityChanged(available);
            }

            @Override
            public void onInterstitialAdShowed(Scene scene) {
                interstitialAdListener.onInterstitialAdShowed(scene.getN());
            }

            @Override
            public void onInterstitialAdShowFailed(Scene scene, Error error) {
                interstitialAdListener.onInterstitialAdShowFailed(scene.getN(), error.toString());
            }

            @Override
            public void onInterstitialAdClosed(Scene scene) {
                interstitialAdListener.onInterstitialAdClosed(scene.getN());
            }

            @Override
            public void onInterstitialAdClicked(Scene scene) {
                interstitialAdListener.onInterstitialAdClicked(scene.getN());
            }
        });
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

    public static void Debug(boolean debug) {
        Log.d(TAG, "Debug:" + debug);
        isDebug = debug;
        AdLog.getSingleton().isDebug(true);
        DeveloperLog.enableDebug(null, true);
    }

    private static void LogD(String info) {
        if (isDebug) {
            Log.d(TAG, info);
        }
    }

    private static void LogE(String info) {
        if (isDebug) {
            Log.e(TAG, info);
        }
    }

}
