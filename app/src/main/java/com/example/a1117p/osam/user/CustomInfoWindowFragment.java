package com.example.a1117p.osam.user;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;

public class CustomInfoWindowFragment extends Fragment {

    InfoWindowData data;
    AppCompatActivity context;
    private View mWindow;
   ListView listView;

    CustomInfoWindowFragment(InfoWindowData data, AppCompatActivity context) {
        this.data = data;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWindow = inflater.inflate(R.layout.custom_info_window, container, false);
        return mWindow;
    }

    void downReview() {
        context.findViewById(R.id.review).setClickable(false);
        context.findViewById(R.id.down).setClickable(false);
        context.findViewById(R.id.bottom).startAnimation(AnimationUtils.loadAnimation(context, R.anim.down_anim));
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        render(view);
        //final  LinearLayout linearLayout = context.findViewById(R.id.map_linear);
        mWindow.findViewById(R.id.recipt_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new ResvtnClickDialog(context, data);
                dialog.show();

            }
        });
        listView = context.findViewById(R.id.review_list);
        mWindow.findViewById(R.id.review_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog Pdialog = new ProgressDialog(context);
                Pdialog.setMessage("리뷰를 불러오는중입니다.");

                Pdialog.show();
                listView.setAdapter(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final HashMap params = new HashMap<String, String>();

                                try {
                                    final String html = RequestHttpURLConnection.request("https://be-light.store/api/reviews?hostIdx=" + data.hostIdx, params, true, "GET");
                                    JSONParser jsonParser = new JSONParser();
                                    JSONArray jsonArray = (JSONArray) jsonParser.parse(html);
                                    final ReviewListAdapter adapter = new ReviewListAdapter(jsonArray);
                                    context.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            Pdialog.dismiss();
                                            context.findViewById(R.id.bottom).startAnimation(AnimationUtils.loadAnimation(context, R.anim.logo_anim));
                                            context.findViewById(R.id.review).setClickable(true);
                                            context.findViewById(R.id.down).setClickable(true);
                                            context.findViewById(R.id.down).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    downReview();
                                                }
                                            });
                                            listView.setAdapter(adapter);
                                        }

                                    });
                                } catch (final Exception e) {
                                    context.runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }

                                    });
                                }

                    }
                }).start();

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        ((ViewGroup) view.getParent()).setBackgroundColor(Color.TRANSPARENT);
                    }

                });

            }
        }).start();

    }

    private void render(View view) {
        ((TextView) view.findViewById(R.id.host_name)).setText(data.hostName);
        ((TextView) view.findViewById(R.id.host_num)).setText(data.hostTel);
        ((TextView) view.findViewById(R.id.host_addr)).setText(data.hostAddress);
    }
}