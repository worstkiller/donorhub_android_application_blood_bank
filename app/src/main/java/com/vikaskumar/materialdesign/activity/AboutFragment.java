package com.vikaskumar.materialdesign.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vikaskumar.materialdesign.R;

/**
 * Created by vikas kumar on 8/22/2015.
 */
public class AboutFragment extends Fragment {
    Button webButton;
    Context contextWeb;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        webButton = (Button) view.findViewById(R.id.button_web);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = "http://donorhub.co.nf";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    getActivity().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    getActivity().getApplicationContext().startActivity(intent);
                }
            }
        });
        return view;
    }
}
