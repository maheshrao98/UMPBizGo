package com.example.umpbizgo.Customer.Order;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.umpbizgo.Seller.Orders.SellerCancelledOrdersFragment;
import com.example.umpbizgo.Seller.Orders.SellerOrderViewFragment;
import com.example.umpbizgo.Seller.Orders.SellerShippedOrdersFragment;

public class CustomerOrderTabsAdapter extends FragmentStatePagerAdapter {
    int mnumTabs;
    public CustomerOrderTabsAdapter(@NonNull FragmentManager fm , int nooftabs) {
        super(fm);
        this.mnumTabs = nooftabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                ViewUnshippedOrdersFragment unshippedorders = new ViewUnshippedOrdersFragment();
                return unshippedorders;
            case 1 :
                ViewShippedOrdersFragment shippedorders = new ViewShippedOrdersFragment();
                return shippedorders;
            case 2 :
                ViewCancelledOrdersFragment canceledorders = new ViewCancelledOrdersFragment();
                return canceledorders;
            case 3 :
                ViewUnpaidOrdersFragment unpaidorders = new ViewUnpaidOrdersFragment();
                return unpaidorders;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mnumTabs;
    }
}
