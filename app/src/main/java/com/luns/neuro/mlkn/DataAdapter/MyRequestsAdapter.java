package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luns.neuro.mlkn.R;

import java.util.List;

//import com.squareup.picasso.Picasso;

//import com.pacific.smartbuyer.model.Image;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyRequests> allMyRequestsList;


    Context context;
    public MyRequestsAdapter(List<MyRequests>allMyRequestsList) {
        this.mContext = mContext;
        this.allMyRequestsList = allMyRequestsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myrequests_listrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MyRequests noty = allMyRequestsList.get(position);
        //holder.tvMyRequestsId.setText(noty.getStrRequestTypeUrl());
        holder.tvMyRequestsTitle.setText(noty.getStrRequestTitle());
        holder.tvMyRequestsBody.setText("Ticket ID: " + noty.getStrRequestTcktCode());
        holder.tvMyRequestsStatus.setText(noty.getStrRequestStatus());
        holder.tvMyRequestsTime.setText(noty.getStrCtreatedAt());
        //holder.tvMyRequestsIcon_text.setText(noty.getStrRequestTypeUrl());
        try {

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.imagenotfound)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();
            context = holder.itemView.getContext();


            Glide.with(context)
                    .load(noty.getStrRequestTypeUrl())
                    .apply(options)
                    .into(holder.menuIcon);
        } catch (NullPointerException df) {

        }
//        imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
//        imageLoader.get(noty.getStrRequestTypeUrl(), ImageLoader.getImageListener(holder.menuIcon, R.drawable.placer, android.R.drawable.ic_dialog_alert));
//        holder.menuIcon.setImageUrl(noty.getStrRequestTypeUrl(), imageLoader);
//

        //holder.tvMyRequestsIcon_text.setText(noty.getStrRequestTitle().substring(0, 1));
        //applyProfilePicture(holder, noty.getStrRequestTypeUrl());
        //holder.offerimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                Integer taggedPosition = (Integer) v.getTag();
//                Intent intent = new Intent(mContext, DetailsActivity.class);
//                //intent.putExtra(SearchMovie.MOVIE_TRANSFER, getMovie(taggedPosition));
//                intent.putExtra("strOrdrid",all.getStrOrderId());
//                intent.putExtra("strparentclass","mainactivity");
//                v.getContext().startActivity(intent);
//            }
//
//        });


    }

    private void applyProfilePicture(MyViewHolder holder, String strIconurl) {
        //Random rnd = new Random();
        //int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        view.setBackgroundColor(color);
        //holder.menuIcon.setImageResource(R.drawable.bg_circle);


    }

    //    List<MyRequests>allMyRequestsList
    public void setFilter(List<MyRequests> data) {
        this.allMyRequestsList.clear();
        this.allMyRequestsList.addAll(data);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return allMyRequestsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public TextView tvNotifId, tvNotifTitle,tvNotifBody,tvNotifTime,tvNfIconText;
        public TextView tvMyRequestsId, tvMyRequestsIcon_text, tvMyRequestsTitle, tvMyRequestsBody, tvMyRequestsTime, tvMyRequestsStatus;
        public ImageView menuIcon;
        public View myrequests_layoutcard;

        public MyViewHolder(View view) {
            super(view);
            tvMyRequestsId = view.findViewById(R.id.tvMyRequestsId);
            tvMyRequestsTitle = view.findViewById(R.id.tvMyRequestsTitle);
            tvMyRequestsBody = view.findViewById(R.id.tvMyRequestsBody);
            tvMyRequestsStatus = view.findViewById(R.id.tvMyRequestsStatus);
            tvMyRequestsTime = view.findViewById(R.id.tvMyRequestsTime);
            tvMyRequestsIcon_text = view.findViewById(R.id.tvMyRequestsIcon_text);
            menuIcon = view.findViewById(R.id.icon_thumbnail);
            myrequests_layoutcard = view.findViewById(R.id.myrequests_layoutcard);
        }
    }

}
