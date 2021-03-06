package com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creativeshare.agriculturalstockexchange.R;
import com.creativeshare.agriculturalstockexchange.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.agriculturalstockexchange.models.Adversiment_Model;
import com.creativeshare.agriculturalstockexchange.models.UserModel;
import com.creativeshare.agriculturalstockexchange.preferences.Preferences;
import com.creativeshare.agriculturalstockexchange.share.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment {
    private HomeActivity homeActivity;
    //private ImageView im_search;
    //, insurance_car;
    private AHBottomNavigation ah_bottom_nav;
    private String cuurent_language;
    private FloatingActionButton fab_add_ads;
    private TextView tv_title;
    private Preferences preferences;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
      //  im_search = view.findViewById(R.id.im_search);
        //insurance_car = view.findViewById(R.id.img_insurance_car);
        ah_bottom_nav = view.findViewById(R.id.ah_bottom_nav);
        fab_add_ads = view.findViewById(R.id.fab_add_ads);
        tv_title = view.findViewById(R.id.tv_title);
       /* im_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentCarSearch();
            }
        });
        insurance_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                homeActivity.DisplayFragmentInsuranceCar();


            }
        });*/
        fab_add_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    if (userModel.getUser_type().equals("2")) {
                        homeActivity.getoAds("-1");
                    } else {
                        Common.CreateSignAlertDialog2(homeActivity, getResources().getString(R.string.upgrade));
                    }
                } else {
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                }
            }
        });

        setUpBottomNavigation();
        updateBottomNavigationPosition(0);
        ah_bottom_nav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        homeActivity.DisplayFragmentMain();
                        break;
                    case 1:
                        if (userModel != null) {
                            homeActivity.DisplayFragmentnotifications();
                        } else {
                            Common.CreateUserNotSignInAlertDialog(homeActivity);
                        }

                        break;
                    case 2:
                        if(userModel!=null) {
                            Adversiment_Model.setId(null);

                            homeActivity.DisplayFragmentProile();
                        }
                        break;
                    case 3:
                        homeActivity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.house);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.notifications), R.drawable.ic_mail);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.my_profile), R.drawable.ic_user);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.more);

        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(homeActivity, R.color.white));
        ah_bottom_nav.setTitleTextSizeInSp(14, 12);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(homeActivity, R.color.colorAccent));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(homeActivity, R.color.gray4));

        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);


    }

    public void updateBottomNavigationPosition(int pos) {
        ah_bottom_nav.setCurrentItem(pos, false);
        if(pos==0){
            tv_title.setText(getResources().getString(R.string.home));
        }
        else if(pos==1){
            tv_title.setText(getResources().getString(R.string.notifications));
        }
        else if (pos==2){
            tv_title.setText(getResources().getString(R.string.search));
        }
        else if (pos==3){
            tv_title.setText(getResources().getString(R.string.more));
        }
    }

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }


}
