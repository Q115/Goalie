package com.github.q115.goalie_android.ui.feeds;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.q115.goalie_android.R;

/**
 * Created by Qi on 8/4/2017.
 */

public class FeedsRecycler extends RecyclerView.Adapter {
    public class FeedsHolder extends RecyclerView.ViewHolder {
        private TextView mGoalPerson;
        private TextView mGoalResult;
        private TextView mGoalQuote;
        private TextView mUpvoteCount;
        private Button mGoalFeedAction;

        public FeedsHolder(View itemView) {
            super(itemView);
            mGoalPerson = itemView.findViewById(R.id.goal_person);
            mGoalResult = itemView.findViewById(R.id.goal_result);
            mGoalQuote = itemView.findViewById(R.id.goal_quote);
            mUpvoteCount = itemView.findViewById(R.id.upvote_count);
            mGoalFeedAction = itemView.findViewById(R.id.goal_feed_action);
        }
    }

    protected FragmentActivity mContext;

    public FeedsRecycler(FragmentActivity context) {
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return 10;
        // TODO
        //return mGoalList.size();
    }

    //Must override, this inflates our Layout and instantiates and assigns
    //it to the ViewHolder.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mContext.getLayoutInflater().inflate(R.layout.list_item_feed, parent, false);
        return new FeedsHolder(itemView);
    }

    //Bind our current data to your view holder.  Think of this as the equivalent
    //of GetView for regular Adapters.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedsHolder viewHolder = (FeedsHolder) holder;

        //Bind our data from our data source to our View References
        viewHolder.mGoalPerson.setText("Chris");
        viewHolder.mGoalPerson.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_default_small, 0, 0);

        viewHolder.mGoalResult.setText("Completed a job worth 10 points");
        viewHolder.mGoalQuote.setText("i Did it!!");
        viewHolder.mUpvoteCount.setText("100");
        viewHolder.mUpvoteCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumb_up, 0, 0, 0);

/*
        final int index = position;
        viewHolder.mGoalFeedAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/
    }
}