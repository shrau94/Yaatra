package com.tcd.yaatra.di.modules;

import com.tcd.yaatra.ui.fragments.DailyCommuteMapFragment;
import com.tcd.yaatra.ui.fragments.RouteInfoFragment;
import com.tcd.yaatra.ui.fragments.PeerToPeerFragment;
import com.tcd.yaatra.ui.fragments.DailyCommuteDetailsFragment;
import com.tcd.yaatra.ui.fragments.DailyFragment;
import com.tcd.yaatra.ui.fragments.MapFragment;
import com.tcd.yaatra.ui.fragments.OfflineMaps;
import com.tcd.yaatra.ui.fragments.ScheduleDailyCommuteFragment;
import com.tcd.yaatra.ui.fragments.SettingsFragment;
import com.tcd.yaatra.ui.fragments.UserRatingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface FragmentModule {

    @ContributesAndroidInjector
    DailyFragment contributeDailyFragment();

    @ContributesAndroidInjector
    SettingsFragment contributeSettingsFragment();

    @ContributesAndroidInjector
    MapFragment contributeMapFragment();

    @ContributesAndroidInjector
    DailyCommuteMapFragment contributeDailyCommuteMapFragment();

    @ContributesAndroidInjector
    ScheduleDailyCommuteFragment contributeScheduleDailyCommuteFragment();

    @ContributesAndroidInjector
    DailyCommuteDetailsFragment contributeDailyCommuteDetails();

    @ContributesAndroidInjector
    PeerToPeerFragment contributePeerToPeerFragment();

    @ContributesAndroidInjector
    RouteInfoFragment contributeRouteinfo();

    @ContributesAndroidInjector
    OfflineMaps contributeOfflineMaps();

    @ContributesAndroidInjector
    UserRatingFragment contributeUserRatingFragment();
}
