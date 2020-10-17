package com.example.umpbizgo.Seller;

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
import android.widget.ImageView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {

    private String categoryname, Description, Price, ProductName, SaveCurrentDate, SaveCurrentTime;
    private Button AddNewProductButton;
    private EditText InputProductName, InputProductDescription,InputProductPrice;
    private ImageView InputProductImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productKey, downloadImageUrl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef, SellerReference;
    private ProgressDialog loadingbar;
    private String businessName, sellerID;
    View view;

    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_product, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            categoryname = bundle.getString("category");
        }
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        SellerReference = FirebaseDatabase.getInstance().getReference().child("Sellers");

        AddNewProductButton = (Button) view.findViewById(R.id.add_new_product);
        InputProductName = (EditText) view.findViewById(R.id.product_name);
        InputProductDescription = (EditText)view.findViewById(R.id.product_description);
        InputProductPrice = (EditText) view.findViewById(R.id.product_price);
        InputProductImage = (ImageView)view.findViewById(R.id.select_product_image);
        loadingbar = new ProgressDialog(getActivity());

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    private void ValidateProductData() {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        ProductName = InputProductName.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(getActivity(), "Product Image is working",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(getActivity(), "Please write product description",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(getActivity(), "Please write product price",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(ProductName))
        {
            Toast.makeText(getActivity(), "Please write product name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {
        loadingbar.setTitle("Add New Product");
        loadingbar.setMessage("Please wait while we are processing your request.");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime = currentTime.format(calendar.getTime());

        productKey = SaveCurrentDate + SaveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getActivity(), "Error : ",Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Product Image Uploaded succesfully.", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(getActivity(), "Getting Product Image Url Succesfully.",Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productKey);
        productMap.put("date",SaveCurrentDate);
        productMap.put("time",SaveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryname);
        productMap.put("price", Price);
        productMap.put("productname", ProductName);

        productMap.put("sellerbusinessname", businessName);
        productMap.put("sellerid", sellerID);


        ProductRef.child(productKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            PickCategoryFragment fragaddproduct = new PickCategoryFragment();
                            ft.replace(R.id.frame_pick_category, fragaddproduct);
                            ft.commit();

                            loadingbar.dismiss();
                            Toast.makeText(getActivity(), "Product is added successfully.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(),"Error :",Toast.LENGTH_SHORT).show();
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
            InputProductImage.setImageURI(ImageUri);
        }
    }


}