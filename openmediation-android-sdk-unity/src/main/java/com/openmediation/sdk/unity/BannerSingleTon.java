// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.unity;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.openmediation.sdk.banner.AdSize;
import com.openmediation.sdk.banner.BannerAd;
import com.openmediation.sdk.banner.BannerAdListener;

import java.util.HashMap;
import java.util.Map;

final class BannerSingleTon {

    /*********Banner*****************/
    private static final String EVENT_BANNER_READY = "onBannerLoadSuccess";
    private static final String EVENT_BANNER_FAILED = "onBannerLoadFailed";
    private static final String EVENT_BANNER_CLICKED = "onBannerClicked";

    private Map<String, BannerAd> mBannerAds;

    private FrameLayout mBannerContainer;
    private Map<String, FrameLayout> mBannerViews;
    private Handler mHandler;

    private static final class BannerHolder {
        private static final BannerSingleTon INSTANCE = new BannerSingleTon();
    }

    private BannerSingleTon() {
        mBannerAds = new HashMap<>();
        mBannerViews = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
    }

    static BannerSingleTon getInstance() {
        return BannerHolder.INSTANCE;
    }

    void loadBanner(Activity activity, String placementId, int sizeType, int position) {
        BannerAd bannerAd = mBannerAds.get(placementId);
        if (bannerAd == null) {
            bannerAd = new BannerAd(activity, placementId, new BannerAdsListener(placementId, position));
            mBannerAds.put(placementId, bannerAd);
        }
        bannerAd.setAdSize(getBannerSize(sizeType));
        loadBannerAds(activity, bannerAd);
    }

    void destroyBanner(final String placementId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mBannerContainer != null) {
                    mBannerContainer.removeAllViews();
                }

                if (mBannerViews != null) {
                    mBannerViews.remove(placementId);
                }
                BannerAd bannerAd = mBannerAds.get(placementId);
                if (bannerAd == null) {
                    return;
                }
                mBannerAds.remove(placementId);
                bannerAd.destroy();
            }
        });
    }

    void displayBanner(String placementId) {
        synchronized (this) {
            final FrameLayout bannerView = mBannerViews.get(placementId);
            if (bannerView == null) {
                Log.d("AdTiming-Unity", "Banner ad is not ready to display, please load before display");
                return;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    bannerView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    void hideBanner(String placemenId) {
        synchronized (this) {
            final FrameLayout bannerView = mBannerViews.get(placemenId);
            if (bannerView == null) {
                Log.d("AdTiming-Unity", "no ad need to be hide");
                return;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    bannerView.setVisibility(View.GONE);
                }
            });
        }
    }

    private void loadBannerAds(final Activity activity, final BannerAd bannerAd) {
        synchronized (this) {
            if (mBannerContainer != null) {
                bannerAd.loadAd();
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (OmBridge.class) {
                            if (mBannerContainer == null) {
                                mBannerContainer = new FrameLayout(activity);
                                mBannerContainer.setBackgroundColor(0);
                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
                                activity.addContentView(mBannerContainer, params);
                            }
                            bannerAd.loadAd();
                        }
                    }
                });
            }
        }
    }

    private static AdSize getBannerSize(int sizeType) {
        switch (sizeType) {
            case 0:
                return AdSize.BANNER;
            case 1:
                return AdSize.MEDIUM_RECTANGLE;
            case 2:
                return AdSize.LEADERBOARD;
            case 3:
                return AdSize.SMART;
            default:
                return AdSize.BANNER;
        }
    }

    private static class BannerAdsListener implements BannerAdListener {

        private int mPosition;
        private String mPlacementId;


        BannerAdsListener(String placementId, int position) {
            mPosition = position;
            mPlacementId = placementId;
        }

        @Override
        public void onAdReady(View view) {
            FrameLayout bannerView = (FrameLayout) view;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = mPosition == 1 ? Gravity.TOP : Gravity.BOTTOM;
            getInstance().mBannerContainer.removeAllViews();
            getInstance().mBannerContainer.addView(bannerView, layoutParams);
            getInstance().mBannerViews.put(mPlacementId, bannerView);
            OmBridge.sendUnityEvent(EVENT_BANNER_READY);
        }

        @Override
        public void onAdFailed(String error) {
            OmBridge.sendUnityEvent(EVENT_BANNER_FAILED, error);
        }

        @Override
        public void onAdClicked() {
            OmBridge.sendUnityEvent(EVENT_BANNER_CLICKED);
        }
    }
}
