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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoDataInfoService {

    private static final String TAG = "VideoDataInfoService";
    private Context context;

    public static final String NICO_URL_API = "https://api.search.nicovideo.jp/api/v2/snapshot/video/contents/search";
    public static final String YOUTUBE_DATA_API = "https://www.googleapis.com/youtube/v3/";


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
                + "&_limit=10"
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
//                                model.VIDEO_LENGTH = jsonObject.getString("lengthSeconds");
                                long second = Long.parseLong(jsonObject.getString("lengthSeconds"));
                                model.VIDEO_LENGTH = new SimpleDateFormat("m:ss").format(new Date(second*1000));
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

    public void getYoutubeVideoInfoByKeyword(SearchRequirement searchRequirement, final VideoInfoResponseListener videoInfoResponseListener){
        String keyword = searchRequirement.getKeyword();
//        String nicoFilter = "[viewCounter][gte]=10000";
        String youtubeOrder = "date";

        String url = YOUTUBE_DATA_API +
                "search?" +
                "q=" + keyword +
                "&type=video" +
                "&part=snippet" +
                "&fields=items(id,snippet)" +
//              "&filters" + nicoFilter   +
                "&order=" + youtubeOrder +
                "&maxResults=10" +
                "&key=" + MyAPIKey.YOUTUBE_DATA_API_KEY;

        Log.d(TAG, url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<VideoReportModel> reportModels = new ArrayList<>();
                try {
                    JSONArray item = response.getJSONArray("items");
                    for (int i = 0; i < item.length(); i++) {
                        VideoReportModel model = new VideoReportModel();
                        JSONObject jsonObject = item.getJSONObject(i).getJSONObject("snippet");
                        model.AUTHOR = jsonObject.getString("channelTitle");
                        model.TITLE = jsonObject.getString("title");
//                                model.COMMENT_COUNTER = jsonObject.getString("title");
                        model.DESCRIPTION = jsonObject.getString("description");
                        model.IMAGE_URL = jsonObject.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                        model.YT_VIDEO_ID = item.getJSONObject(i).getJSONObject("id").getString("videoId");
//                                model.LIKE_COUNTER = jsonObject.getString("title");
//                                model.ORIGINAL_LINK = jsonObject.getString("title");
//                                model.UPLOAD_DATE = jsonObject.getString("title");
//                                model.VIDEO_LENGTH = jsonObject.getString("lengthSeconds");
//                        long second = Long.parseLong(jsonObject.getString("lengthSeconds"));
//                        model.VIDEO_LENGTH = new SimpleDateFormat("m:ss").format(new Date(second*1000));
                        reportModels.add(model);
                    }
//                    videoInfoResponseListener.onResponse(reportModels);
                    getYoutubeVideoMoreInfo(reportModels, videoInfoResponseListener);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

            }
        });

        MyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void getYoutubeVideoMoreInfo(final List<VideoReportModel> reportModels, final VideoInfoResponseListener videoInfoResponseListener){
        String idSet = "";
        for(int i = 0; i<reportModels.size(); i++){
            idSet += reportModels.get(i).YT_VIDEO_ID;
            if(i != reportModels.size()-1) idSet += ",";
        }
        // https://youtube.googleapis.com/youtube/v3/videos?part=contentDetails%2Cstatistics&fields=items(contentDetails(duration),statistics)&id=Iy-JNAUuzYw%2C-u_aPTp_5u8%2CZp7PEiX1zvM&key=AIzaSyCOzktDpznaPMsarODiC4USGOcEmf0nuZw
        String url = YOUTUBE_DATA_API +
                "videos?" +
                "&part=contentDetails,statistics" +
                "&fields=items(contentDetails(duration),statistics)" +
                "&id=" + idSet +
                "&key=" + MyAPIKey.YOUTUBE_DATA_API_KEY;
//        Log.d(TAG, idSet);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray item = response.getJSONArray("items");
                    for (int i = 0; i < item.length(); i++) {
                        VideoReportModel model = reportModels.get(i);
                        JSONObject jsonObject = item.getJSONObject(i).getJSONObject("statistics");
                        model.LIKE_COUNT = jsonObject.getString("likeCount");
                        model.COMMENT_COUNT = jsonObject.getString("commentCount");
                        String length = item.getJSONObject(i).getJSONObject("contentDetails").getString("duration");
                        model.VIDEO_LENGTH = length.substring(2, length.length()-1).replaceAll("[MH]", ":");
                        Log.d(TAG, model.VIDEO_LENGTH);
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
                Log.d(TAG, error.toString());
                videoInfoResponseListener.onError(error);
            }
        });

        MyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getBiliVideoInfoByKeyword(String keyword){

    }

//    SoundCloud don't have API QQ.
//    public void getSoundCloudVideoInfoByKeyword(String keyword){
//
//    }
}
