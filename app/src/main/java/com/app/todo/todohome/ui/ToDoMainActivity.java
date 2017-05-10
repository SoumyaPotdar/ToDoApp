package com.app.todo.todohome.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.todo.todohome.interactor.TodoMainInteractor;
import com.app.todo.todohome.presenter.TodoMainPresenter;
import com.app.todo.todohome.presenter.TodoMainPresenterInterface;
import com.app.todo.ui.AddNoteFragment;
import com.app.todo.ui.LoginActivity;
import com.app.todo.ui.UpdateNoteFragment;
import com.bumptech.glide.Glide;
import com.example.bridgeit.todoapp.R;
import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.baseclass.BaseActivity;
import com.app.todo.model.NotesModel;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.utils.Constants;
import com.app.todo.utils.SessionManagement;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoMainActivity extends BaseActivity implements TodoMainActivityInterface {
    RecyclerView recyclerView;
    boolean isView = false;
    AppCompatButton userlogoutbutton;
    SessionManagement session;
    FloatingActionButton addNoteFab;
    AppCompatEditText editdataedittext;
    public RecyclerAdapter recyclerAdapter;
    NotesDataBaseHandler notesDataBaseHandler;
    SharedPreferences userPref;
    Toolbar toolbar;
    List<NotesModel> models, allNotes;

    /*List<NotesModel>  listMotelDataAll,allNotes,mArchivedNotes,mReminderNotes;*/

    DrawerLayout drawer;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    FirebaseDatabase firebaseDatabase;
    int index;
    AppCompatTextView navHeaderName, navHeaderEmail;
    AppCompatImageView navHeaderImage;
    String gFirstname, gEmail, gImageUrl;
    String fbFirstname, fbLastname, fbEmail, fbImageUrl;
    NavigationView navigationView;
    SimpleDateFormat format;
    String currentDate;
    Bundle bund;
    NotesModel notesModel;
    Menu menu;


    TodoMainPresenterInterface presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        // notesDataBaseHandler=new NotesDataBaseHandler(this);
        initView();
        /*getGoogleData();*/

        presenter.getNoteList(FirebaseAuth.getInstance().getCurrentUser().getUid());
        models = recyclerAdapter.getAllDataList();

        if (session.isFbLogin()){
            String profile = session.getUserDetails().getMobileNo();
            Glide.with(this).load(profile).into(navHeaderImage);
            navHeaderEmail.setText(session.getUserDetails().getEmail());
            navHeaderName.setText(session.getUserDetails().getFullname());
        }else if (session.isGoogleLogin()){
            String profile = session.getUserDetails().getMobileNo();
            Glide.with(this).load(profile).into(navHeaderImage);
            navHeaderName.setText(session.getUserDetails().getFullname());
            navHeaderEmail.setText(session.getUserDetails().getEmail());
        }else {
            navHeaderName.setText(session.getUserDetails().getFullname());
            navHeaderEmail.setText(session.getUserDetails().getEmail());
        }

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(ToDoMainActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    bund = new Bundle();
                    int position = rv.getChildAdapterPosition(child);
                    bund.putInt("id", models.get(position).getId());
                    bund.putString("currentDate", models.get(position).getNoteDate());
                    bund.putString("title", models.get(position).getTitle());
                    bund.putString("description", models.get(position).getDescription());
                    bund.putString("reminddate", models.get(position).getReminderDate());
                    bund.putBoolean("archieve", models.get(position).isArchieve());
/*
                    bund.putString("Archieve",models.get(position),);
*/

                    //  bund.putString("description", "dfsdfsdf");
                    UpdateNoteFragment fre = new UpdateNoteFragment();
                    fre.setArguments(bund);
                    Log.i("fghf", "onInterceptTouchEvent: ");
                    getFragmentManager().beginTransaction().replace(R.id.fragment, fre).addToBackStack(null).commit();

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        addNoteFab.setOnClickListener(this);
    }

    @Override
    public void initView() {

        presenter = new TodoMainPresenter(this, this);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        userlogoutbutton = (AppCompatButton) findViewById(R.id.logoutbutton);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navHeaderName = (AppCompatTextView) header.findViewById(R.id.nav_header_name);
        navHeaderEmail = (AppCompatTextView) header.findViewById(R.id.nav_header_email);
        navHeaderImage = (AppCompatImageView) header.findViewById(R.id.nav_header_imageview);
        format = new SimpleDateFormat("MMMM dd,yyyy");
        currentDate = format.format(new Date().getTime());
        databaseReference=FirebaseDatabase.getInstance().getReference();

        /*if (userPref.contains("whichuser")) {
            if (userPref.getString("whichuser", "null").equals("facebook")) {
                getFBData();
            } else if (userPref.getString("whichuser", "null").equals("google")) {
            } else if (userPref.getString("whichuser", "null").equals("google")) {

                getGoogleplusData();
            } else if (userPref.getString("whichuser", "null").equals("firebase")) {
                getFirebaseData();
            }

        }*/

        session = new SessionManagement(this);

        addNoteFab = (FloatingActionButton) findViewById(R.id.nav_fab);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        editdataedittext = (AppCompatEditText) findViewById(R.id.fragmentdiscriptionedittext);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setVisibility(View.GONE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //toolbar.setVisibility(View.GONE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        checkLayout();
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);
        initSwipe();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:
                return true;

            case R.id.changeview:
                if (!isView) {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_straggered);
                    isView = true;
                } else {

                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    item.setIcon(R.drawable.ic_action_list);
                    isView = false;
                }
                return true;

            case R.id.logoutbutton:

               // session.logout();
                LoginManager.getInstance().logOut();
                firebaseAuth.getInstance().signOut();

                session.logout();

                Intent intent = new Intent(ToDoMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void getGoogleplusData() {
        gFirstname = userPref.getString("name", "value");
        gEmail = userPref.getString("email", "value");
        gImageUrl = userPref.getString("imageUrl", "value");
        Glide.with(getApplicationContext()).load(gImageUrl).into(navHeaderImage);
        setProfile();
    }

    public void getFBData() {
        gFirstname = userPref.getString(Constants.fb_firstName, "value");
        fbLastname = userPref.getString(Constants.fb_lastName, "value");
        gEmail = userPref.getString(Constants.userEmail, "value");
        fbImageUrl = userPref.getString(Constants.profilePic, "value");
        Glide.with(getApplicationContext()).load(fbImageUrl).into(navHeaderImage);
        setProfile();
    }*/


    private void setNotesToRecycler(ArrayList<NotesModel> notesModels) {
        allNotes = notesModels;
        models = getWithoutArchive();
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

    private List<NotesModel> getWithoutArchive() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if (!note.isArchieve()) {
                notesModels.add(note);
            }
        }
        return notesModels;
    }

    private List<NotesModel> getArchiveNotes() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if (note.isArchieve()) {
                notesModels.add(note);
            }
        }
        return notesModels;
    }

    private List<NotesModel> getReminderNotes() {
        ArrayList<NotesModel> notesModels = new ArrayList<>();
        for (NotesModel note : allNotes) {
            if (note.getReminderDate().equals(currentDate)&& !note.isArchieve()) {
                notesModels.add(note);
            }
        }
        return notesModels;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_fab:
                AddNoteFragment addNoteFragment = new AddNoteFragment(this);
                getFragmentManager().beginTransaction().replace(R.id.fragment, addNoteFragment).addToBackStack(null).commit();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   Log.i("check", "onActivityResult:ggmngm ");

        if (resultCode == 2) {

            String str = data.getStringExtra("getdisplaydata");
            // Log.i("ghfghf", "onActivityResult: "+str);
            //  recyclerAdapter.addNote();
            // recyclerView.setAdapter(recyclerAdapter);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

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

                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), getString(R.string.note_Archieved), Snackbar.LENGTH_LONG)
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
                    snackbar.show();

                }
            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    public void updateRecycler(NotesModel notesModel) {
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_notes:
                presenter.showDialog("Loading...");
                models = getWithoutArchive();
                checkLayout();
                recyclerAdapter = new RecyclerAdapter(this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.setNoteList(models);
                presenter.hideDialog();
                /*recyclerAdapter.notifyDataSetChanged();*/
                break;

            case R.id.nav_archieve:
                presenter.showDialog("Loading...");
                models = getArchiveNotes();
                checkLayout();
                recyclerAdapter = new RecyclerAdapter(this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.setNoteList(models);
                presenter.hideDialog();
                break;

            case R.id.nav_reminders:
                presenter.showDialog("Loading...");
                models = getReminderNotes();
                checkLayout();
                recyclerAdapter = new RecyclerAdapter(this);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.setNoteList(models);
                presenter.hideDialog();
                break;
        }
        drawer.closeDrawers();
        return true;

    }

    private void checkLayout() {
        if (isView) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        Log.i("search", "onQueryTextChange: 1 " + newText);
        ArrayList<NotesModel> newList = new ArrayList<>();
        for (NotesModel model : models) {
            String name = model.getTitle().toLowerCase();
            if (name.contains(newText)) {
                newList.add(model);
                Log.i("search", "onQueryTextChange: 2 " + newText + "  " + model.getTitle().toLowerCase());
            }
        }
        Log.i("search", "onQueryTextChange: 3 " + newList.size());

        recyclerAdapter.searchNotes(newList);
        return true;
    }

    @Override
    public void getNotesSuccess(List<NotesModel> notesModelList) {
        List<NotesModel> nonArchievedList = new ArrayList<>();
        allNotes = notesModelList;
        for (NotesModel model :
                notesModelList) {
             if (!model.isArchieve())
                nonArchievedList.add(model);
        }

        recyclerAdapter.setNoteList(nonArchievedList);
    }

    @Override
    public void getNotesFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    ProgressDialog progressDialog;

    @Override
    public void showDialog(String message) {
        progressDialog = new ProgressDialog(this);
        if(!isFinishing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (!isFinishing() && progressDialog != null){
            progressDialog.dismiss();
        }
    }
}