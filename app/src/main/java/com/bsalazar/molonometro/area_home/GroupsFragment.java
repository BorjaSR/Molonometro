package com.bsalazar.molonometro.area_home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.adapters.GroupsRecyclerAdapter;
import com.bsalazar.molonometro.general.Variables;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class GroupsFragment extends Fragment {

    private View rootView;
    private LinearLayout empty_list;
    private GroupsRecyclerAdapter adapterRecycler;
    private RecyclerView groups_recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.groups_fragment, container, false);

        update();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update() {

        empty_list = (LinearLayout) rootView.findViewById(R.id.empty_list);


        groups_recycler = (RecyclerView) rootView.findViewById(R.id.groups_recycler);
        groups_recycler.setHasFixedSize(false);
        groups_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterRecycler = new GroupsRecyclerAdapter(getActivity(), Variables.groups);
        groups_recycler.setAdapter(adapterRecycler);


        if (Variables.groups.size() > 0)
            empty_list.setVisibility(View.GONE);
        else
            empty_list.setVisibility(View.VISIBLE);
    }

    public void updateGroupList() {
        if (adapterRecycler != null)
            adapterRecycler.notifyDataSetChanged();

        if (Variables.groups.size() > 0) {
            adapterRecycler = new GroupsRecyclerAdapter(getActivity(), Variables.groups);
            groups_recycler.setAdapter(adapterRecycler);

            empty_list.setVisibility(View.GONE);
        } else
            empty_list.setVisibility(View.VISIBLE);
    }
}
