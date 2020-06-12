package com.example.aimeliveapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetail;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private TextView tv_home_heart, tv_home_comments, tv_home_shares;
    private View view;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        CircleImageView circleImage = view.findViewById(R.id.goto_pref);
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
        tv_home_comments = view.findViewById(R.id.tv_home_comments);
        tv_home_shares = view.findViewById(R.id.tv_home_shares);
        tv_home_heart = view.findViewById(R.id.tv_home_heart);
        List<GetUserDetail> list = MainActivity.USER_DETAILS_LIST;
        if (list != null) {
            for (GetUserDetail data : list) {
                tv_home_comments.setText(data.getTotalComments());
                tv_home_shares.setText(data.getTotalShares());
                tv_home_heart.setText(data.getHearts());

            }
        } else {
            Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }


}
