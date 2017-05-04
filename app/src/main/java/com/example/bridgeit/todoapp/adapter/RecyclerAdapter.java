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
import com.example.bridgeit.todoapp.model.NotesModel;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TaskHolder> implements View.OnClickListener {
    Context context;
    AppCompatTextView textViewtitle;
    AppCompatTextView textViewdesc;
    AppCompatTextView textViewdate;

    List<NotesModel> model;
    CardView cardView;
    private static LayoutInflater inflater = null;

    public RecyclerAdapter(Context context, List<NotesModel> data) {
        this.context = context;
        this.model = data;
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
    NotesModel notesModel=model.get(position);
        textViewtitle.setText(notesModel.getTitle());
        textViewdesc.setText(notesModel.getDescription());
        textViewdate.setText(notesModel.getNoteDate());
        // cardView.setOnClickListener(this);

    }

    public int getItemCount() {
        return model.size();
    }

    public void addNote(NotesModel notesModel) {
        model.add(notesModel);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        public TaskHolder(View itemView) {
            super(itemView);
            textViewdate = (AppCompatTextView) itemView.findViewById(R.id.datetextview);
            textViewtitle = (AppCompatTextView) itemView.findViewById(R.id.titletextview);
            textViewdesc = (AppCompatTextView) itemView.findViewById(R.id.descriptiontextview);
           // cardView = (CardView) itemView.findViewById(R.id.myCardView);
            // Toast.makeText(this,""+textView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void addItem(NotesModel notesModel) {
        model.add(notesModel);
        notifyItemInserted(model.size());
    }

    public void addItem(NotesModel notesModel, int pos) {
        removeItem(pos);
        model.add(pos,notesModel);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        model.remove(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);

        }
    }

}
