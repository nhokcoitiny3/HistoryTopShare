package com.example.nhokc.historytopshare.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nhokc.historytopshare.R;
import com.example.nhokc.historytopshare.databinding.ActivitySyntheticBinding;

public class MainActivity extends AppCompatActivity {


    private ActivitySyntheticBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_synthetic);
        initialize();
    }

    public void initialize() {

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);

        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_home);
        binding.tabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_history);


    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeFragment();
            } else {
                return new HistoryFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


    }
}
