package com.example.umpbizgo.Seller.Products;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
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

import static android.app.Activity.RESULT_OK;

public class EditAuthorizedProductFragment extends Fragment {
    View view;
    private EditText editproductname, editproductdescription, editproductprice;
    private TextView edittoolbarnamedisplay;
    private TextView editproductcategory;
    private ImageView editproductimage;
    private Button editproductbutton;
    private ImageButton gotoproductpage;
    private static final int GalleryPick = 1;
    private StorageReference ProductImageStorageReference;
    private DatabaseReference ProductReference;
    private Uri ProductImageUri;
    private String downloadImageUrl;
    private UploadTask uploadTask;
    private String checker = "";
    private String productID = "";

    public EditAuthorizedProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productID = bundle.getString("pid");
        }
        ProductImageStorageReference = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductReference = FirebaseDatabase.getInstance().getReference().child("Authorized Products").child(productID);
        editproductbutton = view.findViewById(R.id.edit_authorized_product);
        gotoproductpage = view.findViewById(R.id.backfromeditauthorizedproductpage);
        editproductdescription = view.findViewById(R.id.edit_authorized_product_description);
        editproductimage = view.findViewById(R.id.change_authorized_product_image);
        editproductprice = view.findViewById(R.id.edit_authorized_product_price);
        edittoolbarnamedisplay = view.findViewById(R.id.editproductnamedisplay);
        editproductname = view.findViewById(R.id.edit_authorized_product_name);
        editproductcategory = view.findViewById(R.id.edit_authorized_product_category);

        getProductDetails(productID);

        editproductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                OpenGallery();
            }
        });

        editproductcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryedit();
            }
        });

        gotoproductpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToProductPage();
            }
        });

        editproductbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked"))
                {
                    ProductInfoSaved();
                }
                else
                {
                    UpdateOnlyProductInfo();
                }
            }
        });

        return  view;
    }

    private void categoryedit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Product Category")
                .setItems(ProductCategoryConstants.category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String categorystatus = ProductCategoryConstants.category[i];
                        editproductcategory.setText(categorystatus);
                    }
                })
                .show();
    }

    private void GoToProductPage() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        SellerMyProductsFragment frageditproductsdetails = new SellerMyProductsFragment();
        ft3.replace(R.id.frame_edit_auth_product, frageditproductsdetails);
        ft3.commit();
    }

    private void UpdateOnlyProductInfo() {
        HashMap<String, Object> productInfo = new HashMap<>();
        productInfo.put("productname", editproductname.getText().toString());
        productInfo.put("description", editproductdescription.getText().toString());
        productInfo.put("price",editproductprice.getText().toString());
        productInfo.put("category",editproductcategory.getText().toString());

        ProductReference.updateChildren(productInfo);
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        SellerMyProductsFragment frageditproductsdetails = new SellerMyProductsFragment();
        ft3.replace(R.id.frame_edit_auth_product, frageditproductsdetails);
        ft3.commit();
    }

    private void ProductInfoSaved() {
        if(TextUtils.isEmpty((editproductname.getText().toString())))
        {
            Toast.makeText(getActivity(),"Product name is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((editproductdescription.getText().toString())))
        {
            Toast.makeText(getActivity(),"Product description is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((editproductprice.getText().toString())))
        {
            Toast.makeText(getActivity(),"Product price is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty((editproductcategory.getText().toString())))
        {
            Toast.makeText(getActivity(),"Product category is mandatory.",Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait we are updating your information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (ProductImageUri != null) {
            final StorageReference ProductImageReference = ProductImageStorageReference.child(ProductImageUri.getLastPathSegment() + productID + "jpg");

            final UploadTask uploadTask = ProductImageReference.putFile(ProductImageUri);

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
                            downloadImageUrl = ProductImageReference.getDownloadUrl().toString();
                            return ProductImageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Toast.makeText(getActivity(), "Product Image Url Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
        HashMap<String, Object>productInfos = new HashMap<>();
        productInfos.put("productname", editproductname.getText().toString());
        productInfos.put("description", editproductdescription.getText().toString());
        productInfos.put("price",editproductprice.getText().toString());
        productInfos.put("category",editproductcategory.getText().toString());
        productInfos.put("image",downloadImageUrl);

        ProductReference.updateChildren(productInfos);
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        SellerMyProductsFragment frageditproductsdetails = new SellerMyProductsFragment();
        ft3.replace(R.id.frame_edit_auth_product, frageditproductsdetails);
        ft3.commit();
    }

    private void getProductDetails(String productID) {
        ProductReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Products products = snapshot.getValue(Products.class);
                    edittoolbarnamedisplay.setText(products.getProductname());
                    editproductname.setText(products.getProductname());
                    editproductprice.setText(products.getPrice());
                    editproductdescription.setText(products.getDescription());
                    editproductcategory.setText(products.getCategory());
                    Picasso.get().load(products.getImage()).into(editproductimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            ProductImageUri = data.getData();
            editproductimage.setImageURI(ProductImageUri);
        }
    }
}
