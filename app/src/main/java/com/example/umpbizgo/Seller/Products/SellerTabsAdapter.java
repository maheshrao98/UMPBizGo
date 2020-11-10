package com.example.umpbizgo.Seller.Products;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SellerTabsAdapter extends FragmentStatePagerAdapter {
    int mnumTabs;
    public SellerTabsAdapter(FragmentManager fm, int nooftabs) {
        super(fm);
        this.mnumTabs = nooftabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                SellerMyProductsFragment authorizedproduct = new SellerMyProductsFragment();
                return authorizedproduct;
            case 1 :
                SellerUnauthorizedProductsFragment unauthorizedproduct = new SellerUnauthorizedProductsFragment();
                return unauthorizedproduct;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mnumTabs;
    }
}
