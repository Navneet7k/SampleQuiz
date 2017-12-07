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
    private ArrayList<Result> android;
    private ArrayList<Result> websites;
    private ArrayList<Result> articles;
    private Context context;

    public DataAdapter(ArrayList<Result> articles, Context context) {
        this.context=context;
        this.articles=articles;}

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {



        viewHolder.tv_name.setText(articles.get(i).getQuestion());
        viewHolder.tv_version.setText(articles.get(i).getCorrectAnswer());
//        final String name=articles.get(i).getTitle();
//        final String author=articles.get(i).getAuthor();
//        final String image=articles.get(i).getUrlToImage();
//        final String date=articles.get(i).getPublishedAt();
//        final String desc=articles.get(i).getDescription();
//        final String more=articles.get(i).getUrl();
//
//        viewHolder.tv_version.setText(articles.get(i).getPublishedAt());
//        Picasso.with(context).load(articles.get(i).getUrlToImage()).into(viewHolder.img);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level;


        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            img=(ImageView)view.findViewById(R.id.img);
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            //     tv_api_level = (TextView)view.findViewById(R.id.tv_api_level);

        }
    }

}
