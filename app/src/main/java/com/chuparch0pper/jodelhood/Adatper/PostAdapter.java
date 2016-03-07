package com.chuparch0pper.jodelhood.Adatper;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chuparch0pper.jodelhood.R;
import com.chuparch0pper.jodelhood.backend.JodelPost;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private JodelPost jodelPost;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public Button btnVotes;
        public TextView txtContent;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            btnVotes = (Button) view.findViewById(R.id.btn_votes);
            txtContent = (TextView) view.findViewById(R.id.card_content);

        }
    }

    public PostAdapter(JodelPost jodelPost) {
        this.jodelPost = jodelPost;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int jodelColor = Color.parseColor("#" + jodelPost.getColor());

        holder.cardView.setBackgroundColor(jodelColor);

        // holder.btnVotes.setBackgroundColor(Color.parseColor("#" + jodelPost.getColor()));
        holder.btnVotes.setBackgroundResource(R.drawable.roundedbutton);
        GradientDrawable drawable = (GradientDrawable) holder.btnVotes.getBackground();
        drawable.setColor(darker(jodelColor, 0.8f));


        holder.btnVotes.setText(jodelPost.getVote_count().get(position).toString());
        holder.txtContent.setText(jodelPost.getMessage().get(position));

    }

    @Override
    public int getItemCount() {
        return jodelPost.getNumOfEntries();
    }

    /**
     * Returns darker version of specified <code>color</code>.
     */
    private static int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

}