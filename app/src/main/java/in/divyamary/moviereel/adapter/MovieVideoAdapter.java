package in.divyamary.moviereel.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.helper.Utils;
import in.divyamary.moviereel.model.Video;

/**
 * Created by divyamary on 17-01-2016.
 */
public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.VideoViewHolder> {
    private List<Video> mVideosList;

    public MovieVideoAdapter(List<Video> videosList) {
        this.mVideosList = videosList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_video, parent, false);
        return new VideoViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = mVideosList.get(position);
        String videoKey = video.getKey();
        Context context = holder.thumbnail.getContext();
        if (Utils.isInternetConnected(context)) {
            Picasso.with(context)
                    .load(Utils.getYoutubeThumbnail(videoKey))
                    .placeholder(R.drawable.image_thumbnail_error)
                    .error(R.drawable.image_thumbnail_error)
                    .into(holder.thumbnail);
        } else {
            File thumbnailImage = new File(context.getFilesDir(), videoKey + ".webp");
            Picasso.with(context).load(thumbnailImage).into(holder.thumbnail);
        }
        holder.nameTextView.setText(video.getName());
        holder.qualityTextView.setText(String.valueOf(video.getSize()) + "p");
    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addData(List<Video> videoList) {
        mVideosList.addAll(videoList);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.text_video_name)
        public TextView nameTextView;
        @Bind(R.id.text_video_quality)
        public TextView qualityTextView;
        @Bind(R.id.image_video_thumbnail)
        public ImageView thumbnail;
        private Context context;

        public VideoViewHolder(Context context, View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.context = context;
            thumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String videoId = mVideosList.get(getLayoutPosition()).getKey();
            try {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                context.startActivity(viewIntent);
            } catch (ActivityNotFoundException e) {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW,
                        Utils.getYoutubeURI(videoId));
                context.startActivity(viewIntent);
            }
        }
    }
}

