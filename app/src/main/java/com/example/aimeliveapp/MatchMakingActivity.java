package com.example.aimeliveapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aimeliveapp.matchmaker.GetMatch;
import com.example.aimeliveapp.matchmaker.GetMatchResponse;
import com.example.aimeliveapp.matchmaker.MatchResultActivity;
import com.example.aimeliveapp.network_calls.RestApis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchMakingActivity extends AppCompatActivity {
    String start_age = null, end_age = null, location = null, gender = null;
    RestApis restApis;
    public static List<GetMatch> LIST = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA},
                    1);
        }
        restApis = RestApis.retrofit.create(RestApis.class);
        initPreferenceDialog();
    }

    private void initPreferenceDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_preference);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        final Button start = dialog.findViewById(R.id.start_matching_preference);
        final RelativeLayout below = dialog.findViewById(R.id.layout_below);
        final TextView belowPkg = dialog.findViewById(R.id.pref_blw_coins);
        final TextView belowAge = dialog.findViewById(R.id.age_below);

        final RelativeLayout average = dialog.findViewById(R.id.layout_average);
        final TextView averageAge = dialog.findViewById(R.id.age_average);
        final TextView averagePkg = dialog.findViewById(R.id.pref_avg_coin);

        final RelativeLayout above = dialog.findViewById(R.id.layout_above);
        final TextView aboveAge = dialog.findViewById(R.id.age_all);
        final TextView abovePkg = dialog.findViewById(R.id.pref_abv_coin);

        final RelativeLayout country = dialog.findViewById(R.id.country);
        final TextView countryAge = dialog.findViewById(R.id.dist_country);
        final TextView countryPkg = dialog.findViewById(R.id.pref_ctry_coin);

        final RelativeLayout city = dialog.findViewById(R.id.city);
        final TextView cityAge = dialog.findViewById(R.id.dist_city);
        final TextView cityPkg = dialog.findViewById(R.id.pref_city_coin);

        final RelativeLayout global = dialog.findViewById(R.id.global);
        final TextView globalAge = dialog.findViewById(R.id.dist_global);
        final TextView globalPkg = dialog.findViewById(R.id.pref_glb_coin);

        below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_age = "15";
                end_age = "25";
                below.setBackground(getResources().getDrawable(R.drawable.button_bg_green));
                belowAge.setTextColor(Color.parseColor("#ffffff"));
                belowPkg.setTextColor(Color.parseColor("#000000"));
                belowPkg.setBackground(getDrawable(R.drawable.button_bg_white));
                belowPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coins_white, 0, 0, 0);


                average.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                averageAge.setTextColor(Color.parseColor("#000000"));
                averagePkg.setTextColor(Color.parseColor("#ffffff"));
                averagePkg.setBackground(getDrawable(R.drawable.button_bg_green));
                averagePkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);

                above.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                aboveAge.setTextColor(Color.parseColor("#000000"));
                abovePkg.setTextColor(Color.parseColor("#ffffff"));
                abovePkg.setBackground(getDrawable(R.drawable.button_bg_green));
                abovePkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);
            }
        });

        average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_age = "20";
                end_age = "35";
                average.setBackground(getResources().getDrawable(R.drawable.button_bg_green));
                averageAge.setTextColor(Color.parseColor("#ffffff"));
                averagePkg.setTextColor(Color.parseColor("#000000"));
                averagePkg.setBackground(getDrawable(R.drawable.button_bg_white));
                averagePkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coins_white, 0, 0, 0);

                below.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                belowAge.setTextColor(Color.parseColor("#000000"));
                belowPkg.setTextColor(Color.parseColor("#ffffff"));
                belowPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                belowPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);

                above.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                aboveAge.setTextColor(Color.parseColor("#000000"));
                abovePkg.setTextColor(Color.parseColor("#ffffff"));
                abovePkg.setBackground(getDrawable(R.drawable.button_bg_green));
                abovePkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);


            }
        });

        above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_age = "25";
                end_age = "45";
                above.setBackground(getResources().getDrawable(R.drawable.button_bg_green));
                aboveAge.setTextColor(Color.parseColor("#ffffff"));
                abovePkg.setTextColor(Color.parseColor("#000000"));
                abovePkg.setBackground(getDrawable(R.drawable.button_bg_white));
                abovePkg.setBackground(getDrawable(R.drawable.button_bg_green));
                abovePkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coins_white, 0, 0, 0);

                average.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                averageAge.setTextColor(Color.parseColor("#000000"));
                averagePkg.setTextColor(Color.parseColor("#ffffff"));
                averagePkg.setBackground(getDrawable(R.drawable.button_bg_green));
                averagePkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);

                below.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                belowAge.setTextColor(Color.parseColor("#000000"));
                belowPkg.setTextColor(Color.parseColor("#ffffff"));
                belowPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                belowPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);


            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = "global";

                country.setBackground(getResources().getDrawable(R.drawable.button_bg_green));
                countryAge.setTextColor(Color.parseColor("#ffffff"));
                countryPkg.setTextColor(Color.parseColor("#000000"));
                countryPkg.setBackground(getDrawable(R.drawable.button_bg_white));
                countryPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coins_white, 0, 0, 0);


                city.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                cityAge.setTextColor(Color.parseColor("#000000"));
                cityPkg.setTextColor(Color.parseColor("#ffffff"));
                cityPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                cityPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);

                global.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                globalAge.setTextColor(Color.parseColor("#000000"));
                globalPkg.setTextColor(Color.parseColor("#ffffff"));
                globalPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                globalPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.coin), getDrawable(R.drawable.coin)
                        , getDrawable(R.drawable.coin), getDrawable(R.drawable.coin));
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = "global";

                city.setBackground(getResources().getDrawable(R.drawable.button_bg_green));
                cityAge.setTextColor(Color.parseColor("#ffffff"));
                cityPkg.setTextColor(Color.parseColor("#000000"));
                cityPkg.setBackground(getDrawable(R.drawable.button_bg_white));
                cityPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coins_white, 0, 0, 0);


                country.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                countryAge.setTextColor(Color.parseColor("#000000"));
                countryPkg.setTextColor(Color.parseColor("#ffffff"));
                countryPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                countryPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);

                global.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                globalAge.setTextColor(Color.parseColor("#000000"));
                globalPkg.setTextColor(Color.parseColor("#ffffff"));
                globalPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                globalPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.coin), getDrawable(R.drawable.coin)
                        , getDrawable(R.drawable.coin), getDrawable(R.drawable.coin));
            }
        });

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = "global";
                global.setBackground(getResources().getDrawable(R.drawable.button_bg_green));
                globalAge.setTextColor(Color.parseColor("#ffffff"));
                globalPkg.setTextColor(Color.parseColor("#000000"));
                globalPkg.setBackground(getDrawable(R.drawable.button_bg_white));
                globalPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coins_white, 0, 0, 0);

                city.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                cityAge.setTextColor(Color.parseColor("#000000"));
                cityPkg.setTextColor(Color.parseColor("#ffffff"));
                cityPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                cityPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);

                country.setBackground(getResources().getDrawable(R.drawable.button_bg_white));
                countryAge.setTextColor(Color.parseColor("#000000"));
                countryPkg.setTextColor(Color.parseColor("#ffffff"));
                countryPkg.setBackground(getDrawable(R.drawable.button_bg_green));
                countryPkg.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.coin, 0, 0, 0);

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_age == null && end_age == null) {
                    Toast.makeText(MatchMakingActivity.this, "Please select age range", Toast.LENGTH_SHORT).show();
                } else if (location == null) {
                    Toast.makeText(MatchMakingActivity.this, "Please Select target location ", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    gender = "both";
                    initSearchDialog(start_age, end_age, location, gender);

                }
            }
        });

        dialog.create();
        dialog.show();
    }

    private void initSearchDialog(String start_age, String end_age, String location, String gender) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_search);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final Button stop = dialog.findViewById(R.id.stop_search_preference);
//        final TextView pornography = dialog.findViewById(R.id.report_pornography);
//        final TextView fakeGender = dialog.findViewById(R.id.report_fake_gender);
//        final TextView spam = dialog.findViewById(R.id.report_spam);
//        final TextView langViolence = dialog.findViewById(R.id.report_lang_violence);
//        final TextView other = dialog.findViewById(R.id.report_other);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MatchMakingActivity.super.onBackPressed();
            }
        });

        dialog.create();
        dialog.show();

        restApis.getMatchMakerResult(start_age, end_age, gender, location).enqueue(new Callback<GetMatchResponse>() {
            @Override
            public void onResponse(Call<GetMatchResponse> call, Response<GetMatchResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus()) {
                            LIST = response.body().getGetMatch();
                            startActivity(new Intent(MatchMakingActivity.this, MatchResultActivity.class));

                        } else {

                            Toast.makeText(MatchMakingActivity.this, "Sorry No Match Found!!!", Toast.LENGTH_SHORT).show();
                            MatchMakingActivity.super.onBackPressed();
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(MatchMakingActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetMatchResponse> call, Throwable t) {
                Toast.makeText(MatchMakingActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
