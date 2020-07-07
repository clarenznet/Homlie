package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.luns.neuro.mlkn.R;
import com.luns.neuro.mlkn.library.CustomVolleyRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class FundiTypesAdapter extends RecyclerView.Adapter<FundiTypesAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<FundiTypes> allFt;
    private List<FundiTypes> allFtFiltered;

    //private NetworkImageView imageView;
    private ImageLoader imageLoader;
    private FundiTypesAdapterListener listener;

    //private NetworkImageView thumbnail;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFtId, tvFtTitle,tvFtDescription,tvFtIconText;
        public NetworkImageView dpFtThumbnail;
        public MyViewHolder(View view) {
            super(view);
            tvFtId = (TextView) view.findViewById(R.id.tvFtId);
            tvFtTitle = (TextView) view.findViewById(R.id.tvFtTitle);
            dpFtThumbnail = (NetworkImageView) view.findViewById(R.id.dpFtThumbnail);
            tvFtDescription = (TextView) view.findViewById(R.id.tvFtDescription);
            tvFtIconText = (TextView) view.findViewById(R.id.tvFtIcon_text);
        }
    }

    public FundiTypesAdapter(Context mContext, List<FundiTypes> allFt, FundiTypesAdapterListener listener) {
        this.mContext = mContext;
        this.allFt = allFt;
        this.listener = listener;
        this.allFtFiltered = allFt;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.funditypes_card, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FundiTypes all = allFtFiltered.get(position);
        holder.tvFtTitle.setText(all.getStrFtId());
        holder.tvFtTitle.setText( all.getStrFtTitle());
        holder.tvFtDescription.setText( all.getStrFtDescription());
        //imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
        //imageLoader.get(all.getStrFtUrl(), ImageLoader.getImageListener(holder.dpFtThumbnail, R.drawable.placer, R.drawable.placer));
        //holder.dpFtThumbnail.setImageUrl(all.getStrFtUrl(), imageLoader);
        holder.tvFtIconText.setText(all.getStrFtTitle().substring(0, 1));
        // display profile image
        applyProfilePicture(holder, all);
    }
    private void applyProfilePicture(MyViewHolder holder, FundiTypes clist) {
        holder.dpFtThumbnail.setImageResource(R.drawable.bg_circle);
        holder.dpFtThumbnail.setColorFilter(Color.BLUE);
        //holder.tvFtIconText.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return allFtFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allFtFiltered = allFt;
                } else {
                    List<FundiTypes> filteredList = new ArrayList<>();
                    for (FundiTypes row : allFt) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getStrFtTitle().toLowerCase().contains(charString.toLowerCase())
                        ){
                            filteredList.add(row);
                        }
                    }

                    allFtFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allFtFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allFtFiltered = (ArrayList<FundiTypes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface FundiTypesAdapterListener {
        void onFundiTypeSelected(FundiTypes all);
    }

}