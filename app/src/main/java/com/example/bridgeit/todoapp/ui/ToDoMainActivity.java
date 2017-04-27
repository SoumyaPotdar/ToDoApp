package com.example.bridgeit.todoapp.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.adapter.RecyclerAdapter;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.model.NotesModel;
import com.example.bridgeit.todoapp.sqlitedatabase.NotesDataBaseHandler;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ToDoMainActivity extends BaseActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    boolean isView = false;
    AppCompatButton userlogoutbutton;
    SessionManagement session;
    FloatingActionButton fabupdate;
    AppCompatEditText editdataedittext;
    RecyclerAdapter recyclerAdapter;
    NotesDataBaseHandler notesDataBaseHandler;
    NotesModel notesModel;
    Toolbar toolbar;
    List<NotesModel> models;
    DrawerLayout drawer;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String uid;
    int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        notesDataBaseHandler=new NotesDataBaseHandler(this);
        models = notesDataBaseHandler.getAllNotes();
        firebaseAuth=FirebaseAuth.getInstance();
        initView();
        List<NotesModel> data = new ArrayList<>();

        setSupportActionBar(toolbar);
        //toolbar.setVisibility(View.GONE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //toolbar.setVisibility(View.GONE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerAdapter= new RecyclerAdapter(getApplicationContext(), models);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });


            Intent intent_get=getIntent();
            String getdata=intent_get.getStringExtra("getdisplaydata");

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    Bundle bund = new Bundle();
                    int position = rv.getChildAdapterPosition(child);
                    bund.putString("title", models.get(position).getTitle());
                    bund.putString("description", models.get(position).getDescription());

                  //  bund.putString("description", "dfsdfsdf");

                    UpdateNoteFragment fre = new UpdateNoteFragment(ToDoMainActivity.this, position);
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
        fabupdate.setOnClickListener(this);
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
      //  getdatatextview = (AppCompatTextView) findViewById(R.id.mytextView);
        userlogoutbutton = (AppCompatButton) findViewById(R.id.logoutbutton);

        session = new SessionManagement(this);
        fabupdate = (FloatingActionButton) findViewById(R.id.nav_fab);

        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        editdataedittext=(AppCompatEditText)findViewById(R.id.fragmentdiscriptionedittext);
        initSwipe();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.changeview:
                if (!isView) {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,1));

                    isView = true;
                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    isView = false;
                }
                return true;

            case R.id.logoutbutton:
                session.logout();
                LoginManager.getInstance().logOut();
                firebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ToDoMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public  void setBackData(NotesModel model){
        recyclerAdapter.addNote(model);
        recyclerView.setAdapter(recyclerAdapter);
        //Toast.makeText(this, ""+str, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_fab:
                AddNoteFragment fa= new AddNoteFragment(this);
                getFragmentManager().beginTransaction().replace(R.id.fragment,fa).addToBackStack(null).commit();
                break;
           /* case R.id.myCardView:
                ResultActivity resultActivity=new ResultActivity();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,resultActivity).commit();
                String st=notesModel.getTitle();
                NotesModel note=notesDataBaseHandler.getNotes(st);
               // int itemPosition = recyclerAdapter.getAdapterPosition(v);
                break;*/
            default:
                break;
        }




    }

 /*   ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,  ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //awesome code when user grabs recycler card to reorder
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //awesome code to run when user drops card and completes reorder
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.RIGHT) {
            }

            if (direction == ItemTouchHelper.LEFT) {
            }

        }
    };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    //itemTouchHelper.attachToRecyclerView(recycler);
}
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     //   Log.i("check", "onActivityResult:ggmngm ");

            if(resultCode==2){

                String str=data.getStringExtra("getdisplaydata");
               // Log.i("ghfghf", "onActivityResult: "+str);
              //  recyclerAdapter.addNote();
               // recyclerView.setAdapter(recyclerAdapter);
            }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    /*recyclerAdapter.removeItem(position);
                    notesDataBaseHandler.deleteNote(models.get(position));*/
                  databaseReference=FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("userdata").child(uid).child("fasdsda").child(String.valueOf(index)).removeValue();
                } else {

                    int edit_position = position;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    public void updateRecycler() {
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_notes:

                Fragment fragment=new NoteFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();
                drawer.closeDrawers();
                break;
        }
        return true;

    }
}