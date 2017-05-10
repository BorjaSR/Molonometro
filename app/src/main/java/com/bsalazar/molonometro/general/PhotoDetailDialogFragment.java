package com.bsalazar.molonometro.general;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bsalazar.molonometro.R;
import com.bsalazar.molonometro.general.MyRequestListener;
import com.bsalazar.molonometro.general.Variables;
import com.bumptech.glide.Glide;

/**
 * Created by bsalazar on 10/05/2017.
 */

public class PhotoDetailDialogFragment extends DialogFragment
{

//    @Override
//    public void onActivityCreated(Bundle arg0) {
//        super.onActivityCreated(arg0);
//        getDialog().getWindow()
//                .getAttributes().windowAnimations = R.style.DialogAnimation;
//    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_detail_dialog_fragment, container,
                false);

        ImageView group_image_dialog = (ImageView) rootView.findViewById(R.id.group_image_dialog);

        String imageBase64 = getArguments().getString("image");
        int noImage = getArguments().getInt("noImage");

        if (imageBase64 != null)
            try{
                byte[] imageByteArray = Base64.decode(imageBase64, Base64.DEFAULT);

                Glide.with(getActivity().getApplicationContext())
                        .load(imageByteArray)
                        .asBitmap()
                        .listener(new MyRequestListener(getActivity().getApplicationContext(), group_image_dialog))
                        .into(group_image_dialog);

            }catch (Exception e){
                group_image_dialog.setImageResource(noImage);
            }
        else
            group_image_dialog.setImageResource(noImage);


        rootView.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }
}