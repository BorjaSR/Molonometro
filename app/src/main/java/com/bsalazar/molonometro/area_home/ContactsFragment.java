package com.bsalazar.molonometro.area_home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.area_home.adapters.ContactsAdapter;
import com.bsalazar.molonometro.general.Variables;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class ContactsFragment extends Fragment {

    private View rootView;
    private ContactsAdapter adapter;

    LinearLayout empty_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contacts_fragment, container, false);

        update();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void update() {

        empty_list = (LinearLayout) rootView.findViewById(R.id.empty_list);

        ListView groups_list = (ListView) rootView.findViewById(R.id.contacts_list);
        adapter = new ContactsAdapter(getActivity(), R.layout.contact_item, Variables.contacts);
        groups_list.setAdapter(adapter);

        if (Variables.contacts.size() > 0)
            empty_list.setVisibility(View.GONE);
        else
            empty_list.setVisibility(View.VISIBLE);

    }

    public void updateContactList() {
        if (adapter != null)
            adapter.notifyDataSetChanged();


        if (Variables.contacts.size() > 0)
            empty_list.setVisibility(View.GONE);
        else
            empty_list.setVisibility(View.VISIBLE);
    }


}
