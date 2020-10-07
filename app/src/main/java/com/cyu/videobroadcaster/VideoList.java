package com.cyu.videobroadcaster;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoList extends RecyclerView.Adapter<VideoList.ViewHolder> {
    private List<String> titles;
    private List<Drawable> thumbnails;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView thumbnail;
        public TextView title;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            title = (TextView) v.findViewById(R.id.title);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        }
    }

    public void add(int position, String title, String desc, Drawable drawable) {
        titles.add(position, title);
        thumbnails.add(position, drawable);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        titles.remove(position);
        thumbnails.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoList(List<String> titles, List<Drawable> thumbnails) {
        this.titles = titles;
        this.thumbnails = thumbnails;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VideoList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_video_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final String title = titles.get(position);
        final Drawable image = thumbnails.get(position);
        holder.title.setText(title);
        holder.thumbnail.setImageDrawable(image);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : On Click -> Broadcast video
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return titles.size();
    }

}