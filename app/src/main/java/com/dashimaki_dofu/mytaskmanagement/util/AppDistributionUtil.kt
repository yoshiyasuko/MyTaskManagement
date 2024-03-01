package com.dashimaki_dofu.mytaskmanagement.util

import com.google.firebase.appdistribution.FirebaseAppDistribution
import javax.inject.Inject
import javax.inject.Singleton


/**
 * AppDistributionUtil
 *
 * Created by Yoshiyasu on 2024/03/02
 */

@Singleton
class AppDistributionUtil @Inject constructor(
    private val appDistribution: FirebaseAppDistribution
) {
    fun updateIfNewReleaseAvailable() {
        appDistribution.updateIfNewReleaseAvailable()
    }
}
