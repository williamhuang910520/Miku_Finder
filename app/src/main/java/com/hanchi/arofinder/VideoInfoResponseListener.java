package com.hanchi.arofinder;

import com.android.volley.VolleyError;

import java.util.List;

public interface VideoInfoResponseListener {
    void onResponse(List<VideoReportModel> reportModels);
    void onError(VolleyError error);
}
