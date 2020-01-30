package com.tcd.yaatra.di.modules;

import com.tcd.yaatra.ui.activities.DailyCommuteActivity;
import com.tcd.yaatra.ui.activities.LoginActivity;

import com.tcd.yaatra.ui.activities.DailyCommuteListActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ActivityModule {

    @ContributesAndroidInjector
    LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector
    DailyCommuteActivity contributetDailyCommuteActivity();

    @ContributesAndroidInjector
    DailyCommuteListActivity contributeUserActivity();
}