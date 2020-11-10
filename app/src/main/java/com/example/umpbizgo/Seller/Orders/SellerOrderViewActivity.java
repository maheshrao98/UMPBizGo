package com.example.umpbizgo.Seller.Orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.SellerHomeActivity;
import com.google.android.material.tabs.TabLayout;

public class SellerOrderViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_view);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar_seller);
        toolbar.setTitle("My Orders");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        FragmentManager ft2 = getSupportFragmentManager();
                        final FragmentTransaction sellerFrag = ft2.beginTransaction();
                        final LogOutFragment fragsellermyorders = new LogOutFragment();
                        sellerFrag.add(R.id.frameLayoutorderseller, fragsellermyorders).commit();
                        break;
                    case R.id.sellerhome:
                        Intent productview = new Intent(SellerOrderViewActivity.this, SellerHomeActivity.class);
                        startActivity(productview);
                        break;
                }
                return false;
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_order);
        tabLayout.addTab(tabLayout.newTab().setText("Unprocessed"));
        tabLayout.addTab(tabLayout.newTab().setText("Shipped"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager_order);
        SellerOrderTabsAdapter tabsAdapter = new SellerOrderTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}