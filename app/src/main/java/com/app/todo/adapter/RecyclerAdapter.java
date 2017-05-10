package com.app.todo.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bridgeit.todoapp.R;
import com.app.todo.model.NotesModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TaskHolder> implements View.OnClickListener {
    Context context;
    List<NotesModel> notesModelList;

    public RecyclerAdapter(Context context) {
        this.context = context;
        this.notesModelList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_note, parent, false);
        TaskHolder viewHolder = new TaskHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {
    NotesModel notesModel= notesModelList.get(position);
        holder.textViewtitle.setText(notesModel.getTitle());
        holder.textViewdesc.setText(notesModel.getDescription());
        holder.textViewdate.setText(notesModel.getNoteDate());
        // cardView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public void setNoteList(List<NotesModel> noteList){
        notesModelList.clear();
        notifyDataSetChanged();
        notesModelList.addAll(noteList);
        notifyDataSetChanged();
    }

    public  void searchNotes(ArrayList<NotesModel> newList){
        notesModelList.clear();
        notesModelList.addAll(newList);
        notifyDataSetChanged();

        Log.i("val ", "searchNotes: "+ notesModelList.get(notesModelList.size()-1).getTitle());
    }

    public void addNote(NotesModel notesModel) {
        notesModelList.add(notesModel);
        notifyItemInserted(notesModelList.size());
    }

    public void removeItem(int position) {
        notesModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notesModelList.size());
    }

    public void updateItem(int pos, NotesModel model){
        notesModelList.set(pos, model);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewtitle;
        public AppCompatTextView textViewdesc;
        public AppCompatTextView textViewdate;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewdate = (AppCompatTextView) itemView.findViewById(R.id.datetextview);
            textViewtitle = (AppCompatTextView) itemView.findViewById(R.id.titletextview);
            textViewdesc = (AppCompatTextView) itemView.findViewById(R.id.descriptiontextview);
           // cardView = (CardView) itemView.findViewById(R.id.myCardView);
            // Toast.makeText(this,""+textView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem(NotesModel notesModel, int pos) {
        removeItem(pos);
        notesModelList.add(pos,notesModel);
        notifyDataSetChanged();
    }

    public NotesModel getNoteModel(int pos){
        return notesModelList.get(pos);
    }

    public List<NotesModel> getAllDataList(){
        return notesModelList;
    }
}
