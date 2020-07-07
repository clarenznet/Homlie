package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.luns.neuro.mlkn.R;

import java.util.List;
import java.util.Random;

//import com.squareup.picasso.Picasso;

//import com.pacific.smartbuyer.model.Image;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyRequests> allMyRequestsList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        //public TextView tvNotifId, tvNotifTitle,tvNotifBody,tvNotifTime,tvNfIconText;
        public TextView tvMyRequestsId,tvMyRequestsIcon_text,tvMyRequestsTitle,tvMyRequestsBody,tvMyRequestsTime,tvMyRequestsStatus;
        public ImageView menuIcon;
        public View myrequests_layoutcard;

        public MyViewHolder(View view) {
            super(view);
            tvMyRequestsId = (TextView) view.findViewById(R.id.tvMyRequestsId);
            tvMyRequestsTitle = (TextView) view.findViewById(R.id.tvMyRequestsTitle);
            tvMyRequestsBody = (TextView) view.findViewById(R.id.tvMyRequestsBody);
            tvMyRequestsStatus = (TextView) view.findViewById(R.id.tvMyRequestsStatus);
            tvMyRequestsTime = (TextView) view.findViewById(R.id.tvMyRequestsTime);
            tvMyRequestsIcon_text = (TextView) view.findViewById(R.id.tvMyRequestsIcon_text);
            menuIcon = (ImageView) view.findViewById(R.id.icon_thumbnail);
            myrequests_layoutcard=view.findViewById(R.id.myrequests_layoutcard);
        }
    }
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
        holder.tvMyRequestsId.setText(noty.getStrRequestId());
        holder.tvMyRequestsTitle.setText(noty.getStrRequestTitle());
        holder.tvMyRequestsBody.setText("Service ticket id #"+noty.getStrRequestId()+" created");
        holder.tvMyRequestsStatus.setText(noty.getStrRequestStatus());
        holder.tvMyRequestsTime.setText(noty.getStrCtreatedAt());
        //holder.tvMyRequestsIcon_text.setText(noty.getStrRequestTitle().substring(0, 1));
        //applyProfilePicture(holder, noty.getStrRequestColor());


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
    private void applyProfilePicture(MyViewHolder holder,String requestColor) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        view.setBackgroundColor(color);
        holder.menuIcon.setImageResource(R.drawable.bg_circle);
        try {
            if (requestColor.equals("") ||requestColor.isEmpty()||requestColor.equals(null)) {
                holder.menuIcon.setColorFilter(Color.parseColor("#00afff"));
                holder.tvMyRequestsStatus.setBackgroundColor(Color.parseColor("#00afff"));
            } else {
                holder.menuIcon.setColorFilter(Color.parseColor(requestColor));
                holder.tvMyRequestsStatus.setBackgroundColor(Color.parseColor(requestColor));
            }
        }catch (NullPointerException ty){

        }

        holder.tvMyRequestsIcon_text.setVisibility(View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return allMyRequestsList.size();
    }

}
