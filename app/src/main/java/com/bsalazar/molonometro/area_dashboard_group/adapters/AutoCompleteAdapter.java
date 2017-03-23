package com.bsalazar.molonometro.area_dashboard_group.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.entities.Participant;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by bsalazar on 23/03/2017.
 */

public class AutoCompleteAdapter extends ArrayAdapter<Participant> {

    private Context mContext;
    private ArrayList<Participant> participants;

    private ArrayList<Participant> itemsAll;
    private ArrayList<Participant> suggestions;

    public AutoCompleteAdapter(Context context, int resource, ArrayList<Participant> participants) {
        super(context, resource);
        this.mContext = context;
        this.participants = participants;

        itemsAll = (ArrayList<Participant>) this.participants.clone();
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Participant participant = participants.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.autocomplete_participant_item, null);

            holder = new ViewHolder();
            holder.participant_image = (ImageView) convertView.findViewById(R.id.participant_image);
            holder.participant_name = (TextView) convertView.findViewById(R.id.participant_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.participant_name.setText(participant.getName());

        try{
            byte[] imageByteArray = Base64.decode(participant.getImageBase64(), Base64.DEFAULT);

            Glide.with(mContext)
                    .load(imageByteArray)
                    .asBitmap()
                    .into(holder.participant_image);

        }catch (Exception e){
            holder.participant_image.setImageResource(R.drawable.user_icon);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Nullable
    @Override
    public Participant getItem(int position) {
        return participants.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((Participant)(resultValue)).getName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Participant customer : itemsAll) {
                    if(customer.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Participant> filteredList = (ArrayList<Participant>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Participant c : filteredList) {
                    add(c);
                }

                participants = (ArrayList<Participant>) filteredList.clone();
                notifyDataSetChanged();
            }
        }
    };

    static class ViewHolder {
        private ImageView participant_image;
        private TextView participant_name;
    }
}
