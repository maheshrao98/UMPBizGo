package com.example.umpbizgo.Seller.SocialPosts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Products.AddProductActivity;
import com.example.umpbizgo.Seller.Products.ProductCategoryConstants;
import com.example.umpbizgo.Seller.SellerHomeActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddSocialPostsActivity extends AppCompatActivity {
    private String  Description, Title, SaveCurrentDate, SaveCurrentTime;
    private Button AddNewPostButton;
    private EditText InputPostTitle, InputPostDescription;
    private ImageView InputPostImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String postKey, downloadImageUrl;
    private StorageReference PostImageRef, filePath;
    private DatabaseReference PostRef, SellerReference;
    private ProgressDialog loadingbar;
    private String businessName, sellerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_social_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar_seller);
        toolbar.setTitle("Add New Post");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sellerhome:
                        Intent intent = new Intent(AddSocialPostsActivity.this, SellerHomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        Intent intent2 = new Intent(AddSocialPostsActivity.this, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        PostImageRef = FirebaseStorage.getInstance().getReference().child("Post Images");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Authorized Posts");
        SellerReference = FirebaseDatabase.getInstance().getReference().child("Sellers");

        AddNewPostButton = (Button) findViewById(R.id.post_new_post);
        InputPostTitle = (EditText) findViewById(R.id.post_title);
        InputPostDescription = (EditText)findViewById(R.id.post_description);
        InputPostImage = (ImageView)findViewById(R.id.select_post_image);
        loadingbar = new ProgressDialog(AddSocialPostsActivity.this);

        InputPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });

        SellerReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            businessName = snapshot.child("businessname").getValue().toString();
                            sellerID = snapshot.child("sid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void ValidateProductData() {
        Description = InputPostDescription.getText().toString();
        Title = InputPostTitle.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(AddSocialPostsActivity.this, "Post Image is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(AddSocialPostsActivity.this, "Please write post description",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Title))
        {
            Toast.makeText(AddSocialPostsActivity.this, "Please write post title",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StorePostInformation();
        }

    }

    private void StorePostInformation() {
        loadingbar.setTitle("Add New Post");
        loadingbar.setMessage("Please wait while we are processing your request.");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime = currentTime.format(calendar.getTime());

        postKey = PostRef.push().getKey();

        filePath = PostImageRef.child(ImageUri.getLastPathSegment() + postKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddSocialPostsActivity.this, "Error : ",Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddSocialPostsActivity.this, "Post Image Uploaded succesfully.", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri>then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception{
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AddSocialPostsActivity.this, "Getting Post Image Url Succesfully.",Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("poid",postKey);
        productMap.put("date",SaveCurrentDate);
        productMap.put("time",SaveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("title",Title);

        productMap.put("sellerbusinessname", businessName);
        productMap.put("sellerid", sellerID);


        PostRef.child(postKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AddSocialPostsActivity.this, SellerHomeActivity.class);
                            startActivity(intent);

                            loadingbar.dismiss();
                            Toast.makeText(AddSocialPostsActivity.this, "Post is added successfully.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddSocialPostsActivity.this,"Error :",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void OpenGallery() {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, GalleryPick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            InputPostImage.setImageURI(ImageUri);
        }
    }
}