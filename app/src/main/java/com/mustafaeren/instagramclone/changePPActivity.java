    package com.mustafaeren.instagramclone;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;

    import android.Manifest;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.ImageDecoder;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.DocumentReference;
    import com.google.firebase.firestore.FieldValue;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;
    import com.google.firebase.storage.UploadTask;

    import java.io.IOException;
    import java.util.HashMap;
    import java.util.UUID;

    public class changePPActivity extends AppCompatActivity {
    Bitmap selectedImage;
    ImageView newPPImage;
    EditText bioText;
    Button ppUpdate;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    Uri imageData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_p_p);

        newPPImage = findViewById(R.id.changePP);
        bioText = findViewById(R.id.igBio);
        ppUpdate = findViewById(R.id.profilGuncelleme);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        ppUpdate.setEnabled(true);
    }

    public void selecetImageForChangePP(View view)
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else
        {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK && data!=null)
        {
            imageData = data.getData();
            try {
                if(Build.VERSION.SDK_INT>=28)
                {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                }
                else
                {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    newPPImage.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ppAndBioChange(View view)
    {
//        if(imageData!=null){
//            ppUpdate.setEnabled(false);
//            UUID uuid = UUID.randomUUID();
//            final String imageName = "profileImages/"+uuid+".jpg";
//
//            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
//                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            String downloadURL = uri.toString();
//                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                            String userEmail = firebaseUser.getEmail();
//                            String newBio  = bioText.getText().toString();
//
//                            HashMap<String,Object> profileData = new HashMap<>();
//                            profileData.put("userEmail",userEmail);
//                            profileData.put("bio",newBio);
//                            profileData.put("downloadUrl",downloadURL);
//                            profileData.put("date", FieldValue.serverTimestamp());
//
//                            firebaseFirestore.collection("profiles").add(profileData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Intent intentToProfile = new Intent(changePPActivity.this,ProfileActivity.class);
//                                    intentToProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intentToProfile);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(changePPActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(changePPActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(changePPActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
//                }
//            });
//        }

        if(imageData!=null){
            ppUpdate.setEnabled(false);
            UUID uuid = UUID.randomUUID();
            final String imageName = "profileImages/"+uuid+".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadURL = uri.toString();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userEmail = firebaseUser.getEmail();
                            String newBio  = bioText.getText().toString();

                            HashMap<String,Object> profileData = new HashMap<>();
                            profileData.put("userEmail",userEmail);
                            profileData.put("bio",newBio);
                            profileData.put("downloadUrl",downloadURL);
                            profileData.put("date", FieldValue.serverTimestamp());


                            firebaseFirestore.collection("UserProfile").document(userEmail).set(profileData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intentToProfile = new Intent(changePPActivity.this,ProfileActivity.class);
                                    intentToProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intentToProfile);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(changePPActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(changePPActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(changePPActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    public void signOut(View view)
    {
        firebaseAuth.signOut();
        Intent intent = new Intent(changePPActivity.this,SignInActivity.class);
        startActivity(intent);
        finish();
    }

}