package com.app.todo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.updatenotes.ui.UpdateNoteFragment;
import com.example.bridgeit.todoapp.R;
import com.app.todo.model.NotesModel;
import java.util.ArrayList;
import java.util.List;

public class
RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TaskHolder>  {
    Context context;
    List<NotesModel> notesModelList;
  //  List<NotesModel> model;
    private Bundle bund;
    //ToDoMainActivity toDoMainActivity;

    public RecyclerAdapter(Context context) {
        this.context = context;
        this.notesModelList = new ArrayList<>();
       // this.model=model;
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_note, parent, false);
        TaskHolder viewHolder = new TaskHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskHolder holder,final int position) {
        NotesModel notesModel = notesModelList.get(position);
        holder.textViewtitle.setText(notesModel.getTitle());
        holder.textViewdesc.setText(notesModel.getDescription());
        holder.textViewdate.setText(notesModel.getReminderDate());
        if (notesModel.getColor() != null) {
            holder.linearLayout.setBackgroundColor(Integer.parseInt(notesModel.getColor()));
        }
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

    public class TaskHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView textViewtitle;
        public AppCompatTextView textViewdesc;
        public AppCompatTextView textViewdate;
        public LinearLayout linearLayout;
        CardView cardView;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewdate = (AppCompatTextView) itemView.findViewById(R.id.datetextview);
            textViewtitle = (AppCompatTextView) itemView.findViewById(R.id.titletextview);
            textViewdesc = (AppCompatTextView) itemView.findViewById(R.id.descriptiontextview);
            cardView = (CardView) itemView.findViewById(R.id.myCardView);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.cardviewlayout);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bund = new Bundle();
                    int position =getAdapterPosition();
                    bund.putInt("id", notesModelList.get(position).getId());
                    bund.putString("currentDate", notesModelList.get(position).getNoteDate());
                    bund.putString("title", notesModelList.get(position).getTitle());
                    bund.putString("description", notesModelList.get(position).getDescription());
                    bund.putString("reminddate", notesModelList.get(position).getReminderDate());
                    bund.putString("color",notesModelList.get(position).getColor());
                    UpdateNoteFragment updateNoteFragment = new UpdateNoteFragment();
                    updateNoteFragment.setArguments(bund);
                    ((ToDoMainActivity)context).getFragmentManager().beginTransaction()
                            .replace(R.id.fragment,updateNoteFragment)
                            .addToBackStack(null).commit();
                }
            });
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
