package com.app.todo.todohome.ui.downloadimage;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Utility {
    private  static  String TAG="Utility";
    ProgressDialog mProgressDialog;
    Context mContext;
    public Utility(Context context) {
        this.mContext=context;
        mProgressDialog=new ProgressDialog(mContext);
    }

    public void uploadFile(Bitmap bitmap, String mEmail_id){
        //if there is a file to upload
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "Title", null);

        if (Uri.parse(path) != null) {
            //displaying a progress dialog while upload is going on
            mProgressDialog.setMessage("Uploading");
            StorageReference riversRef = FirebaseStorage.getInstance().getReference();
            StorageReference ref=riversRef.child("myProfiles/"+mEmail_id.substring(0,mEmail_id.indexOf("@"))+".jpg");
            ref.putFile(Uri.parse(path))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //hiding the progress dialog
                            mProgressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //hiding the progress dialog
                            mProgressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            mProgressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Log.i(TAG, "uploadFile: ");  //you can display an error toast
        }
    }
}
