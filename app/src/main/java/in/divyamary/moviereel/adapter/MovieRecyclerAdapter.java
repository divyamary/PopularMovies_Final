package in.divyamary.moviereel.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.helper.Utils;
import in.divyamary.moviereel.model.Movie;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Movie> mMovieList;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private MovieAdapterOnClickHandler mClickHandler;

    public MovieRecyclerAdapter(List<Movie> movieList, MovieAdapterOnClickHandler onClickHandler) {
        this.mMovieList = movieList;
        this.mClickHandler = onClickHandler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_image, parent, false);
        return new ImageViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Movie movie = mMovieList.get(position);
        Context context = ((ImageViewHolder) holder).imageView.getContext();
        //((ImageViewHolder) holder).dateTextView.setText(Utils.getYearFromDate(movie.getReleaseDate()));
        //((ImageViewHolder) holder).nameTextView.setText(movie.getTitle());
        Uri imageUri = Utils.getImageURI(context.getString(R.string.url_base_image),
                context.getString(R.string.poster_image_size), movie.getPosterPath());
        Picasso.with(context)
                .load(imageUri)
                .into(((ImageViewHolder) holder).imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ((ImageViewHolder) holder).dateTextView.setText(Utils.getYearFromDate(movie.getReleaseDate()));
                        ((ImageViewHolder) holder).dateTextView.setVisibility(View.VISIBLE);
                        ((ImageViewHolder) holder).nameTextView.setText(movie.getTitle());
                        ((ImageViewHolder) holder).nameTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addData(List<Movie> moviesList, Boolean isRefresh) {
        if (isRefresh) {
            mMovieList.clear();
        }
        mMovieList.addAll(moviesList);
    }

    public List<Movie> getData() {
        return mMovieList;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.image_grid_item)
        public ImageView imageView;
        @Bind(R.id.text_movie_date)
        public TextView dateTextView;
        @Bind(R.id.text_movie_name)
        public TextView nameTextView;
        private Context context;

        public ImageViewHolder(Context context, View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.context = context;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            mClickHandler.onClick(mMovieList.get(position).getId());
        }
    }

}
