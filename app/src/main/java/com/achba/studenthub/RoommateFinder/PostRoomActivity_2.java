package com.achba.studenthub.RoommateFinder;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.achba.studenthub.EditProfileActivity;
import com.achba.studenthub.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class PostRoomActivity_2 extends AppCompatActivity {
    private Spinner spinnerHome;
    TextView bedRoom, bathRoom;
    int bathRoomValue, bedRoomValue;
    View focusView = null;
    ImageView imageView;
    private Uri imgUri;
    FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference databaseReferenceRoommate;
    StorageTask uploadTask;
    StorageReference storageRef;
    String spinnerHomeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bedRoom = findViewById(R.id.tvBedroom);
        bathRoom = findViewById(R.id.tvBathroom);
        imageView = findViewById(R.id.imageView_2);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        imgUri = Uri.parse("http://diazworld.com/images/avatar-placeholder.png");

        bedRoomValue = Integer.parseInt(bedRoom.getText().toString());
        bathRoomValue = Integer.parseInt(bathRoom.getText().toString());

        spinnerHome = findViewById(R.id.spinnerHome);
        ArrayAdapter<CharSequence> adapterHome = ArrayAdapter.createFromResource(this,
                R.array.spinnerHome, android.R.layout.simple_spinner_item);
        adapterHome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHome.setAdapter(adapterHome);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onClickMinusBedroom(View view) {
        if (bedRoomValue > 1) {
            bedRoomValue--;
            bedRoom.setText(Integer.toString(bedRoomValue));
        } else {
            Toast.makeText(this, "Rooms must be more or equal to 1.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPlusBedroom(View view) {
        bedRoomValue++;
        bedRoom.setText(Integer.toString(bedRoomValue));
    }

    public void onClickMinusBathroom(View view) {
        if (bathRoomValue > 1) {
            bathRoomValue--;
            bathRoom.setText(Integer.toString(bathRoomValue));
        } else {
            Toast.makeText(this, "Rooms must be more or equal to 1.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickPlusBathroom(View view) {
        bathRoomValue++;
        bathRoom.setText(Integer.toString(bathRoomValue));
    }

    public void onClickNext(View view) {
        attemptNext();
    }

    public void onClickIcon(View view) {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
//        imgUri=Uri.parse("http://diazworld.com/images/avatar-placeholder.png");
        final ProgressDialog progressDialog = new ProgressDialog(PostRoomActivity_2.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (imgUri != null) {
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imgUri));
            uploadTask = fileRef.putFile(imgUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String myUri = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Roommate").child(firebaseUser.getUid());

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", myUri);
                        reference.updateChildren(hashMap);
                        progressDialog.dismiss();

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.profileimg_placeholder);
                        requestOptions.error(R.drawable.profileimg_placeholder);
                        Glide.with(PostRoomActivity_2.this)
                                .setDefaultRequestOptions(requestOptions)
                                .load(imgUri)
                                .into(imageView);
                    } else {
                        Toast.makeText(PostRoomActivity_2.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostRoomActivity_2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();
            uploadImage();
        } else {
            Toast.makeText(this, "Something wents wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void attemptNext() {
        boolean cancel = false;
        spinnerHomeValue = spinnerHome.getSelectedItem().toString();

        if (spinnerHomeValue.equals("e.g. Condo")) {
            ((TextView) spinnerHome.getSelectedView()).setError("Select one from drop down");
            focusView = spinnerHome;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            Toast.makeText(this, "Incorrect Input.", Toast.LENGTH_SHORT).show();
        } else {
            uploadData();
        }
    }

    private void uploadData(){
        String userID = firebaseUser.getUid();
        databaseReferenceRoommate = FirebaseDatabase.getInstance().getReference("Roommate").child(userID);



        HashMap dataMap = new HashMap();
        dataMap.put("homeType", spinnerHome.getSelectedItem().toString());
        dataMap.put("bedRoom", bedRoomValue);
        dataMap.put("bathRoom", bathRoomValue);
        databaseReferenceRoommate.updateChildren(dataMap);

        Intent intent = new Intent(this, PostRoomActivity_3.class);
        startActivity(intent);
    }
}
