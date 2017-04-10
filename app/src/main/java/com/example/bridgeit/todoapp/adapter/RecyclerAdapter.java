package com.example.bridgeit.todoapp.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bridgeit.todoapp.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TaskHolder> implements View.OnClickListener {
    Context context;
    AppCompatTextView textView;
    List<String> data;
    CardView cardView;
    private static LayoutInflater inflater = null;

    public RecyclerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_todo_main, parent, false);
        TaskHolder viewHolder = new TaskHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {

        textView.setText(data.get(position));
        // cardView.setOnClickListener(this);

    }

    public int getItemCount() {
        return data.size();
    }

    public void addNote(String strdata) {
        data.add(strdata);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        public TaskHolder(View itemView) {
            super(itemView);
            textView = (AppCompatTextView) itemView.findViewById(R.id.mytextView);
            cardView = (CardView) itemView.findViewById(R.id.myCardView);
            // Toast.makeText(this,""+textView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void addItem(String information) {
        data.add(information);
        notifyItemInserted(data.size());
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);

        }
    }

}
