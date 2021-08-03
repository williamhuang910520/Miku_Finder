package com.hanchi.arofinder;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<VideoReportModel> reportModels;

    class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView title, author, description, videoLength;
        private final ImageView picture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_info_list_textview_title);
            author = itemView.findViewById(R.id.video_info_list_textview_author);
            description = itemView.findViewById(R.id.video_info_list_textview_description);
            picture = itemView.findViewById(R.id.video_info_list_imageview);
            videoLength = itemView.findViewById(R.id.video_info_list_textview_length);
        }

        public TextView getTitle() {
            return title;
        }

        public ImageView getPicture() {
            return picture;
        }

        public TextView getAuthor() {
            return author;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getVideoLength() {
            return videoLength;
        }
    }

    public MyAdapter(List<VideoReportModel> dataSet) {
        reportModels = dataSet;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_info_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.getTitle().setText(reportModels.get(position).TITLE);
        holder.getAuthor().setText(reportModels.get(position).AUTHOR);
        holder.getDescription().setText(reportModels.get(position).DESCRIPTION);
        holder.getVideoLength().setText(reportModels.get(position).VIDEO_LENGTH); //TODO
        String url = reportModels.get(position).IMAGE_URL;
//        holder.getPicture().setImageURI(Uri.parse(uri));
        Picasso.get().load(url).into(holder.getPicture());
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }
}
