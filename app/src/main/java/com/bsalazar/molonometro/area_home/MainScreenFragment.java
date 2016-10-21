package com.bsalazar.molonometro.area_home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsalazar.molonometro.MainActivity;
import com.bsalazar.molonometro.R;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class MainScreenFragment extends Fragment {


    private View rootView;
    private ViewPager main_view_pager;
    private MainScreenAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_screen_fragment, container, false);

        update();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update() {

        TextView groups_button = (TextView) rootView.findViewById(R.id.groups_button);
        TextView contacts_button = (TextView) rootView.findViewById(R.id.contacts_button);
        final LinearLayout indicator_current_page = (LinearLayout) rootView.findViewById(R.id.indicator_current_page);

        main_view_pager = (ViewPager) rootView.findViewById(R.id.main_view_pager);
        adapter = new MainScreenAdapter(getChildFragmentManager());
        main_view_pager.setAdapter(adapter);

        groups_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_view_pager.getCurrentItem() != 0)
                    main_view_pager.setCurrentItem(0);
            }
        });

        contacts_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_view_pager.getCurrentItem() != 1)
                    main_view_pager.setCurrentItem(1);
            }
        });

        main_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((MainActivity) getActivity()).mainViewPagerScrolled(position, positionOffset, positionOffsetPixels);

                indicator_current_page.setX(positionOffsetPixels / 2);
                if (position == 1 && positionOffsetPixels == 0)
                    indicator_current_page.setX(((MainActivity) getActivity()).size.x / 2);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class MainScreenAdapter extends FragmentStatePagerAdapter {

        private GroupsFragment groupsFragment = new GroupsFragment();
        private ContactsFragment constantsFragment = new ContactsFragment();


        public MainScreenAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = groupsFragment;
                    break;
                case 1:
                    fragment = constantsFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
