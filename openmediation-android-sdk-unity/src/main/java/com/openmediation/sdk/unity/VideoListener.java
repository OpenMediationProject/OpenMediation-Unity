// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.unity;

public interface VideoListener {
    void onRewardedVideoAvailabilityChanged(boolean available);

    void onRewardedVideoAdShowed(String scene);

    void onRewardedVideoAdShowFailed(String scene, String error);

    void onRewardedVideoAdClicked(String scene);

    void onRewardedVideoAdClosed(String scene);

    void onRewardedVideoAdStarted(String scene);

    void onRewardedVideoAdEnded(String scene);

    void onRewardedVideoAdRewarded(String scene);
}
