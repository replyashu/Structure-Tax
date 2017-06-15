package com.structuremytax.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.structuremytax.R;

/**
 * Created by apple on 08/04/17.
 */

public class Dashboard_Fragment extends Fragment{

    private TextView txtView;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private double t;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_dashboard,container, false);

        txtView = (TextView) view.findViewById(R.id.textView);
        fetchAndPopulate();

        return view;
    }

    private void fetchAndPopulate(){

        final String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("savings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(android_id)) {
                    txtView.setText("Points:  " +  snapshot.child(
                            android_id).getValue());
                    // run some code
                }
                else
                    txtView.setText("Points:  0" );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database = FirebaseDatabase.getInstance();

        // Get a reference to the todoItems child items it the database
        myRef = database.getReference("savings/");

        t = 0;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    t += (Double)childDataSnapshot.getValue();


////                    GlobalPoints points = childDataSnapshot.getValue();
////                    pointses.add(points);
//
////                    Log.d("pointaa", childDataSnapshot.getValue(String.class));
////
////                    String points = childDataSnapshot.getValue(String.class);
//                     int total = pointses.size();
//
//                    for(int i = 0; i < total; i++)
//                        t += pointses.get(i).getPoints();
//
////                    txtGlobalSavedPoints.setText(t);

//                    adapter = new ShopAdapter(getActivity(),categories, ShopFragment.this);
//                    adapter.notifyDataSetChanged();
//                    recyclerView.setAdapter(adapter);
                }

                startCountAnimation((int)t);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                dataSnapshot.getChildren().iterator().next().getValue(Banner.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startCountAnimation(int t) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, t);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                txtView.setText("Monthly saved so far in Rs(Tentative) \n Rs. " + (int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
