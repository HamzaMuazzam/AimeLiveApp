package com.example.aimeliveapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetail;
import com.example.aimeliveapp.service.ExampleService;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private TextView tv_home_heart, tv_home_comments, tv_home_shares, tv_onlinestatus;
    private View view;
    Context context;
    CircleImageView circleImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        circleImage = view.findViewById(R.id.goto_pref);
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MatchMakingActivity.class));
            }
        });

        initViews();
        return view;
    }

    private void initViews() {
        context = getContext();
        tv_onlinestatus = view.findViewById(R.id.tv_onlinestatus);
        tv_home_comments = view.findViewById(R.id.tv_home_comments);
        tv_home_shares = view.findViewById(R.id.tv_home_shares);
        tv_home_heart = view.findViewById(R.id.tv_home_heart);
        List<GetUserDetail> list = MainActivity.USER_DETAILS_LIST;
        if (list != null) {
            for (GetUserDetail data : list) {
                tv_home_comments.setText(data.getTotalComments());
                try {

                    Glide.with(getContext()).load(data.getProfileImage()).into(circleImage);
                } catch (Exception e) {
                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                tv_home_shares.setText(data.getTotalShares());
                tv_home_heart.setText(data.getHearts());

            }
        } else {
            Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        final String[] status = {sharedPreferences.getString("status", "")};
        if (status[0].equalsIgnoreCase("online")) {
            tv_onlinestatus.setText("online");
            Drawable img = getContext().getResources().getDrawable(R.drawable.ic_on);
            tv_onlinestatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            Intent intent = new Intent(getContext(), ExampleService.class);
            intent.putExtra("uuid", sharedPreferences.getString(LoginActivity.LOGIN_ID, ""));
            context.startService(intent);
        } else if (status[0].equalsIgnoreCase("offline")) {
            tv_onlinestatus.setText("offline");
            Drawable img = getContext().getResources().getDrawable(R.drawable.ic_off);
            tv_onlinestatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        } else {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("status", "online");
            edit.apply();
            Drawable img = getContext().getResources().getDrawable(R.drawable.ic_on);
            tv_onlinestatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            Intent intent = new Intent(getContext(), ExampleService.class);
            intent.putExtra("uuid", sharedPreferences.getString(LoginActivity.LOGIN_ID, ""));
            context.startService(intent);
            tv_onlinestatus.setText("online");
        }

        tv_onlinestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status[0].equalsIgnoreCase("offline")) {
                    tv_onlinestatus.setText("online");
                    Drawable img = getContext().getResources().getDrawable(R.drawable.ic_on);
                    tv_onlinestatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("status", "online");
                    edit.apply();
                    status[0] = "online";
                    Intent intent = new Intent(getContext(), ExampleService.class);
                    intent.putExtra("uuid", sharedPreferences.getString(LoginActivity.LOGIN_ID, ""));
                    context.startService(intent);
                } else if (status[0].equalsIgnoreCase("online")) {
                    tv_onlinestatus.setText("offline");
                    Drawable img = getContext().getResources().getDrawable(R.drawable.ic_off);
                    tv_onlinestatus.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("status", "offline");
                    edit.apply();
                    status[0] = "offline";
                    Intent intent = new Intent(getContext(), ExampleService.class);
                    intent.putExtra("uuid", sharedPreferences.getString(LoginActivity.LOGIN_ID, ""));
                    context.stopService(intent);
                }
            }
        });

    }


}
