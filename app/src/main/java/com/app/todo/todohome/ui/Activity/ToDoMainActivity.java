package com.app.todo.todohome.ui.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.todo.todohome.presenter.TodoMainPresenter;
import com.app.todo.todohome.presenter.TodoMainPresenterInterface;
import com.app.todo.todohome.ui.Fragment.AboutFragment;
import com.app.todo.todohome.ui.Fragment.archivednotes.ui.ArchiveFragment;
import com.app.todo.todohome.ui.Fragment.notes.ui.NotesFragment;
import com.app.todo.todohome.ui.Fragment.remindernotes.ui.ReminderFragment;
import com.app.todo.todohome.ui.Fragment.TrashFragment;
import com.app.todo.todohome.ui.Fragment.updatenotes.ui.UpdateNoteFragment;
import com.app.todo.todohome.ui.downloadimage.DownloadImage;
import com.app.todo.todohome.ui.downloadimage.DownloadImageInterface;
import com.app.todo.todohome.ui.downloadimage.Utility;
import com.app.todo.todohome.ui.Fragment.addnotes.ui.AddNoteFragment;
import com.app.todo.login.ui.LoginActivity;
import com.app.todo.utils.Constants;
import com.bumptech.glide.Glide;
import com.example.bridgeit.todoapp.R;
import com.app.todo.adapter.RecyclerAdapter;
import com.app.todo.baseclass.BaseActivity;
import com.app.todo.model.NotesModel;
import com.app.todo.sqlitedatabase.NotesDataBaseHandler;
import com.app.todo.utils.SessionManagement;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToDoMainActivity extends BaseActivity implements TodoMainActivityInterface,ColorPickerDialogListener {
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
    CircleImageView circleImageView;
    List<NotesModel> models, allNotes;
    Utility utility;
    boolean isNewNoteFragment=false;
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
    private Uri mPrfilefilePath;
    private SharedPreferences.Editor editor;
    private int SELECT_PHOTO=3;
    private NotesFragment notesFragment;
    private AddNoteFragment addNoteFragment;
    private UpdateNoteFragment updateNoteFragment;
    private ArchiveFragment archiveFragment;
    private ReminderFragment reminderFragment;
    private ToDoMainActivity toDoMainActivity;
    private LinearLayout addnotelayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        notesFragment = new NotesFragment(this, allNotes);
        getFragmentManager().beginTransaction().replace(R.id.fragment, notesFragment).commit();
        // notesDataBaseHandler=new NotesDataBaseHandler(this);

        /*getGoogleData();*/
        userPref = getSharedPreferences(Constants.key_pref, Context.MODE_PRIVATE);
        editor=userPref.edit();
        initView();
        if(userPref.getBoolean("isList",false)){
            isView = false;
        }
        else{
            isView = true;
        }

        presenter.getNoteList(FirebaseAuth.getInstance().getCurrentUser().getUid());
        models = recyclerAdapter.getAllDataList();

        if (session.isFbLogin()){
            String profile = session.getUserDetails().getMobileNo();
            Glide.with(this).load(profile).into(circleImageView);
            navHeaderEmail.setText(session.getUserDetails().getEmail());
            navHeaderName.setText(session.getUserDetails().getFullname());

        }else if (session.isGoogleLogin()){
            String profile = session.getUserDetails().getMobileNo();
            Glide.with(this).load(profile).into(circleImageView);
            navHeaderName.setText(session.getUserDetails().getFullname());
            navHeaderEmail.setText(session.getUserDetails().getEmail());
        }else {
            navHeaderName.setText(session.getUserDetails().getFullname());
            navHeaderEmail.setText(session.getUserDetails().getEmail());
            String email=navHeaderEmail.getText().toString();

            if (userPref.contains(Constants.BundleKey.USER_PROFILE_SERVER) && userPref.getString(Constants.BundleKey.USER_PROFILE_SERVER, getString(R.string.flag_false)).equals(getString(R.string.flag_true))) {
                circleImageView.setOnClickListener(this);
                DownloadImage.downloadImage(String.valueOf("myProfiles/" + email.substring(0, email.indexOf("@")) + ".jpg"), new DownloadImageInterface() {
                    @Override
                    public void getImage(Bitmap bitmap) {
                        circleImageView.setImageBitmap(bitmap);
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                        circleImageView.setImageBitmap(resized);
                    }
                });
            }
        }
    }

    @Override
    public void initView() {

        presenter = new TodoMainPresenter(this, this);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        editor.putString(Constants.keyUserId,uid);
        editor.commit();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        addnotelayout= (LinearLayout)findViewById(R.id.addnotefragment);
        updateNoteFragment=new UpdateNoteFragment();

        userlogoutbutton = (AppCompatButton) findViewById(R.id.logoutbutton);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navHeaderName = (AppCompatTextView) header.findViewById(R.id.nav_header_name);
        navHeaderEmail = (AppCompatTextView) header.findViewById(R.id.nav_header_email);
        format = new SimpleDateFormat("MMMM dd,yyyy");
        currentDate = format.format(new Date().getTime());
        databaseReference=FirebaseDatabase.getInstance().getReference();
        circleImageView= (CircleImageView) header.findViewById(R.id.nav_header_imageview);
        utility=new Utility(this);
        circleImageView.setOnClickListener(this);
        session = new SessionManagement(this);

        addNoteFab = (FloatingActionButton) findViewById(R.id.nav_fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        editdataedittext = (AppCompatEditText) findViewById(R.id.fragmentdiscriptionedittext);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //toolbar.setVisibility(View.GONE);
        addNoteFab.setOnClickListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        checkLayout();
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_fab:
                isNewNoteFragment=true;
                addNoteFragment = new AddNoteFragment(this);
                getFragmentManager().beginTransaction().replace(R.id.fragment, addNoteFragment).addToBackStack(null).commit();
                break;

            case R.id.nav_header_imageview:
                Intent picker = new Intent();
                picker.setType("image/*");
                picker.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(picker, String.valueOf(R.string.select_pick)), SELECT_PHOTO);
              break;

            default:
                break;
        }
    }

    public void showOrHideFab(boolean show){
        if(show){
            addNoteFab.setVisibility(View.VISIBLE);
        }else {
            addNoteFab.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   Log.i("check", "onActivityResult:ggmngm ");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            String str = data.getStringExtra("getdisplaydata");
        }

        if(requestCode==SELECT_PHOTO){
            {
                // Get the url from data
                if (data != null) {
                    mPrfilefilePath = data.getData();
                    if (null != mPrfilefilePath) {
                        cropCapturedImage(mPrfilefilePath);
                        editor.putString(Constants.BundleKey.USER_PROFILE_LOCAL, String.valueOf(mPrfilefilePath));
                        editor.putString(Constants.BundleKey.USER_PROFILE_SERVER, getString(R.string.flag_true));
                        editor.commit();
                    }
                }
            }
        }

        if (requestCode == 6) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap cropedPic = extras.getParcelable("data");
                Bitmap scaled = Bitmap.createScaledBitmap(cropedPic, 100, 100, true);
                circleImageView.setImageBitmap(scaled);

                utility.uploadFile(cropedPic,session.getUserDetails().getEmail());
            }
        }
    }

    private void cropCapturedImage(Uri mPrfilefilePath) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(mPrfilefilePath, "image/*");
        cropIntent.putExtra("crop", getString(R.string.flag_true));
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 6);
    }


    public void updateRecycler(NotesModel notesModel) {
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_notes:
               NotesFragment notesFragment=new NotesFragment(this,allNotes);
                getFragmentManager().beginTransaction().replace(R.id.fragment,notesFragment).commit();
                break;

            case R.id.nav_archieve:
                ArchiveFragment archiveFragment=new ArchiveFragment(this, allNotes);
                getFragmentManager().beginTransaction().replace(R.id.fragment,archiveFragment).commit();
                break;

            case R.id.nav_reminders:
                ReminderFragment reminderFragment=new ReminderFragment(this,allNotes);
                getFragmentManager().beginTransaction().replace(R.id.fragment,reminderFragment).commit();
                break;

            case R.id.nav_trash:
                TrashFragment trashFragment= new TrashFragment(this,allNotes);
                getFragmentManager().beginTransaction().replace(R.id.fragment,trashFragment).commit();

                break;
            case R.id.nav_about:
                getFragmentManager().beginTransaction().replace(R.id.fragment,new AboutFragment(this,allNotes)).commit();

                break;
            case R.id.logoutbutton:

                session.logout();
                LoginManager.getInstance().logOut();
                firebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ToDoMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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
    public void getNotesSuccess(List<NotesModel> notesModelList) {
            List<NotesModel> nonArchievedList = new ArrayList<>();
            allNotes = notesModelList;
            for (NotesModel model :
                    notesModelList) {
                if (!model.isArchieve())
                    nonArchievedList.add(model);
        }
        if(getTitle().equals("Notes")) {
             notesFragment = new NotesFragment(this, allNotes);
            getFragmentManager().beginTransaction().replace(R.id.fragment, notesFragment).commit();
        }else if(getTitle().equals("Archieve")){
             archiveFragment=new ArchiveFragment(this, allNotes);
            getFragmentManager().beginTransaction().replace(R.id.fragment,archiveFragment).commit();
        }else  if(getTitle().equals("Reminder")){
             reminderFragment=new ReminderFragment(this,allNotes);
            getFragmentManager().beginTransaction().replace(R.id.fragment,reminderFragment).commit();
        }else if(getTitle().equals("Trash")){

        }
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
        if (!isFinishing() && progressDialog!= null){
            progressDialog.dismiss();
        }
    }

    public Intent getPickImageChooserIntent() {

//  Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

// collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new  ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new  File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    @Override
    public void onColorSelected(int dialogId, @ColorInt int color) {
        if (dialogId == 0) {
            if(isNewNoteFragment){

                addNoteFragment.setBackgroundColor(color);
            }else {

                updateNoteFragment.setBackgroundColor(color);
            }
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}