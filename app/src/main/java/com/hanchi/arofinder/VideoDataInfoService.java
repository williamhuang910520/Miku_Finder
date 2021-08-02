package com.hanchi.arofinder;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoDataInfoService {

    private static final String TAG = "VideoDataInfoService";
    private Context context;

    public static final String NICO_URL_API = "https://api.search.nicovideo.jp/api/v2/snapshot/video/contents/search";

    public VideoDataInfoService(Context context){
        this.context = context;
    }

    public void getNicoVideoInfoByKeyword(SearchRequirement searchRequirement, final VideoInfoResponseListener videoInfoResponseListener){
        String keyword = searchRequirement.getKeyword();
//        keyword = "狼音アロ";
        String nicoFilter = "[viewCounter][gte]=10000";
        String nicoSort = "startTime";
        String url_nico = NICO_URL_API
                + "?q=" + keyword
                + "&targets=title,description,tags"
                + "&fields=title,userId,lengthSeconds,description,contentId,thumbnailUrl"
//                + "&filters" + nicoFilter
                + "&_sort=" + nicoSort
//                + "&_offset=0"
                + "&_limit=3"
                + "&_context=finderTest";
//                "https://api.search.nicovideo.jp/api/v2/snapshot/video/contents/search?q=%E5%88%9D%E9%9F%B3%E3%83%9F%E3%82%AF&targets=title&fields=contentId,title,viewCounter&filters[viewCounter][gte]=10000&_sort=-viewCounter&_offset=0&_limit=3&_context=apiguide";

        Log.d(TAG, url_nico);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_nico, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<VideoReportModel> reportModels = new ArrayList<>();
                        try {
                            JSONArray item = response.getJSONArray("data");
                            for (int i = 0; i < item.length(); i++) {
                                VideoReportModel model = new VideoReportModel();
                                JSONObject jsonObject = item.getJSONObject(i);
                                model.AUTHOR = jsonObject.getString("userId");
                                model.TITLE = jsonObject.getString("title");
//                                model.COMMENT_COUNTER = jsonObject.getString("title");
                                model.DESCRIPTION = jsonObject.getString("description");
                                model.IMAGE_URL = jsonObject.getString("thumbnailUrl");
//                                model.LIKE_COUNTER = jsonObject.getString("title");
//                                model.ORIGINAL_LINK = jsonObject.getString("title");
//                                model.UPLOAD_DATE = jsonObject.getString("title");
                                model.VIDEO_LENGTH = jsonObject.getString("lengthSeconds");
                                reportModels.add(model);
                            }
                            videoInfoResponseListener.onResponse(reportModels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText(error.toString());
                Log.d(TAG, error.toString());
                videoInfoResponseListener.onError(error);
            }
        });

        MyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getYoutubeVideoInfoByKeyword(String keyword){

    }

    public void getBiliVideoInfoByKeyword(String keyword){

    }

    public void getSoundCloudVideoInfoByKeyword(String keyword){

    }
}
