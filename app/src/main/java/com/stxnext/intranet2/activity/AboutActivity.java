package com.stxnext.intranet2.activity;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.adapter.OfficeInfoPagerAdapter;
import com.stxnext.intranet2.model.Office;

/**
 * Created by Tomasz Konieczny on 2015-06-11.
 */
public class AboutActivity extends AppCompatActivity {

    private OfficeInfoPagerAdapter fragmentAdapter;
    private GoogleMap map;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentAdapter = new OfficeInfoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.sliding_tabs);
        tabStrip.setTabIndicatorColor(getResources().getColor(R.color.stxnext_green));
        tabStrip.setTextColor(getResources().getColor(R.color.stxnext_green_dark));

        prepareMap();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Office office = fragmentAdapter.getOffice(position);
                animateMap(office);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    private void prepareMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_fragment);
        if (fragment != null) {
            map = fragment.getMap();
            if (map != null) {
                Office[] offices = Office.values();
                for (Office office : offices) {
                    MarkerOptions options = new MarkerOptions();
                    LatLng position = new LatLng(office.getLat(), office.getLon());
                    options.position(position);
                    map.addMarker(options);
                }

                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                map.getUiSettings().setAllGesturesEnabled(false);

                animateMap(offices[0]);
            }
        }
    }

    private void animateMap(Office office) {
        if (map != null) {
            LatLng cameraPosition = new LatLng(office.getLat() + 0.02, office.getLon());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, 12), 2000, null);
        }
    }

    @VisibleForTesting
    public String getPageTitle() {
        return fragmentAdapter.getPageTitle(viewPager.getCurrentItem()).toString();
    }

    @VisibleForTesting
    public Office getOffice() {
        return fragmentAdapter.getOffice(viewPager.getCurrentItem());
    }

}
