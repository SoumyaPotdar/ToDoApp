package com.example.bridgeit.todoapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bridgeit.todoapp.R;
import com.example.bridgeit.todoapp.adapter.RecyclerAdapter;
import com.example.bridgeit.todoapp.baseclass.BaseActivity;
import com.example.bridgeit.todoapp.utils.SessionManagement;
import java.util.ArrayList;
import java.util.List;

public class ToDoMainActivity extends BaseActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    AppCompatTextView getdatatextview;
    boolean isView = false;
    AppCompatButton userlogoutbutton;
    SessionManagement session;
    FloatingActionButton fabupdate;
    AppCompatEditText editdataedittext;
    RecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        initview();

        List<String> data = new ArrayList<>();
     /*   data.add("Welcome to BridgeLabz ");
        data.add(" Fragments");
        data.add("Layouts");
        data.add("Views");
*/

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerAdapter= new RecyclerAdapter(getApplicationContext(), data);
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
                    bund.putString("texts", "dfsdfsdf");
                    int position = rv.getChildAdapterPosition(child);

                    FragmentActivity1 fre = new FragmentActivity1();
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

    }

    @Override
    public void initview() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        getdatatextview = (AppCompatTextView) findViewById(R.id.mytextView);
        userlogoutbutton = (AppCompatButton) findViewById(R.id.logoutbutton);
        session = new SessionManagement(this);
        fabupdate = (FloatingActionButton) findViewById(R.id.nav_fab);
        fabupdate.setOnClickListener(this);
        editdataedittext=(AppCompatEditText)findViewById(R.id.fragmentdataedittext);
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
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

                    isView = true;
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

                    isView = false;
                }
                return true;

            case R.id.logoutbutton:
                session.logout();
                Intent intent = new Intent(ToDoMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public  void setBackData(String str){
        recyclerAdapter.addNote(str);
        recyclerView.setAdapter(recyclerAdapter);
        //Toast.makeText(this, ""+str, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_fab:
                FragmentActivity1 fa= new FragmentActivity1(this);
                getFragmentManager().beginTransaction().replace(R.id.fragment,fa).addToBackStack(null).commit();
                break;
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
                recyclerAdapter.addNote(str);
                recyclerView.setAdapter(recyclerAdapter);
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
                    recyclerAdapter.removeItem(position);
                } else {

                    int edit_position = position;
                }
            }
/*
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
            {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
                {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint p = new Paint();
                    if(dX > 0)
                    {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        //    icon = BitmapFactory.decodeResource(getResources(), R.drawable.);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        //      c.drawBitmap(icon,null,icon_dest,p);
                    } else
                    {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        // icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        // c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }*/
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


}