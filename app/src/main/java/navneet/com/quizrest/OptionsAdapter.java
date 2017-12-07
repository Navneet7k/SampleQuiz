package navneet.com.quizrest;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 07-Dec-2017.
 */

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {
    private ArrayList<String> option;
    private Context context;
    private String correct;
    private int idItem = 0;
    private OptionSelected optionSelected;

    public OptionsAdapter(ArrayList<String> option, Context context,String correct,OptionSelected optionSelected) {
        this.context=context;
        this.option=option;
        this.correct=correct;
        this.optionSelected=optionSelected;
    }

    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.option_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OptionsAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tv_name.setText(option.get(i));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (option.get(i).equals(correct)){
                    Toast.makeText(context,"CORRECT ANSWER :)",Toast.LENGTH_SHORT).show();
                    viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.correctAnswer));
                    viewHolder.itemView.setClickable(false);
                    optionSelected.onOptionCorrect();
                } else {
                    viewHolder.itemView.setClickable(false);
                    Toast.makeText(context,"SORRY INCORRECT ANSWER :(",Toast.LENGTH_SHORT).show();
                    viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.wrongAnswer));
                    optionSelected.onOptionWrong();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return option.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.tv_name);
        }
    }

}

