package com.example.umpbizgo.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Models.Seller;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopProfileActivity extends AppCompatActivity {
    private CircleImageView shoplogoImageView;
    private ImageButton BackButton;
    private TextView changelogoImage;
    private Button UpdateButton;
    private StorageReference sellerlogoImageReference;
    private DatabaseReference SellerReference;
    TextView shopname, email, password, phone, address;
    FirebaseAuth firebaseAuth;
    String sellerID;
    private String downloadImageUrl;
    private Uri profileImageUri;
    private String myUri = "";
    private UploadTask uploadTask;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);
        sellerlogoImageReference = FirebaseStorage.getInstance().getReference().child("ShopLogos");
        firebaseAuth = FirebaseAuth.getInstance();
        sellerID = firebaseAuth.getCurrentUser().getUid();

        SellerReference = FirebaseDatabase.getInstance().getReference().child("Sellers").child(sellerID);
        
        shoplogoImageView = findViewById(R.id.shop_logo_image);
        shopname = findViewById(R.id.shop_name);
        email = findViewById(R.id.shop_email);
        password =findViewById(R.id.shop_password);
        phone = findViewById(R.id.shop_phone_number);
        address = findViewById(R.id.shop_address);
        changelogoImage = findViewById(R.id.change_shop_logo_btn);
        
        UpdateButton = findViewById(R.id.save_shop_info_button);
        BackButton = findViewById(R.id.BacktoShopHomePageButton);
        
        shopinfodisplay(shopname,email,password,phone,address,shoplogoImageView);

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

        changelogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                Intent intent = CropImage.activity(profileImageUri)
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(ShopProfileActivity.this);
                startActivityForResult(intent,CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void UpdateOnlyUserInfo(){
        HashMap<String, Object> shopInfo = new HashMap<>();
        shopInfo.put("businessname", shopname.getText().toString());
        shopInfo.put("email",email.getText().toString());
        shopInfo.put("password",password.getText().toString());
        shopInfo.put("phone",phone.getText().toString());
        shopInfo.put("address",address.getText().toString());

        SellerReference.updateChildren(shopInfo);
        Intent intent2 = new Intent(ShopProfileActivity.this, SellerHomeActivity.class);
        startActivity(intent2);
        Toast.makeText(ShopProfileActivity.this,"Shop Profile Information Updated Successfully.", Toast.LENGTH_SHORT).show();
    }

    private void UserInfoSaved() {
        if(TextUtils.isEmpty((shopname.getText().toString())))
        {
            Toast.makeText(ShopProfileActivity.this,"Business name is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((phone.getText().toString())))
        {
            Toast.makeText(ShopProfileActivity.this,"Phone number is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((email.getText().toString())))
        {
            Toast.makeText(ShopProfileActivity.this,"Email address is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((password.getText().toString())))
        {
            Toast.makeText(ShopProfileActivity.this,"Password is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((address.getText().toString())))
        {
            Toast.makeText(ShopProfileActivity.this,"Business address is mandatory.",Toast.LENGTH_SHORT).show();
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
            shoplogoImageView.setImageURI(profileImageUri);
        }
        else
        {
            Toast.makeText(ShopProfileActivity.this, "Error,Try Again",Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(ShopProfileActivity.this, ShopProfileActivity.class);
            startActivity(intent);
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(ShopProfileActivity.this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait we are updating your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (profileImageUri != null) {
            final StorageReference fileReference = sellerlogoImageReference.child(profileImageUri.getLastPathSegment() + sellerID + "jpg");

            final UploadTask uploadTask = fileReference.putFile(profileImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(ShopProfileActivity.this, "Error : ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ShopProfileActivity.this, "Shop Logo Image Uploaded succesfully.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ShopProfileActivity.this, "Shop Logo Image Url Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                SaveUserInformationToDatabase();
                            }
                        }
                    });
                }
            });
        }
    }

    private void SaveUserInformationToDatabase() {
        HashMap<String, Object> shopInfo = new HashMap<>();
        shopInfo.put("businessname", shopname.getText().toString());
        shopInfo.put("email",email.getText().toString());
        shopInfo.put("password",password.getText().toString());
        shopInfo.put("phone",phone.getText().toString());
        shopInfo.put("address",address.getText().toString());
        shopInfo.put("shoplogo",downloadImageUrl);

        SellerReference.updateChildren(shopInfo);
        Intent intent2 = new Intent(ShopProfileActivity.this, SellerHomeActivity.class);
        startActivity(intent2);
        Toast.makeText(ShopProfileActivity.this,"Shop Profile Information Updated Successfully.", Toast.LENGTH_SHORT).show();
    }

    private void BacktoHomePage() {
        Intent intent = new Intent(ShopProfileActivity.this, SellerHomeActivity.class);
        startActivity(intent);
    }

    private void shopinfodisplay(TextView shopname, TextView email, TextView password, TextView phone, TextView address, CircleImageView shoplogoImageView) {
        SellerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("shoplogo").exists()){
                    Seller seller = snapshot.getValue(Seller.class);
                    Picasso.get().load(seller.getLogo()).into(shoplogoImageView);
                    shopname.setText(seller.getBusinessName());
                    email.setText(seller.getEmail());
                    password.setText(seller.getPassword());
                    phone.setText(seller.getPhone());
                    address.setText(seller.getAddress());
                }
                else {
                    Seller seller = snapshot.getValue(Seller.class);
                    shoplogoImageView.setImageResource(R.drawable.ic_landscape);
                    shopname.setText(seller.getBusinessName());
                    email.setText(seller.getEmail());
                    password.setText(seller.getPassword());
                    phone.setText(seller.getPhone());
                    address.setText(seller.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}