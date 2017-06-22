package com.app.todo.todohome.ui.Fragment.notes.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.todohome.ui.Activity.DrawerLocker;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.addnotes.ui.AddNoteActivity;
import com.app.todo.todohome.ui.Fragment.addnotes.ui.AddNoteFragment;
import com.app.todo.todohome.ui.Fragment.notes.presenter.NotesPresenter;
import com.app.todo.todohome.ui.Fragment.notes.presenter.NotesPresenterInterface;
import com.app.todo.todohome.ui.Fragment.trashnotes.presenter.TrashPresenter;
import com.app.todo.todohome.ui.Fragment.trashnotes.presenter.TrashPresenterInterface;
import com.app.todo.todohome.ui.Fragment.trashnotes.ui.TrashFragment;
import com.app.todo.todohome.ui.Fragment.trashnotes.ui.TrashFragmentViewInterface;
import com.app.todo.utils.Constants;
import com.crashlytics.android.Crashlytics;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotesFragment extends Fragment implements NotesViewInterface, SearchView.OnQueryTextListener, View.OnClickListener {
    private static final String TAG = "NotesFragment";
    private  Context mContext;
    List<NotesModel> models;
    List<NotesModel> allNotes = new ArrayList<>();
    ToDoMainActivity toDoMainActivity;
    RecyclerView recyclerView;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    String uid;
    FloatingActionButton addNoteFab;
    NotesPresenterInterface presenter;
    List<NotesModel> searchList;
    private boolean isView = false;
    private RecyclerAdapter recyclerAdapter;
    private SharedPreferences userPref;
    TrashFragment trashFragment;
    private ArrayList<NotesModel> trashNoteList;


    public NotesFragment(Context context,ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes) {
        this.toDoMainActivity = toDoMainActivity;
        this.allNotes = allNotes;
        this.mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        Fabric.with(getActivity(),new Crashlytics());

        setHasOptionsMenu(true);
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.notes_recyclerview);
        trashNoteList=new ArrayList<>();
        presenter = new NotesPresenter(getActivity(), this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userPref = getActivity().getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        isView = userPref.getBoolean("isList", false);
        models = getWithoutArchive();
        checkLayout();
        addNoteFab = (FloatingActionButton) view.findViewById(R.id.nav_fab);

        recyclerAdapter = new RecyclerAdapter(getActivity(),this);
        recyclerAdapter.setNoteList(models);
        recyclerView.setAdapter(recyclerAdapter);
        initSwipe();
        getActivity().setTitle("Notes");
        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AddNoteFragment addNoteFragment = new AddNoteFragment(mContext,(ToDoMainActivity) getActivity());
                getFragmentManager().beginTransaction().replace(R.id.fragment, addNoteFragment).addToBackStack(null).commit();
 */              Intent intent=new Intent(getActivity(),AddNoteActivity.class);
                startActivity(intent);


            }
        });
        return view;
    }

    private List<NotesModel> getWithoutArchive() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        if (allNotes != null) {
            for (NotesModel note : allNotes) {
                if (!note.isArchieve()&&!note.isTrash()) {
                        notesModels.add(note);
                    }

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ToDoMainActivity) getActivity()).showOrHideFab(true);
    }

    public void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN|ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                recyclerAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    notesDataBaseHandler = new NotesDataBaseHandler(getApplicationContext());
                    notesModel = recyclerAdapter.getNoteModel(position);
                    notesModel.setTrash(true);
                    presenter.moveToTrash(notesModel);
                }
                if (direction == ItemTouchHelper.RIGHT) {
                    notesModel = recyclerAdapter.getNoteModel(position);
                    notesModel.setArchieve(true);
                    presenter.archiveNote(notesModel);
                    Snackbar snackbar = Snackbar.make(getActivity().getCurrentFocus(), getString(R.string.note_Archieved), Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    notesModel.setArchieve(false);
                                    Log.i(TAG, "snackbar onClick: " + notesModel.getDescription() + "\n " + notesModel.getTitle());

                                    presenter.undoNote(notesModel);
                                    // models.add(position,notesModel);
                                    recyclerAdapter.setNoteList(models);

                                }
                            });
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.changeview:
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

    @Override
    public void deleteNoteSuccess(String message) {
      //  Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteNoteFailure(String message) {
       // Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void archiveNoteSuccess(String message) {
        //Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void archiveNoteFailure(String message) {
       // Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void undoNoteSuccess(String message) {
        //Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void undoNoteFailure(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToTrashSuccess(String message) {
        //Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToTrashFailure(String message) {
       // Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }
}
