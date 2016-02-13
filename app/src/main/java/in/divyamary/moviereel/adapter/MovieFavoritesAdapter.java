package in.divyamary.moviereel.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.data.MovieContract;
import in.divyamary.moviereel.helper.Utils;

/**
 * Created by divyamary on 24-01-2016.
 */
public class MovieFavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Cursor mCursor;
    private MovieAdapterOnClickHandler mClickHandler;

    public MovieFavoritesAdapter(MovieAdapterOnClickHandler onClickHandler) {
        this.mClickHandler = onClickHandler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int movieId = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID));
        Context context = ((ImageViewHolder) holder).imageView.getContext();
        ((ImageViewHolder) holder).nameTextView.setText(mCursor.getString
                (mCursor.getColumnIndex(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_TITLE)));
        ((ImageViewHolder) holder).dateTextView.setText(Utils.getYearFromDate(mCursor.getString
                (mCursor.getColumnIndex(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_RELEASE_DATE))));
        File posterImage = new File(context.getFilesDir(), movieId + "_poster.webp");
        Picasso.with(context).load(posterImage).into(((ImageViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        @Bind(R.id.image_grid_item)
        public ImageView imageView;
        @Bind(R.id.text_movie_date)
        public TextView dateTextView;
        @Bind(R.id.text_movie_name)
        public TextView nameTextView;

        public ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID)));
        }
    }
}
