package in.divyamary.moviereel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.divyamary.moviereel.R;
import in.divyamary.moviereel.model.Review;

/**
 * Created by divyamary on 17-01-2016.
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder> {
    private List<Review> mReviewList;
    private Context mContext;

    public MovieReviewAdapter(Context context, List<Review> reviewList) {
        this.mContext = context;
        this.mReviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_review, parent, false);
        return new ReviewViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviewList.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
        holder.textView.setText(mContext.getString(R.string.review_by));
        holder.urlView.setText(review.getUrl());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addData(List<Review> reviewList) {
        mReviewList.addAll(reviewList);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text_author)
        public TextView authorTextView;
        @Bind(R.id.text_content)
        public TextView contentTextView;
        @Bind(R.id.text_review_by)
        public TextView textView;
        @Bind(R.id.text_review_url)
        public TextView urlView;
        private Context context;

        public ReviewViewHolder(Context context, View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.context = context;
        }
    }
}
