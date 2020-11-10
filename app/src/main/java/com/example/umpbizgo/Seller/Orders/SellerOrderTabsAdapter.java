package com.example.umpbizgo.Seller.Orders;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SellerOrderTabsAdapter extends FragmentStatePagerAdapter {
    int mnumTabs;
    public SellerOrderTabsAdapter(@NonNull FragmentManager fm, int nooftabs) {
        super(fm);
        this.mnumTabs = nooftabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                SellerOrderViewFragment unprocessedorders = new SellerOrderViewFragment();
                return unprocessedorders;
            case 1 :
                SellerShippedOrdersFragment shippedorders = new SellerShippedOrdersFragment();
                return shippedorders;
            case 2 :
                SellerCancelledOrdersFragment cancelledorders = new SellerCancelledOrdersFragment();
                return cancelledorders;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mnumTabs;
    }
}
