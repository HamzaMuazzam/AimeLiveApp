package com.example.aimeliveapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.aimeliveapp.Adapter.SliderAdapter;
import com.example.aimeliveapp.addfragment.AddFragment;
import com.example.aimeliveapp.network_calls.RestApis;
import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetail;
import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetailsResponse;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private SpaceNavigationView spaceNav;
    private RestApis restApis;
    public  static  List<GetUserDetail> USER_DETAILS_LIST=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restApis = RestApis.retrofit.create(RestApis.class);
        initPager();
        initViews(savedInstanceState);
    }

    private void initViews(Bundle savedInstanceState) {

        spaceNav = findViewById(R.id.space_nav);
        spaceNav.initWithSaveInstanceState(savedInstanceState);
        spaceNav.addSpaceItem(new SpaceItem("", R.drawable.home));
        spaceNav.addSpaceItem(new SpaceItem("", R.drawable.ic_person));


        spaceNav.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                spaceNav.setCentreButtonSelectable(true);
                pager.setCurrentItem(1);

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        pager.setCurrentItem(0);

                        break;
                    case 1:
                        pager.setCurrentItem(2);
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNav.onSaveInstanceState(outState);
    }

    private void initPager() {
        pager = findViewById(R.id.viewpager);
        final SliderAdapter adapter = new SliderAdapter(getSupportFragmentManager());
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String id = sharedPreferences.getString(LoginActivity.LOGIN_ID, "");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading data....");
        progressDialog.show();
        restApis.getCheckUser(id, "none").enqueue(new Callback<GetUserDetailsResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponse> call, Response<GetUserDetailsResponse> response) {
                progressDialog.dismiss();
                try {
                    USER_DETAILS_LIST = response.body().getGetUserDetails();

                    adapter.addFragment(new HomeFragment());
                    adapter.addFragment(new AddFragment());
                    adapter.addFragment(new ProfileFragment());
                    pager.setAdapter(adapter);
                    pager.setCurrentItem(1);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailsResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
