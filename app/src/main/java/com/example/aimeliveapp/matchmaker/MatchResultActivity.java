package com.example.aimeliveapp.matchmaker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimeliveapp.MatchMakingActivity;
import com.example.aimeliveapp.R;

import java.util.List;

public class MatchResultActivity extends AppCompatActivity {
    RecyclerView rcv_matchmakeresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        initViews();


    }

    private void initViews() {
        List<GetMatch> list = MatchMakingActivity.LIST;
        rcv_matchmakeresult = findViewById(R.id.rcv_matchmakeresult);
        if (list != null) {
            MatchMakerResultAdapter adapter = new MatchMakerResultAdapter(this, list);
            rcv_matchmakeresult.setAdapter(adapter);
            rcv_matchmakeresult.setLayoutManager(new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false));
            adapter.notifyDataSetChanged();
        }


    }
}