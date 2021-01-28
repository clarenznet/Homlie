package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luns.neuro.mlkn.R;

import java.util.List;

//import com.squareup.picasso.Picasso;

//import com.pacific.smartbuyer.model.Image;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class HelpItemsAdapter extends RecyclerView.Adapter<HelpItemsAdapter.MyViewHolder> {

    private Context vContext;
    private List<HelpItems> hlpList;
    private static int currentPosition = 0;

    Context contextv;

    public HelpItemsAdapter(List<HelpItems> hlpList, Context vContext) {
        this.vContext = vContext;
        this.hlpList = hlpList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hlep_listrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HelpItems hlp = hlpList.get(position);
        holder.tvHelpId.setText(hlp.getStrHlpId());
        holder.tvHelpCategory.setText("Category: " + hlp.getStrHlpCategory());
        holder.tvHelpIssue.setText(hlp.getStrHlpIssue());
        holder.tvHelpDescription.setText(hlp.getStrHlpDescription());
        holder.llytslidedown.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(vContext, R.anim.slidedown);

            //toggling visibility
            holder.llytslidedown.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.llytslidedown.startAnimation(slideDown);
        }

        holder.tvHelpIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });
    }

    public void setFilter(List<HelpItems> data) {
        this.hlpList.clear();
        this.hlpList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return hlpList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public TextView tvNotifId, tvNotifTitle,tvNotifBody,tvNotifTime,tvNfIconText;
        public TextView tvHelpId, tvHelpCategory, tvHelpIssue, tvHelpDescription;
        public View hlp_layoutcard, llytslidedown;

        public MyViewHolder(View view) {
            super(view);
            tvHelpId = view.findViewById(R.id.tvHelpId);
            tvHelpCategory = view.findViewById(R.id.tvHelpTitle);
            tvHelpIssue = view.findViewById(R.id.tvHelpIssue);
            tvHelpDescription = view.findViewById(R.id.tvHelpDescription);
            hlp_layoutcard = view.findViewById(R.id.hlp_layoutcard);
            llytslidedown = view.findViewById(R.id.llytslidedown);
        }
    }

}
