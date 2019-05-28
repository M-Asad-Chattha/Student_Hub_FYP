package com.achba.studenthub;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    Spinner spinnerProgram, spinnerSemester, spinnerSection, spinnerCampus;
    RelativeLayout layout;
    private EditText mName, mBio;
    private CircularImageView profileImg;

    private FirebaseUser firebaseUser;
    private Uri imgUri;
    StorageTask uploadTask;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerProgram = findViewById(R.id.spinnerProgram);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        spinnerSection = findViewById(R.id.spinnerSection);
        spinnerCampus = findViewById(R.id.spinnerCampus);
        profileImg=findViewById(R.id.profileImg_edit);
        mName = findViewById(R.id.name);
        mBio= findViewById(R.id.bio);
        layout = findViewById(R.id.layout);


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageRef= FirebaseStorage.getInstance().getReference("uploads");

        ArrayAdapter<CharSequence> adapterProgram = ArrayAdapter.createFromResource(this,
                R.array.spinnerProgram, android.R.layout.simple_spinner_item);
        adapterProgram.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProgram.setAdapter(adapterProgram);

        ArrayAdapter<CharSequence> adapterSemester = ArrayAdapter.createFromResource(this,
                R.array.spinnerSemester, android.R.layout.simple_spinner_item);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(adapterSemester);

        ArrayAdapter<CharSequence> adapterSection = ArrayAdapter.createFromResource(this,
                R.array.spinnerSection, android.R.layout.simple_spinner_item);
        adapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSection.setAdapter(adapterSection);

        ArrayAdapter<CharSequence> adapterCampus = ArrayAdapter.createFromResource(this,
                R.array.spinnerCampus, android.R.layout.simple_spinner_item);
        adapterCampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampus.setAdapter(adapterCampus);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Please Wait...");
        progress.setMessage("Fetching User Data...");
        progress.setCancelable(false);
        progress.show();

        DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);
                    String profileImageUrl=dataSnapshot.child("profileImageUrl").getValue(String.class);
                    imgUri=Uri.parse(profileImageUrl);

                    mName.setText(name);
                    mBio.setText(bio);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.profileimg_placeholder);
                    requestOptions.error(R.drawable.profileimg_placeholder);
                    Glide.with(EditProfileActivity.this)
                            .setDefaultRequestOptions(requestOptions)
                            .load(imgUri)
                            .into(profileImg);

                    progress.dismiss();
                }else{
                    progress.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Data retrieve Problem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void attemptSave(){
        boolean cancel = false;
        String spinnerProgramValue = spinnerProgram.getSelectedItem().toString();
        String spinnerSemesterValue = spinnerSemester.getSelectedItem().toString();
        String spinnerSectionValue = spinnerSection.getSelectedItem().toString();
        String spinnerCampusValue = spinnerCampus.getSelectedItem().toString();
        String name = mName.getText().toString();
        String bio = mBio.getText().toString();

        if (spinnerProgramValue.equals("Select Degree Program:")) {
            ((TextView)spinnerProgram.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerSemesterValue.equals("Select Semester:")){
            ((TextView)spinnerSemester.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerSectionValue.equals("Select Section:")){
            ((TextView)spinnerSection.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }
        if (spinnerCampusValue.equals("Select Campus:")){
            ((TextView)spinnerCampus.getSelectedView()).setError("Select one from drop down");
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (TextUtils.isEmpty(bio)) {
            mBio.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (cancel) {
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            updateProfile();
        }
    }

    private void updateProfile() {
        ProgressDialog progress = new ProgressDialog(EditProfileActivity.this);
        progress.setTitle("Please Wait...");
        progress.setMessage("Processing your request...");
        progress.setCancelable(false);
        progress.show();

        String spinnerProgramValue = spinnerProgram.getSelectedItem().toString();
        String spinnerSemesterValue = spinnerSemester.getSelectedItem().toString();
        String spinnerSectionValue = spinnerSection.getSelectedItem().toString();
        String spinnerCampusValue = spinnerCampus.getSelectedItem().toString();
        String name = mName.getText().toString();
        String bio = mBio.getText().toString();

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("bio", bio);
//        hashMap.put("email", email);
//        hashMap.put("userName", userName);
        hashMap.put("program", spinnerProgramValue);
        hashMap.put("semester", spinnerSemesterValue);
        hashMap.put("section", spinnerSectionValue);
        hashMap.put("campus", spinnerCampusValue);
        userDB.updateChildren(hashMap);

        progress.dismiss();
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imgUri !=null){
            final StorageReference fileRef=storageRef.child(System.currentTimeMillis()
                    +"."+ getFileExtension(imgUri));
            uploadTask=fileRef.putFile(imgUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String myUri=downloadUri.toString();

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap=new HashMap<>();
                        hashMap.put("profileImageUrl", myUri);
                        reference.updateChildren(hashMap);
                        progressDialog.dismiss();

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.profileimg_placeholder);
                        requestOptions.error(R.drawable.profileimg_placeholder);
                        Glide.with(EditProfileActivity.this)
                                .setDefaultRequestOptions(requestOptions)
                                .load(imgUri)
                                .into(profileImg);
                    }else {
                        Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("URL", "resultBefore: "+imgUri.toString());
        Log.i("Code", "requestCode: "+requestCode+" resultCode: "+resultCode);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();
            uploadImage();
        }else{
            Toast.makeText(this, "Something wents wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onClickSave(View view) {
        hideKeyboard();
        attemptSave();
    }

    public void hideKeyboard(View view){
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onClickProfileImg(View view) {
        Log.i("URL", imgUri.toString());
        CropImage.activity()
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }

}
