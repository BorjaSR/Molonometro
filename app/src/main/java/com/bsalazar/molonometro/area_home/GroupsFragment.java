package com.bsalazar.molonometro.area_home;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.general.Tools;

import java.util.ArrayList;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class GroupsFragment extends Fragment {

    private View rootView;

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

        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            groups.add(new Group(i, "Mi grupo " + (i + 1), Tools.getRoundedCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.group_icon))));

        ListView groups_list = (ListView) rootView.findViewById(R.id.groups_list);
        GroupsAdapter adapter = new GroupsAdapter(getActivity(), R.layout.group_item, groups);
        groups_list.setAdapter(adapter);
    }
}
