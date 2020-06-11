// Copyright 2020 ADTIMING TECHNOLOGY COMPANY LIMITED
// Licensed under the GNU Lesser General Public License Version 3

package com.openmediation.sdk.unity;

public interface InitListener {

    /**
     * called upon SDK init success
     */
    void onSuccess();

    /**
     * called upon SDK init failure
     */
    void onError(String error);
}
