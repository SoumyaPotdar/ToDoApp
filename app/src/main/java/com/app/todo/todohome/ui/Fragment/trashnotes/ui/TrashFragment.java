package com.app.todo.todohome.ui.Fragment.trashnotes.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.notes.ui.NotesFragment;
import com.app.todo.todohome.ui.Fragment.trashnotes.presenter.TrashPresenter;
import com.app.todo.todohome.ui.Fragment.trashnotes.presenter.TrashPresenterInterface;
import com.app.todo.utils.Constants;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static com.example.bridgeit.todoapp.R.id.search;
import static com.facebook.FacebookSdk.getApplicationContext;
import static io.fabric.sdk.android.Fabric.TAG;

public  class TrashFragment extends Fragment implements TrashFragmentViewInterface,SearchView.OnQueryTextListener{
    private NotesDataBaseHandler notesDataBaseHandler;
    private NotesModel notesModel;
    RecyclerAdapter recyclerAdapter;
    List<NotesModel> models,allNotes,trashNoteList;
    TrashPresenterInterface presenter;
    private RecyclerView recyclerView;
    NotesFragment notesFragment;
    ToDoMainActivity toDoMainActivity;
    Context context;
    private String userId;
    SharedPreferences userPref;
    SharedPreferences.Editor editor;
    private String TAG = "TrashFragment";



    private ProgressDialog progressDialog;
    private boolean isView;

    private List<NotesModel> searchList;

    public TrashFragment(ToDoMainActivity toDoMainActivity) {
        this.toDoMainActivity=toDoMainActivity;
    }

    public TrashFragment(Context context,ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes) {
        this.toDoMainActivity=toDoMainActivity;
        this.allNotes=allNotes;
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.trashfragment,container,false);
        Fabric.with(getActivity(),new Crashlytics());

        setHasOptionsMenu(true);
        getActivity().setTitle("Trash");
        userPref = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        editor = userPref.edit();

        if (userPref.getBoolean("isList", false)) {
            isView = true;
        } else {
            isView = false;
        }

        notesModel=new NotesModel();
        isView=false;
         progressDialog=new ProgressDialog(getActivity());
        models=new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.trash_recyclerview);
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();

        checkLayout();

        presenter=new TrashPresenter(getActivity(),this);
        presenter.getAllNotelist(userId);
       // recyclerAdapter.setNoteList(trashNoteList);
        recyclerAdapter=new RecyclerAdapter(getActivity());
        recyclerAdapter.setNoteList(models);
        recyclerView.setAdapter(recyclerAdapter);
        initSwipe();
        return view;
    }



    public void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    notesDataBaseHandler = new NotesDataBaseHandler(getApplicationContext());
                    notesModel = models.get(position);
                    notesDataBaseHandler.deleteNote(notesModel);
                    recyclerAdapter.removeItem(position);
                    models.remove(position);
                    presenter.deleteNote(position,notesModel);
                }
                if (direction == ItemTouchHelper.RIGHT) {
                    notesModel = models.get(position);
                    notesModel.setTrash(false);

                    presenter.retriveNote(notesModel);
                    recyclerAdapter.setNoteList(models);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void getNotesSuccess(List<NotesModel> notesModelList) {
        allNotes=notesModelList;
        models=getTrashNotes();

        recyclerAdapter.setNoteList(models);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private List<NotesModel> getTrashNotes() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if(note.isTrash()) {
                notesModels.add(note);
            }
        }
        return notesModels;
    }

    private void checkLayout() {
            if (isView) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            } else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.changeview:
                SharedPreferences userPref=getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
                if (!isView) {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_straggered);
                    SharedPreferences.Editor edit = userPref.edit();
                    edit.putBoolean("isList", true);
                    edit.commit();
                    isView = true;
                } else {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_list);
                    SharedPreferences.Editor edit = userPref.edit();
                    edit.putBoolean("isList", false);
                    edit.commit();
                    isView = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deleteNoteSuccess(String message) {
        //Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

   /* @Override
    public void deleteNoteFailure(String message) {
       // Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
*/
    @Override
    public void retriveNoteSuccess(String message) {
       // Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void retriveNoteFailure(String message) {
       // Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

   /* @Override
    public void getNotesFailure(String message) {
      //  Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
*/
    @Override
    public void showDialog(String message) {
       // if(!getActivity().isFinishing()){
            progressDialog.setMessage(message);
            progressDialog.show();
        //}
    }

    @Override
    public void hideDialog() {
        if ( progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String search) {
        searchList = new ArrayList<>();
        search = search.toLowerCase();
        Log.e(TAG, "onQueryTextChange: " + models.size());
        for (NotesModel model : models) {
            String title = model.getTitle().toLowerCase();
            if (title.contains(search)) {
                searchList.add(model);
            }
        }
        recyclerAdapter.setNoteList(searchList);
        return true;
    }
}
