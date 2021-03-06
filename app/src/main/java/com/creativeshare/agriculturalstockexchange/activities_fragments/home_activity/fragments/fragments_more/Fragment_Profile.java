package com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.fragments.fragments_more;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.agriculturalstockexchange.R;
import com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.agriculturalstockexchange.adapters.Other_Adversiment_Adapter;
import com.creativeshare.agriculturalstockexchange.models.Adversiment_Model;
import com.creativeshare.agriculturalstockexchange.models.CityModel;
import com.creativeshare.agriculturalstockexchange.models.UserModel;
import com.creativeshare.agriculturalstockexchange.preferences.Preferences;
import com.creativeshare.agriculturalstockexchange.remote.Api;
import com.creativeshare.agriculturalstockexchange.share.Common;
import com.creativeshare.agriculturalstockexchange.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private CircleImageView imageprofile;
    private TextView tv_name, tv_loaction, tv_address, tv_phone, tv_email;
    private ImageView arrow1, arrow2, arrow3, arrow4, arrow5, im_edit;
    private LinearLayout ll_shipservice,lll_packging,ll_storage;
    private SwitchCompat switchCompat,switchCompatpackge,switchCompatstorage;
    private SimpleRatingBar simpleRatingBar;
    private Button bt_upgrade;
    private Preferences preferences;
    private UserModel userModel;
    private ImageView back;
    private List<CityModel> cityModels;
    private RecyclerView recyclerotherads;
    private List<UserModel.Advertsing> advertsingList;
    private Other_Adversiment_Adapter other_adversiment_adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        getCities();
        return view;
    }

    private void initView(View view) {
        advertsingList=new ArrayList<>();

        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();

        if (Adversiment_Model.getId() == null) {
            userModel = preferences.getUserData(homeActivity);
        }

        cityModels = new ArrayList<>();
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        imageprofile = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_loaction = view.findViewById(R.id.tv_location);
        tv_address = view.findViewById(R.id.tv_address);
        //tv_commericial = view.findViewById(R.id.tv_commercial);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_email = view.findViewById(R.id.tv_email);
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        im_edit = view.findViewById(R.id.im_edit);
        back = view.findViewById(R.id.arrow_back);
        simpleRatingBar = view.findViewById(R.id.rating);
        bt_upgrade = view.findViewById(R.id.bt_upgrade);
        ll_shipservice=view.findViewById(R.id.ll);
        lll_packging=view.findViewById(R.id.lll);
        ll_storage=view.findViewById(R.id.llll);
        switchCompat=view.findViewById(R.id.switch1);
        switchCompatpackge=view.findViewById(R.id.switch2);
        switchCompatstorage=view.findViewById(R.id.switch3);
        recyclerotherads =view.findViewById(R.id.rec_advers);
        recyclerotherads.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerotherads.setItemViewCacheSize(25);
        recyclerotherads.setDrawingCacheEnabled(true);
other_adversiment_adapter=new Other_Adversiment_Adapter(advertsingList,homeActivity);
recyclerotherads.setLayoutManager(new GridLayoutManager(homeActivity,1));
recyclerotherads.setAdapter(other_adversiment_adapter);
        if (cuurent_language.equals("en")) {
            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
            back.setRotation(180);

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });
        bt_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentupgrade();
            }
        });
        if (Adversiment_Model.getId() != null) {
            im_edit.setImageDrawable(getResources().getDrawable(R.drawable.follow));
            simpleRatingBar.setVisibility(View.VISIBLE);
            bt_upgrade.setVisibility(View.GONE);
            ll_shipservice.setVisibility(View.GONE);
            lll_packging.setVisibility(View.GONE);
            ll_storage.setVisibility(View.GONE);

        } else {
            if (userModel != null) {
                if (userModel.getUser_type().equals("2")) {

                    bt_upgrade.setVisibility(View.GONE);
                }

            }
        }

        //  updateprofile();
        im_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Adversiment_Model.getId() == null) {
                    homeActivity.DisplayFragmentEditProfile();
                } else {
                    followuser();
                }
            }
        });

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    changenotifystatus();
                }
            }

        });
        switchCompatpackge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel!=null){
                    changenotifystatus2();
                }
            }
        });
        switchCompatstorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel!=null){
                    changenotifystatus3();
                }
            }
        });
        simpleRatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null && Adversiment_Model.getId() != null) {
                    if (userModel.isRating_status() == false) {
                        updaterating();
                    }
                }
            }
        });
    }


        private void changenotifystatus() {
            final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
           // Log.e("user", userModel.getToken());
            String insurancetype;
            if(userModel.getShipping_serv().equals("1")){
                insurancetype="0";
            }
            else {
                insurancetype="1";
            }
            Api.getService().shipping_serv(userModel.getUser_id(),insurancetype).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                     //   Log.e("kk",response.body().getInsurance_services());
                        preferences.create_update_userdata(homeActivity,response.body());
                        userModel=preferences.getUserData(homeActivity);

                        updateprofile();
                    } else {
try {
    Log.e("error_code", response.code() + "_" + response.errorBody() + response.message() + response.raw() + response.headers());


    Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
}catch (Exception e){

}


                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    }
                    catch (Exception e){

                    }
                }
            });
        }
    private void changenotifystatus2() {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // Log.e("user", userModel.getToken());
        String insurancetype;
        if(userModel.getPackaging_serv().equals("1")){
            insurancetype="0";
        }
        else {
            insurancetype="1";
        }
        Api.getService().packaging_serv(userModel.getUser_id(),insurancetype).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //   Log.e("kk",response.body().getInsurance_services());
                    preferences.create_update_userdata(homeActivity,response.body());
                    userModel=preferences.getUserData(homeActivity);

                    updateprofile();
                } else {
                    try {
                        Log.e("error_code", response.code() + "_" + response.errorBody() + response.message() + response.raw() + response.headers());


                        Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){

                    }


                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
                catch (Exception e){

                }
            }
        });
    }
    private void changenotifystatus3() {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // Log.e("user", userModel.getToken());
        String insurancetype;
        if(userModel.getStorage_serv().equals("1")){
            insurancetype="0";
        }
        else {
            insurancetype="1";
        }
        Api.getService().storage_serv(userModel.getUser_id(),insurancetype).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //   Log.e("kk",response.body().getInsurance_services());
                    preferences.create_update_userdata(homeActivity,response.body());
                    userModel=preferences.getUserData(homeActivity);

                    updateprofile();
                } else {
                    try {
                        Log.e("error_code", response.code() + "_" + response.errorBody() + response.message() + response.raw() + response.headers());


                        Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){

                    }


                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
                catch (Exception e){

                }
            }
        });
    }


    private void updaterating() {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().makerating(userModel.getUser_id(), preferences.getUserData(homeActivity).getUser_id(), simpleRatingBar.getRating() + "").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //Log.e("uu",((float)simpleRatingBar.getRating())+""+userModel.getUser_id()+" "+preferences.getUserData(homeActivity).getUser_id());
                    userModel.setRating_value(simpleRatingBar.getRating());
                } else {
                    try {
                        Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();
                        Log.e("Error_code", response.code() + "" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();

                try {
                    Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {

                }
            }
        });
    }

    private void followuser() {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().followuser(preferences.getUserData(homeActivity).getUser_id(), userModel.getUser_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (userModel.isUser_follow() == true) {
                        im_edit.setImageDrawable(getResources().getDrawable(R.drawable.follow));
                        userModel.setUser_follow(false);

                    } else {
                        im_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
                        userModel.setUser_follow(true);

                    }
                } else {
                    try {
                        Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();
                        Log.e("Error_code", response.code() + "" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();

                try {
                    Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {


                }
            }
        });
    }

    private void getdata(final String id) {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        ;
        Api.getService().Showotherprofile(Preferences.getInstance().getUserData(homeActivity).getUser_id(), id).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    userModel = response.body();
                    if(userModel.isRating_status()){
                        simpleRatingBar.setIndicator(true);
                    }
                    updateprofile();

                } else {
                    try {
                        Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();
                        Log.e("Error_code", response.code() + "" + response.errorBody() + response.headers() + response.message() + response.raw() + " " + id);
                    }
                    catch (Exception e){

                    }

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {

                }
            }
        });
    }

    private void getCities() {

        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService()
                .getCities(cuurent_language)
                .enqueue(new Callback<List<CityModel>>() {
                    @Override
                    public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                cityModels.clear();

                                cityModels.addAll(response.body());
                                if (Adversiment_Model.getId() == null) {
                                    updateprofile();
                                } else {
                                    getdata(Adversiment_Model.getId());
                                }


                            }
                        } else {
                            try {
                                Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();
                                Log.e("Error_code", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CityModel>> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void updateprofile() {
     //   userModel=preferences.getUserData(homeActivity);
      //  Log.e("llll",userModel.getInsurance_services());
        if (userModel != null) {
            if (userModel.getUser_image() != null && !userModel.getUser_image().equals("0")) {
                Picasso.with(homeActivity).load(Tags.IMAGE_URL + userModel.getUser_image()).placeholder(R.drawable.logo).fit().into(imageprofile);
            }
            if (userModel.getUser_name() != null) {
                tv_name.setText(userModel.getUser_name());
            }
            if (userModel.getUser_city() != null) {
                //  Log.e("msg",cityModels.size()+"");
                //  tv_loaction.setText(userModel.getUser_city());
                if (cuurent_language.equals("en")) {
                    tv_loaction.setText(userModel.getEn_city_title());
                } else {
                    tv_loaction.setText(userModel.getAr_city_title());
                }
            }
            if (userModel.getUser_address() != null) {
                tv_address.setText(userModel.getUser_address());
            }
            if (userModel.getCommercial_register() != null) {
                //tv_commericial.setText(userModel.getCommercial_register());
            }
            if (userModel.getUser_phone() != null) {
                tv_phone.setText(userModel.getUser_phone());
            }
            if (userModel.getUser_email() != null) {
                tv_email.setText(userModel.getUser_email());
            }
            if (Adversiment_Model.getId() != null) {
                if (userModel.isUser_follow() == true) {
                    im_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
                } else {
                    im_edit.setImageDrawable(getResources().getDrawable(R.drawable.follow));

                }

             //   Log.e("lll", userModel.getRating_value() + "");
                simpleRatingBar.setRating(userModel.getRating_value());

            }

            if(userModel.getShipping_serv()!=null&&userModel.getShipping_serv().equals("0")){
                switchCompat.setChecked(false);
            }
            else {
                switchCompat.setChecked(true);
            }
            if(userModel.getPackaging_serv()!=null&&userModel.getPackaging_serv().equals("0")){
                switchCompatpackge.setChecked(false);
            }
            else {
                switchCompatpackge.setChecked(true);
            }
            if(userModel.getStorage_serv()!=null&&userModel.getStorage_serv().equals("0")){
                switchCompatstorage.setChecked(false);
            }
            else {
                switchCompatstorage.setChecked(true);
            }
            if(userModel.getAdvertsing()!=null){
                advertsingList.clear();
                advertsingList.addAll(userModel.getAdvertsing());
                other_adversiment_adapter.notifyDataSetChanged();
            }

        }
    }

    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

}
