package com.app.todo.todohome.ui.Fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
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
import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.model.NotesModel;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.todohome.presenter.TodoMainPresenterInterface;
import com.app.todo.todohome.ui.Activity.OnSearchTextChanged;
import com.app.todo.todohome.ui.Activity.ToDoMainActivity;
import com.app.todo.todohome.ui.Fragment.addnotes.ui.AddNoteFragment;
import com.app.todo.utils.Constants;
import com.example.bridgeit.todoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotesFragment extends Fragment implements OnSearchTextChanged,View.OnClickListener {
    TodoMainPresenterInterface presenter;
    List<NotesModel> models;
    List<NotesModel> allNotes;
    ToDoMainActivity toDoMainActivity;
    RecyclerView recyclerView;
    private boolean isView=false;
    private RecyclerAdapter recyclerAdapter;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    DatabaseReference databaseReference;
    String uid;
    FloatingActionButton addNoteFab;

    private static final String TAG = "NotesFragment";
    private SharedPreferences userPref;

    public NotesFragment(ToDoMainActivity toDoMainActivity, List<NotesModel> allNotes) {
        this.toDoMainActivity = toDoMainActivity;
        this.allNotes = allNotes;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        setHasOptionsMenu(true);
        //   presenter.showDialog("Loading...");
        recyclerView = (RecyclerView) view.findViewById(R.id.notes_recyclerview);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userPref=getActivity().getSharedPreferences(Constants.key_pref,Context.MODE_PRIVATE);
        isView=userPref.getBoolean("isList",false);
        models = getWithoutArchive();
        checkLayout();
        addNoteFab = (FloatingActionButton) view.findViewById(R.id.nav_fab);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerAdapter = new RecyclerAdapter(getActivity().getBaseContext());
        recyclerAdapter.setNoteList(models);
        recyclerView.setAdapter(recyclerAdapter);
        initSwipe();
        getActivity().setTitle("Notes");
        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteFragment addNoteFragment = new AddNoteFragment((ToDoMainActivity) getActivity());
                getFragmentManager().beginTransaction().replace(R.id.fragment, addNoteFragment).addToBackStack(null).commit();
            }
        });      //  presenter.hideDialog();
        return view;
    }

    private List<NotesModel> getWithoutArchive() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        if (allNotes != null) {
            for (NotesModel note : allNotes) {
                if (!note.isArchieve()) {
                    notesModels.add(note);
                }
            }
        }
        return notesModels;
    }

    private void checkLayout() {
        if (isView) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ToDoMainActivity) getActivity()).showOrHideFab(true);
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
                  /*  SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                    String getdate = dateFormat.format(new Date().getTime());*/
                    databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                            .child(String.valueOf(notesModel.getId())).removeValue();
                }
                if (direction == ItemTouchHelper.RIGHT) {
                    notesModel = models.get(position);
                    notesModel.setArchieve(true);
                    databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                            .child(String.valueOf(notesModel.getId())).setValue(notesModel);

                    /*Snackbar snackbar = Snackbar.make(getActivity().getCurrentFocus(), getString(R.string.note_Archieved), Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    notesModel.setArchieve(false);
                                    databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                                            .child(String.valueOf(notesModel.getId())).setValue(notesModel);
                                }
                            });
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();*/
                    Snackbar snackbar = Snackbar.make(getActivity().getCurrentFocus(), getString(R.string.note_Archieved), Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    notesModel.setArchieve(false);

                                    Log.i(TAG, "snackbar onClick: " + notesModel.getDescription() + "\n " + notesModel.getTitle());

                                    databaseReference.child("userdata").child(uid).child(notesModel.getNoteDate())
                                            .child(String.valueOf(notesModel.getId())).setValue(notesModel);
//                                   models.add(position,notesModel);
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
    public void onSearchTextChange(String search) {
        search = search.toLowerCase();
        ArrayList<NotesModel> newList = new ArrayList<>();
        for (NotesModel model : models) {
            String name = model.getTitle().toLowerCase();
            if (name.contains(search)) {
                newList.add(model);
            }
        }
        recyclerAdapter.searchNotes(newList);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        //this.menu = menu;

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        //   searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.changeview:
                if (isView) {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_straggered);
                    SharedPreferences.Editor edit = userPref.edit();
                    edit.putBoolean("isList",true);
                    edit.commit();
                    isView = true;
                } else {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_list);
                    SharedPreferences.Editor edit = userPref.edit();
                    edit.putBoolean("isList",false);
                    edit.commit();
                    isView = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
