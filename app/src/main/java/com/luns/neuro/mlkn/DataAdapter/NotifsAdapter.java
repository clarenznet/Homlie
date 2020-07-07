package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.luns.neuro.mlkn.R;

import java.util.List;
import java.util.Random;

//import com.squareup.picasso.Picasso;

//import com.pacific.smartbuyer.model.Image;


/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class NotifsAdapter extends RecyclerView.Adapter<NotifsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Notifs> allNotifsList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNotifId, tvNotifTitle,tvNotifBody,tvNotifTime,tvNfIconText;
        public ImageView menuIcon;
        public View notif_layoutcard;

        public MyViewHolder(View view) {
            super(view);
            tvNotifId = (TextView) view.findViewById(R.id.tvNotifId);
            tvNotifTitle = (TextView) view.findViewById(R.id.tvNotifTitle);
            tvNotifBody = (TextView) view.findViewById(R.id.tvNotifBody);
            tvNotifTime = (TextView) view.findViewById(R.id.tvNotifTime);
            tvNfIconText = (TextView) view.findViewById(R.id.tvNfIcon_text);
            menuIcon = (ImageView) view.findViewById(R.id.icon_thumbnail);
            notif_layoutcard=view.findViewById(R.id.notif_layoutcard);
        }
    }
    public NotifsAdapter(List<Notifs> allNotifsList) {
        this.mContext = mContext;
        this.allNotifsList = allNotifsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_listrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Notifs noty = allNotifsList.get(position);
        holder.tvNotifId.setText(noty.getStrNotifId());
        holder.tvNotifTitle.setText(noty.getStrNotifTitle());
        holder.tvNotifBody.setText("Ticket #"+noty.getStrNotifId()+" "+noty.getStrNotifBody());
        holder.tvNotifTime.setText(noty.getStrNotifTime());
        holder.tvNfIconText.setText(noty.getStrNotifTitle().substring(0, 1));
        //applyProfilePicture(holder, noty.getStrNotifColor());


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
    private void applyProfilePicture(MyViewHolder holder,String notifColor) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        view.setBackgroundColor(color);

        holder.menuIcon.setImageResource(R.drawable.bg_circle);
        if (notifColor.equals(""))
        holder.menuIcon.setColorFilter(Color.parseColor("#00Afff"));
        else
            holder.menuIcon.setColorFilter(Color.parseColor(notifColor));
        holder.tvNfIconText.setVisibility(View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return allNotifsList.size();
    }

}
