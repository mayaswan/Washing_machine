package com.example.ruchadapong.projectapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;

public class RegisterFragment extends Fragment {
    
    private ImageView imageView;
    private Uri uri;
    private boolean aBoolean = true;
    private String nameString,emailString,passwordString;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();
        
        // Avatar Controller
        
        avatarController();


    } //Main Method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemUpload) {

            checkUpload();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUpload() {

        MyAlert myAlert = new MyAlert(getActivity());

        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText emailEditText = getView().findViewById(R.id.edtEmail);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);

        nameString = nameEditText.getText().toString().trim();
        emailString = emailEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (aBoolean) {

            myAlert.normalDialog("ยังไม่มีการเลือกรูปภาพ","กรุณาเลือกรูปภาพของท่าน");

        } else if (nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty()) {
            // มีช่องว่าง
            myAlert.normalDialog("ข้อมูลของท่านยังไม่ครบถ้วน","กรุณากรอกข้อมูลของท่านให้ครบถ้วน");

        }else {
            // ไม่มีช่องว่าง
            uploadAvatar();
        }

    } //checkUpload

    private void uploadAvatar() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("อัพโหลดข้อมูลของท่าน");
        progressDialog.setMessage("โปรดรอสักครู่...");
        progressDialog.show();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference storageReference1 = storageReference.child("Avatar/" + nameString);
        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(),"อัพโหลดข้อมูลของท่านเสร็จสมบูรณ์",Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

                registerEmail();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"ไม่สามารถอัพโหลดได้ ==> " + e.toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    private void registerEmail() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailString,passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            //success
                            updateDatabase();
                        }else {

                            //Non success
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.normalDialog("สมัครสมาชิกไม่สำเร็จ",task.getException().getMessage());
                        }
                    }
                });

    } // registerEmail

    private void updateDatabase() {

        //Find UID
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String uidString = firebaseAuth.getUid();
        Log.d("16Oct61","uid ==> " + uidString);

        //Find URL of Avatar
        final String urlAvatarString = null;
        try {

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();

            final String[] strings = new String[1];

            storageReference.child("Avatar").child(nameString).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            strings[0] = uri.toString();
                            Log.d("16Oct61","url ==> " + strings[0]);
                            insertValueToFirebase(uidString, strings[0]);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("16Oct61","e Storage ==> " + e.toString());
                }
            });
        }catch (Exception e){

            Log.d("16Oct61","e ==> " + e.toString());

        }

    }

    private void insertValueToFirebase(String uidString, String urlAvatarString) {

        //Setter Value to Model
        UserModel userModel = new UserModel(nameString,urlAvatarString);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child("User")
                .child(uidString);

        //Getter by Model
        databaseReference.setValue(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"ยินดีต้อนรับ " + nameString,Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("16Oct61","e Database ==> " + e.toString());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            uri = data.getData();
            aBoolean = false;
            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap,800,600,true);
                imageView.setImageBitmap(bitmap1);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(getActivity(),"โปรดเลือกรูปภาพของท่าน",Toast.LENGTH_SHORT).show();
        }


    }

    private void avatarController() {

        imageView = getView().findViewById(R.id.imgAvatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"โปรดเลือกรูปภาพของท่าน"),1);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register,menu);
    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.register);
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("กรอกข้อมูลให้ครบถ้วน");

        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        setHasOptionsMenu(true);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        return view;
    }
}
