// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.unity;

public interface InterstitialListener {
    void onInterstitialAdAvailabilityChanged(boolean available);

    void onInterstitialAdShowed(String scene);

    void onInterstitialAdShowFailed(String scene, String error);

    void onInterstitialAdClosed(String scene);

    void onInterstitialAdClicked(String scene);
}
