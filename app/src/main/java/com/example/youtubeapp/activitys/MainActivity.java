package com.example.youtubeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.youtubeapp.R;
import com.example.youtubeapp.fragment.ShortsFragment;
import com.example.youtubeapp.fragment.HomeFragment;
import com.example.youtubeapp.fragment.LibraryFragment;
import com.example.youtubeapp.fragment.NotificationFragment;
import com.example.youtubeapp.fragment.SearchFragment;
import com.example.youtubeapp.fragment.SearchResultsFragment;
import com.example.youtubeapp.fragment.SubcriptionFragment;
import com.example.youtubeapp.utiliti.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    Toolbar tbNav;
    BottomNavigationView bnvFragment;
    FrameLayout flContent;
    SwipeRefreshLayout srlReloadData;
    HomeFragment homeFragment;
    AppBarLayout ablHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tbNav = findViewById(R.id.tb_nav);
        tbNav.setVisibility(View.VISIBLE);
        bnvFragment = findViewById(R.id.bnv_fragment);
        bnvFragment.setVisibility(View.VISIBLE);
        flContent = findViewById(R.id.fl_content);
        srlReloadData = findViewById(R.id.srl_reload);
        ablHome = findViewById(R.id.abl_nav);
        srlReloadData.setOnRefreshListener(this);
        homeFragment = new HomeFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_content, homeFragment, "fragHome");
        fragmentTransaction.addToBackStack(HomeFragment.TAG);
        fragmentTransaction.commit();
        bnvFragment.getMenu().findItem(R.id.mn_home).setChecked(true);
        // Sự kiện click thanh menu bottom
        bnvFragment.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_home:
                        srlReloadData.setEnabled(true);
                        setToolBarMainVisible();
                        if (item.isChecked()) {
                            homeFragment = (HomeFragment) getSupportFragmentManager()
                                    .findFragmentByTag("fragHome");
                            homeFragment.topRecycleView();
                        } else {
                            item.setChecked(true);
                            getSupportFragmentManager().popBackStack(HomeFragment.TAG, 0);
                        }
                        break;
                    case R.id.mn_explore:
                        tbNav.setVisibility(View.GONE);
                        srlReloadData.setEnabled(false);
                        ShortsFragment exploreFragment = new ShortsFragment();
                        item.setChecked(true);
                        selectFragment(exploreFragment);
                        break;
                    case R.id.mn_subcription:
                        SubcriptionFragment subcriptionFragment = new SubcriptionFragment();
                        item.setChecked(true);
                        selectFragment(subcriptionFragment);
                        break;
                    case R.id.mn_notification:
                        NotificationFragment notificationFragment = new NotificationFragment();
                        item.setChecked(true);
                        selectFragment(notificationFragment);
                        break;
                    case R.id.mn_library:
                        LibraryFragment libraryFragment = new LibraryFragment();
                        item.setChecked(true);
                        selectFragment(libraryFragment);
                        break;
                }
                return false;
            }
        });

        tbNav.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_search:
                        addFragmentSearch("");

                        break;

                }
                return false;
            }
        });

    }

    // Phương thức chọn fragment khi click menu
    private void selectFragment(Fragment fragment) {
        Random random = new Random();
        float floatNumber = random.nextFloat();
        int intNumber = random.nextInt();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.addToBackStack(floatNumber + intNumber + "9");
        fragmentTransaction.commit();
    }

    // KHi load lại trang home
    @Override
    public void onRefresh() {
        tbNav.setVisibility(View.VISIBLE);
        HomeFragment homeFragment1 = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, homeFragment1, "fragHome");
        fragmentTransaction.addToBackStack(HomeFragment.TAG);
        fragmentTransaction.commit();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                srlReloadData.setRefreshing(false);
            }
        }, 1000);
    }

    public void addFragmentSearch(String s) {
        bnvFragment.setVisibility(View.GONE);
        tbNav.setVisibility(View.GONE);
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Util.BUNDLE_EXTRA_TEXT_EDITTEXT, s);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        searchFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_content, searchFragment, "fragSearch");
        fragmentTransaction.addToBackStack("SearchFragment");
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("fragHome");
        if (fragment != null && fragment.isVisible()) {
            setToolBarMainVisible();
        }

    }

    public void addFragmentSearchResults(String q) {
        tbNav.setVisibility(View.GONE);
        bnvFragment.setVisibility(View.VISIBLE);
//        removeFragmentSearch();
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        getSupportFragmentManager().popBackStack();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Util.BUNDLE_EXTRA_Q, q);
        searchResultsFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fl_content, searchResultsFragment, "fragSearchRe");
        fragmentTransaction.addToBackStack("SearchFragmentRe");
        fragmentTransaction.commit();
    }

//    public void addFragmentChannel(String idChannel, String titleChannel) {
//        tbNav.setVisibility(View.GONE);
//        bnvFragment.setVisibility(View.VISIBLE);
////        removeFragmentSearch();
//        ChannelDetailFragment channelDetailFragment = new ChannelDetailFragment();
////        getSupportFragmentManager().popBackStack();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        Bundle bundle = new Bundle();
//        bundle.putString("ID_CHANNEL", idChannel);
//        bundle.putString("TITLE_CHANNEL", titleChannel);
//        channelDetailFragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.fl_content, channelDetailFragment, "fragChannelDetail");
//        fragmentTransaction.addToBackStack("Channel");
//        fragmentTransaction.commit();
//    }


//    private void removeFragmentSearch() {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        SearchFragment searchFragment
//                = (SearchFragment) getSupportFragmentManager().findFragmentByTag("fragSearch");
//        if (searchFragment != null) {
//            transaction.remove(searchFragment);
//            transaction.commit();
//        } else {
//            Toast.makeText(this,
//                    "Không có fragment để xóa",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }


    public void setToolBarMainVisible() {
        bnvFragment.setVisibility(View.VISIBLE);
        tbNav.setVisibility(View.VISIBLE);
        Log.d("fjdsal","success");
    }
}