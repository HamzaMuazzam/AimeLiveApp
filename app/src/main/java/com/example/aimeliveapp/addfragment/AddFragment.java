package com.example.aimeliveapp.addfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.aimeliveapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    public static final String TAG = "MYTAGs";

    public AddFragment() {
        // Required empty public constructor
    }

    Context context;
    VerticalViewPager verticalViewPager;
    PagerAdapter pagerAdapter;
    View view;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        context = getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("videos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot result = task.getResult();
                    List<DocumentSnapshot> documentsList = result.getDocuments();
                    verticalViewPager = view.findViewById(R.id.pager);
                    try {

                        verticalViewPager.setOffscreenPageLimit(1);
                    } catch (Exception e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    try {

                        pagerAdapter = new ViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), 1, documentsList, context);
                        verticalViewPager.setAdapter(pagerAdapter);
                    } catch (Exception e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        return view;
    }



}
