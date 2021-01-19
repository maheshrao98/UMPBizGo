package com.example.umpbizgo.Seller.SocialPosts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Admin.AdminProductApprovalActivity;
import com.example.umpbizgo.Admin.AdminProductUnauthorizeActivity;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Posts;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Products.SellerMyProductsFragment;
import com.example.umpbizgo.Seller.SellerHomeActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {
    private EditText editpostname, editpostdescription;
    private TextView toolbarTitle;
    private ImageView editpostimage;
    private Button editpostbutton;
    private ImageButton gotoprostpage, deletepost;
    private static final int GalleryPick = 1;
    private StorageReference PostImageStorageReference;
    private DatabaseReference PostReference, SellerReference;
    private Uri PostImageUri;
    private String downloadImageUrl;
    private UploadTask uploadTask;
    private String checker = "";
    private String postID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        PostImageStorageReference = FirebaseStorage.getInstance().getReference().child("Post Images");
        PostReference = FirebaseDatabase.getInstance().getReference().child("Authorized Posts").child(postID);
        SellerReference = FirebaseDatabase.getInstance().getReference().child("Sellers");

        Intent intent = getIntent();
        postID = intent.getExtras().getString("pid");
        
        editpostdescription = findViewById(R.id.edit_post_description);
        editpostname = findViewById(R.id.edit_post_title);
        editpostimage = findViewById(R.id.change_post_image);
        gotoprostpage = findViewById(R.id.backfromeditpost);
        deletepost = findViewById(R.id.deletepost);
        toolbarTitle = findViewById(R.id.editpostnamedisplay);
        editpostbutton = findViewById(R.id.edit_post);
        
        getPostDetails(postID);

        editpostimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                OpenGallery();
            }
        });

        gotoprostpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBack();
            }
        });

        deletepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeletePost();
            }
        });

        editpostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked"))
                {
                    PostInfoSaved();
                }
                else
                {
                    UpdateOnlyPostInfo();
                }
            }
        });
    }

    private void UpdateOnlyPostInfo() {
        HashMap<String, Object> postinfo = new HashMap<>();
        postinfo.put("title", editpostname.getText().toString());
        postinfo.put("description", editpostdescription.getText().toString());

        PostReference.updateChildren(postinfo);
        Intent intent = new Intent(EditPostActivity.this, ManagePostsActivity.class);
        startActivity(intent);
        Toast.makeText(EditPostActivity.this, "Post Updated Succesfully.", Toast.LENGTH_SHORT).show();
    }

    private void PostInfoSaved() {
        if(TextUtils.isEmpty((editpostname.getText().toString())))
        {
            Toast.makeText(EditPostActivity.this,"Post title is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((editpostdescription.getText().toString())))
        {
            Toast.makeText(EditPostActivity.this,"Post description is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(EditPostActivity.this);
        progressDialog.setTitle("Update Product");
        progressDialog.setMessage("Please wait we are updating your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(PostImageUri!=null) {
            final StorageReference PostImageReference = PostImageStorageReference.child(PostImageUri.getLastPathSegment() + postID + "jpg");

            final UploadTask uploadTask = PostImageReference.putFile(PostImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(EditPostActivity.this, "Error : ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditPostActivity.this, "Post Image Uploaded succesfully.", Toast.LENGTH_SHORT).show();
                    Task<Uri> UriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageUrl = PostImageReference.getDownloadUrl().toString();
                            return PostImageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Toast.makeText(EditPostActivity.this, "Post Image Url Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
        HashMap<String, Object> postinfo = new HashMap<>();
        postinfo.put("title", editpostname.getText().toString());
        postinfo.put("description", editpostdescription.getText().toString());
        postinfo.put("image",downloadImageUrl);

        PostReference.updateChildren(postinfo);
        Intent intent = new Intent(EditPostActivity.this, ManagePostsActivity.class);
        startActivity(intent);
        Toast.makeText(EditPostActivity.this, "Post Updated Succesfully.", Toast.LENGTH_SHORT).show();
    }


    private void DeletePost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this,R.style.AlertDialog);
        builder.setTitle("Confirm Delete Post");
        builder.setMessage("Are you sure to delete the post?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                PostReference.removeValue();
                Intent intent = new Intent(EditPostActivity.this, ManagePostsActivity.class);
                startActivity(intent);
                Toast.makeText(EditPostActivity.this, "Post Deleted Succesfully.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(EditPostActivity.this, ManagePostsActivity.class);
                startActivity(intent);
            }
        });

        builder.show();
    }

    private void GoBack() {
        Intent intent = new Intent(EditPostActivity.this, ManagePostsActivity.class);
        startActivity(intent);
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
            PostImageUri = data.getData();
            editpostimage.setImageURI(PostImageUri);
        }
    }

    private void getPostDetails(String postID) {
        PostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Posts posts = snapshot.getValue(Posts.class);
                    toolbarTitle.setText(posts.getTitle());
                    editpostname.setText(posts.getTitle());
                    editpostdescription.setText(posts.getDescription());
                    Picasso.get().load(posts.getImage()).into(editpostimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}