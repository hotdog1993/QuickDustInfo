package com.example.quickdustinfo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.quickdustinfo.common.AddLocationDialogFragment;
import com.example.quickdustinfo.finedust.FineDustContract;
import com.example.quickdustinfo.finedust.FineDustFragment;
import com.example.quickdustinfo.util.GeoUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FINE_COARSE_PERMISION = 1000;
    private AppBarConfiguration mAppBarConfiguration;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Pair<Fragment, String>> mFragmentList;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLocationDialogFragment.newInstance(new AddLocationDialogFragment.OnClickListener() {
                    @Override
                    public void onOkClicked(final String city) {
                        GeoUtil.getLocationFromName(MainActivity.this, city, new GeoUtil.GeoUtilListener() {
                            @Override
                            public void onSuccess(double lat, double lng) {
                                addNewFragment(lat, lng, city);
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show(getSupportFragmentManager(), "dialog");
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setUpViewPager();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();*/
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
    }

    private void addNewFragment(double lat, double lng, String city) {
        mFragmentList.add(new Pair<Fragment, String>(
                FineDustFragment.newInstance(lat, lng), city
        ));
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setUpViewPager() {     //뷰페이저 초기화
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        loadDbData();
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void loadDbData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new Pair<Fragment, String>(
                new FineDustFragment(), "현재 위치"
        ));
    }

    public void getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_FINE_COARSE_PERMISION);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            FineDustContract.View view = (FineDustContract.View) mFragmentList.get(0).first;
                            view.reload(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_FINE_COARSE_PERMISION) {
            if(grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation();
            }
        }
    }

    private static class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Pair<Fragment, String>> mFragmentList;
        public MyPagerAdapter(@NonNull FragmentManager fm, List<Pair<Fragment, String>> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position).first;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentList.get(position).second;
        }
    }
}