package com.hanchi.arofinder;

public class VideoReportModel {
    public String TITLE;
    public String AUTHOR;
    public String VIDEO_LENGTH;
    public String DESCRIPTION;
    public String IMAGE_URL;
    public String ORIGINAL_LINK;
    public String UPLOAD_DATE;
    public String LIKE_COUNT;
    public String COMMENT_COUNT;

    public String YT_VIDEO_ID;

    public VideoReportModel() {

    }

    @Override
    public String toString() {
        return "VideoReportModel{" +
                "TITLE='" + TITLE + '\n' +
                ", AUTHOR='" + AUTHOR + '\n' +
                ", VIDEO_LENGTH='" + VIDEO_LENGTH + '\'' +
//                ", DESCRIPTION='" + DESCRIPTION + '\'' +
//                ", IMAGE_URL='" + IMAGE_URL + '\'' +
//                ", ORIGINAL_LINK='" + ORIGINAL_LINK + '\'' +
//                ", UPLOAD_DATE='" + UPLOAD_DATE + '\'' +
//                ", LIKE_COUNTER='" + LIKE_COUNTER + '\'' +
//                ", COMMENT_COUNTER='" + COMMENT_COUNTER + '\'' +
                '}';
    }
}
