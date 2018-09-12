package navneet.com.quizrest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user on 07-Dec-2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<User> articles=new ArrayList<>();
    private Context context;
    private String email;

    public DataAdapter(ArrayList<User> articles,Context context,String email) {
        this.context=context;
        this.articles=articles;
        this.email=email;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {


        if (articles.get(i).getEmail().equals(email)) {
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.correctAnswer));
        }

        if (i==0)
        viewHolder.leader.setVisibility(View.VISIBLE);

        viewHolder.tv_name.setText(articles.get(i).getUsername());
        viewHolder.tv_version.setText(articles.get(i).getHighScore());
        viewHolder.right.setText(articles.get(i).getTotalCorrect());

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,right;
        private ImageView leader;


        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            right = (TextView)view.findViewById(R.id.right);
            leader = (ImageView) view.findViewById(R.id.leader);

        }
    }

}
