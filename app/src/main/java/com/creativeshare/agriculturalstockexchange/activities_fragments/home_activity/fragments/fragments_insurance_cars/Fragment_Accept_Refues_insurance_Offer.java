package com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.fragments.fragments_insurance_cars;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.agriculturalstockexchange.R;
import com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.agriculturalstockexchange.models.Insuarce_Model;
import com.creativeshare.agriculturalstockexchange.models.Notification_Model;
import com.creativeshare.agriculturalstockexchange.models.UserModel;
import com.creativeshare.agriculturalstockexchange.preferences.Preferences;
import com.creativeshare.agriculturalstockexchange.remote.Api;
import com.creativeshare.agriculturalstockexchange.share.Common;
import com.creativeshare.agriculturalstockexchange.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Accept_Refues_insurance_Offer extends Fragment {

    private HomeActivity activity;
    private String current_language;
    private final static String Tag1 = "order";
    private Notification_Model.Data data;

    private Preferences preferences;
    private UserModel userModel;

    private ImageView back_arrow;
    private TextView tv_offer, tv_name, tv_time, tv_quantity, tv_desc, tv_from, tv_to;
    private Button bt_accept, bt_refues;

    public static Fragment_Accept_Refues_insurance_Offer newInstance(Notification_Model.Data data) {
        Fragment_Accept_Refues_insurance_Offer fragment_accept_refues_insurance_offer = new Fragment_Accept_Refues_insurance_Offer();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Tag1, data);
        fragment_accept_refues_insurance_offer.setArguments(bundle);
        return fragment_accept_refues_insurance_offer;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_refuse_insurance_offer, container, false);
        initView(view);
        getioneinsurance();
        return view;
    }

    private void getioneinsurance() {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().showoneinsurancerequsts(userModel.getUser_id(), data.getRequest_id()).enqueue(new Callback<Insuarce_Model>() {
            @Override
            public void onResponse(Call<Insuarce_Model> call, Response<Insuarce_Model> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    updatedata(response.body());
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                    Log.e("Error_Code", response.code() + "  " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Insuarce_Model> call, Throwable t) {
                dialog.dismiss();
                try {
                    Log.e("Error", t.getMessage());
                    Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();
                }
                catch (Exception e){

                }
            }
        });
    }

    private void updatedata(Insuarce_Model body) {
        tv_name.setText(body.getFrom_user_name());
        tv_time.setText(body.getApppoiment());
        tv_quantity.setText(body.getAmount());

        tv_desc.setText(body.getAmount_desc());
        tv_from.setText(body.getFrom_address());
        tv_to.setText(body.getTo_address());

    }

    private void initView(View view) {
        data = (Notification_Model.Data) getArguments().getSerializable(Tag1);
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        back_arrow = view.findViewById(R.id.arrow);
        tv_name = view.findViewById(R.id.tv_name);
        tv_time = view.findViewById(R.id.tv_time);
        tv_quantity = view.findViewById(R.id.tv_quantity);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_from = view.findViewById(R.id.tv_from);
        tv_to = view.findViewById(R.id.tv_to);
        tv_offer = view.findViewById(R.id.tv_offer);
        bt_accept = view.findViewById(R.id.btn_send);
        bt_refues = view.findViewById(R.id.btn_refuse);
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tv_offer.setText(data.getOffer_value());
        if (current_language.equals("ar")) {
            back_arrow.setRotation(180.0f);
        }
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
            }
        });
        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptrefuse("accept");
            }
        });
        bt_refues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptrefuse("refuse");
            }
        });

    }

    private void acceptrefuse(String accept_refues) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().acceptrefuesinsuranceoffer(userModel.getUser_id(), data.getFrom_user_id_fk(), data.getId_notification(), data.getRequest_id(), accept_refues).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                    activity.refresh();
                    activity.Back();
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                    Log.e("Error_Code", response.code() + "  " + response.errorBody().toString()+response.message()+response.raw());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                try {
                    Log.e("Error", t.getMessage());
                    Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();
                }
                catch (Exception e){

                }
            }
        });
    }

}





