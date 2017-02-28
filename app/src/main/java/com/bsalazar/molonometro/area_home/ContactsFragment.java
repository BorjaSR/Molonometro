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
import com.bsalazar.molonometro.area_home.adapters.ContactsRecyclerAdapter;
import com.bsalazar.molonometro.general.Variables;

/**
 * Created by bsalazar on 18/10/2016.
 */
public class ContactsFragment extends Fragment {

    private View rootView;
    private ContactsRecyclerAdapter adapterRecycler;

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

        RecyclerView contacts_recycler = (RecyclerView) rootView.findViewById(R.id.contacts_recycler);
        contacts_recycler.setHasFixedSize(true);
        contacts_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterRecycler = new ContactsRecyclerAdapter(getActivity(), Variables.contacts);
        contacts_recycler.setAdapter(adapterRecycler);

        if (Variables.contacts.size() > 0)
            empty_list.setVisibility(View.GONE);
        else
            empty_list.setVisibility(View.VISIBLE);

    }

    public void updateContactList() {
        if (adapterRecycler != null)
            adapterRecycler.notifyDataSetChanged();


        if (Variables.contacts.size() > 0)
            empty_list.setVisibility(View.GONE);
        else
            empty_list.setVisibility(View.VISIBLE);
    }


}
