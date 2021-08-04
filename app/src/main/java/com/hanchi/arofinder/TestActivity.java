package com.hanchi.arofinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private final String TAG = "TestActivity";

    TextView textView;
    EditText editText;
    Button button;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.testActivity_txv_viewInfo);
        editText = findViewById(R.id.testActivity_edittext_keyword);
        button = findViewById(R.id.testActivity_button_confirm);
        recyclerView = findViewById(R.id.testActivity_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDataInfoService videoDataInfoService = new VideoDataInfoService(TestActivity.this);
                SearchRequirement searchRequirement = new SearchRequirement();
//                searchRequirement.setKeyword(editText.getText().toString());
                searchRequirement.setKeyword("狼音アロ");
                videoDataInfoService.getYoutubeVideoInfoByKeyword(searchRequirement, new VideoInfoResponseListener() {
                    @Override
                    public void onResponse(List<VideoReportModel> reportModels) {
//                        String title_1 = null;
//                        title_1 = reportModels.get(0).TITLE;
//                        textView.setText(title_1);

                        MyAdapter adapter = new MyAdapter(reportModels);
                        recyclerView.setAdapter(adapter);
                        Log.d(TAG, "Successfully get values.");
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                });
            }
        });


    }
}