package com.example.umpbizgo.Customer.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.umpbizgo.Customer.CartFragment;
import com.example.umpbizgo.Customer.HomeActivity;
import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Orders.SellerOrderTabsAdapter;
import com.google.android.material.tabs.TabLayout;

public class CustomerOrderViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_view);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.home_logout_bar);
        toolbar.setTitle("Orders");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout2:
                        FragmentManager ft4 = getSupportFragmentManager();
                        final FragmentTransaction cuorder2Frag = ft4.beginTransaction();
                        LogOutFragment fragcustomerorder2 = new LogOutFragment();
                        cuorder2Frag.add(R.id.frameLayoutordercustomer,fragcustomerorder2).commit();
                        break;
                }
                return false;
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_order_customer);
        tabLayout.addTab(tabLayout.newTab().setText("Unshipped"));
        tabLayout.addTab(tabLayout.newTab().setText("Shipped"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending Payment"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager_order_customer);
        CustomerOrderTabsAdapter tabsAdapter = new CustomerOrderTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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