package com.bsalazar.molonometro.area_home;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.adapters.GroupsAdapter;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Tools;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.controllers.GroupController;
import com.bsalazar.molonometro.rest.json.UserIdJson;
import com.bsalazar.molonometro.rest.services.ServiceCallbackInterface;

import java.util.ArrayList;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class GroupsFragment extends Fragment {

    private View rootView;
    private GroupsAdapter adapter;
    private LinearLayout empty_list;
    private ListView groups_list;

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

        groups_list = (ListView) rootView.findViewById(R.id.groups_list);
        adapter = new GroupsAdapter(getActivity(), R.layout.group_item, Variables.groups);
        groups_list.setAdapter(adapter);

        if (Variables.groups.size() > 0)
            empty_list.setVisibility(View.GONE);
        else
            empty_list.setVisibility(View.VISIBLE);
    }

    public void updateGroupList() {
        if (adapter != null)
            adapter.notifyDataSetChanged();

        if (Variables.groups.size() > 0) {
            adapter = new GroupsAdapter(getActivity(), R.layout.group_item, Variables.groups);
            groups_list.setAdapter(adapter);

            empty_list.setVisibility(View.GONE);
        } else
            empty_list.setVisibility(View.VISIBLE);
    }
}
