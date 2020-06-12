package com.example.aimeliveapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimeliveapp.Adapter.ProfileAdapter;
import com.example.aimeliveapp.Model.ProfileModel;
import com.example.aimeliveapp.network_calls.getchekuser.GetUserDetail;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private List<ProfileModel> profileModels = new ArrayList<>();
    private TextView tv_followingProfile, tv_followerProfile, tv_likeProfile;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initviews();


        return view;
    }

    private void initviews() {
        tv_followingProfile = view.findViewById(R.id.tv_followingProfile);
        tv_followerProfile = view.findViewById(R.id.tv_followerProfile);
        tv_likeProfile = view.findViewById(R.id.tv_likeProfile);
        List<GetUserDetail> list = MainActivity.USER_DETAILS_LIST;
        if (list != null) {
            tv_followerProfile.setText(list.get(0).getFollower());
            tv_followingProfile.setText(list.get(0).getFollowing());
            tv_likeProfile.setText(list.get(0).getLikes());

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupProfileAdapter(view);
    }

    private void setupProfileAdapter(View view) {
        final RecyclerView profileRecycler = view.findViewById(R.id.profile_recycler);
        ProfileAdapter adapter = new ProfileAdapter(profileModels);
        profileRecycler.setAdapter(adapter);
        loadProfileItems();
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

//        if (adapter.getItemCount() == profileModels.size()/2){
//
//            CardView.LayoutParams marginLayout = new CardView.LayoutParams(profileRecycler.getLayoutParams());
//            marginLayout.setMargins(0,0,0,50);
//            profileRecycler.setLayoutParams(marginLayout);
//            profileRecycler.requestLayout();
//        }
    }

    private void loadProfileItems() {
        profileModels.clear();

        profileModels.add(new ProfileModel(
                R.drawable.trophy,
                getString(R.string.contributor),
                ""
        ));

        profileModels.add(new ProfileModel(
                R.drawable.income,
                getString(R.string.my_income),
                "OBB tickets"
        ));

        profileModels.add(new ProfileModel(
                R.drawable.wallet,
                getString(R.string.wallet),
                "OBB Coins"
        ));

        profileModels.add(new ProfileModel(
                R.drawable.wish,
                getString(R.string.my_wish),
                MainActivity.USER_DETAILS_LIST.get(0).getWishes()
        ));

        profileModels.add(new ProfileModel(
                R.drawable.voucher,
                getString(R.string.voucher),
                MainActivity.USER_DETAILS_LIST.get(0).getVoucher()
        ));

        profileModels.add(new ProfileModel(
                R.drawable.fans,
                getString(R.string.top_fans),
                MainActivity.USER_DETAILS_LIST.get(0).getTopFans()
        ));

        profileModels.add(new ProfileModel(
                R.drawable.broadcaster,
                getString(R.string.broadcaster_sign),
                ""
        ));

        profileModels.add(new ProfileModel(
                R.drawable.broadcaster2,
                getString(R.string.broadcaster),
                ""
        ));

        profileModels.add(new ProfileModel(
                R.drawable.chat,
                getString(R.string.chat),
                "Verified"
        ));

        profileModels.add(new ProfileModel(
                R.drawable.settings,
                getString(R.string.setting),
                ""
        ));
    }

}
