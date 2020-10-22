package com.example.umpbizgo.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Models.Customer;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MyAccountCustomerFragment extends Fragment {
    View view;
    private CircleImageView profileImageView;
    private ImageButton BackButton;
    private EditText nameText, emailText, phoneText, passwordText, homeaddressText, cityAddressText;
    private TextView changeprofileImage;
    private Button UpdateButton;
    private StorageReference profileImageReference;
    private DatabaseReference UserReference;
    private FirebaseAuth firebaseAuth;
    private String downloadImageUrl;
    private String userID;
    private Uri profileImageUri;
    private String myUri = "";
    private UploadTask uploadTask;
    private String checker = "";

    public MyAccountCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_account_customer, container, false);
        profileImageReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        firebaseAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        UserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        profileImageView = view.findViewById(R.id.user_profile_image);
        nameText = view.findViewById(R.id.user_name);
        emailText = view.findViewById(R.id.user_email);
        phoneText = view.findViewById(R.id.user_phone_number);
        passwordText = view.findViewById(R.id.user_password);
        homeaddressText = view.findViewById(R.id.user_home_address);
        cityAddressText = view.findViewById(R.id.user_city_address);
        changeprofileImage = view.findViewById(R.id.change_profile_picture_btn);

        UpdateButton = view.findViewById(R.id.save_info_button);
        BackButton = view.findViewById(R.id.BacktoHomePageButton);

        userInfoDisplay(profileImageView, nameText, emailText, phoneText, passwordText, homeaddressText, cityAddressText);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BacktoHomePage();
            }
        });

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked"))
                {
                    UserInfoSaved();
                }
                else 
                {
                    UpdateOnlyUserInfo();
                }
            }
        });

        changeprofileImage.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

               Intent intent = CropImage.activity(profileImageUri)
                                .setAspectRatio(1,1)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .getIntent(getContext());
               startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }));



        return view;
    }

    private void BacktoHomePage() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

    private void UpdateOnlyUserInfo() {
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", nameText.getText().toString());
        userInfo.put("email", emailText.getText().toString());
        userInfo.put("phone", phoneText.getText().toString());
        userInfo.put("password", passwordText.getText().toString());
        userInfo.put("homeaddress", homeaddressText.getText().toString());
        userInfo.put("cityaddress", cityAddressText.getText().toString());

        UserReference.child(userID).updateChildren(userInfo);
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        Toast.makeText(getActivity(),"Profile Information Updated Successfully.", Toast.LENGTH_SHORT).show();
    }

    private void UserInfoSaved() {
        if(TextUtils.isEmpty((nameText.getText().toString())))
        {
            Toast.makeText(getActivity(),"Name is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((phoneText.getText().toString())))
        {
            Toast.makeText(getActivity(),"Phone number is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((emailText.getText().toString())))
        {
            Toast.makeText(getActivity(),"Email address is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((passwordText.getText().toString())))
        {
            Toast.makeText(getActivity(),"Password is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((cityAddressText.getText().toString())))
        {
            Toast.makeText(getActivity(),"City address is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((homeaddressText.getText().toString())))
        {
            Toast.makeText(getActivity(),"Home address is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            profileImageUri = result.getUri();
            profileImageView.setImageURI(profileImageUri);
        }
        else
        {
            Toast.makeText(getActivity(), "Error,Try Again",Toast.LENGTH_SHORT).show();
            FragmentTransaction ft3 = getFragmentManager().beginTransaction();
            MyAccountCustomerFragment fragmyaccount = new MyAccountCustomerFragment();
            ft3.replace(R.id.frame_customer_account, fragmyaccount);
            ft3.commit();
        }
    }


        private void uploadImage() {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Update Profile");
            progressDialog.setMessage("Please wait we are updating your information");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            if (profileImageUri != null) {
                final StorageReference fileReference = profileImageReference.child(profileImageUri.getLastPathSegment() + userID + "jpg");

                final UploadTask uploadTask = fileReference.putFile(profileImageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = e.toString();
                        Toast.makeText(getActivity(), "Error : ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "Product Image Uploaded succesfully.", Toast.LENGTH_SHORT).show();
                        Task<Uri> UriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                downloadImageUrl = fileReference.getDownloadUrl().toString();
                                return fileReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    downloadImageUrl = task.getResult().toString();
                                    Toast.makeText(getActivity(), "User Profile Image Url Uploaded Successfully", Toast.LENGTH_SHORT).show();

                                    SaveUserInformationToDatabase();
                                }
                            }
                        });
                    }
                });
            }
        }

        private void userInfoDisplay ( final CircleImageView profileImageView,
        final EditText nameText, final EditText emailText, final EditText phoneText,
        final EditText passwordText,
        final EditText homeaddressText, final EditText cityAddressText)
        {
            UserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
            UserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("image").exists()) {
                        Customer customer = snapshot.getValue(Customer.class);
                        Picasso.get().load(customer.getImage()).into(profileImageView);
                        nameText.setText(customer.getUsername());
                        emailText.setText(customer.getEmail());
                        phoneText.setText(customer.getPhone());
                        passwordText.setText(customer.getPassword());
                        homeaddressText.setText(customer.getHomeaddress());
                        cityAddressText.setText(customer.getCityaddress());
                    }
                    else
                    {
                        Customer customer = snapshot.getValue(Customer.class);
                        profileImageView.setImageResource(R.drawable.ic_customer);
                        nameText.setText(customer.getUsername());
                        emailText.setText(customer.getEmail());
                        phoneText.setText(customer.getPhone());
                        passwordText.setText(customer.getPassword());
                        homeaddressText.setText(customer.getHomeaddress());
                        cityAddressText.setText(customer.getCityaddress());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        private void SaveUserInformationToDatabase() {
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", nameText.getText().toString());
            userInfo.put("email", emailText.getText().toString());
            userInfo.put("phone", phoneText.getText().toString());
            userInfo.put("password", passwordText.getText().toString());
            userInfo.put("homeaddress", homeaddressText.getText().toString());
            userInfo.put("cityaddress", cityAddressText.getText().toString());
            userInfo.put("image", downloadImageUrl);

            UserReference.updateChildren(userInfo);
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "Profile Information Updated Successfully.", Toast.LENGTH_SHORT).show();
        }

    }