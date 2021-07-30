package com.example.cs4227_project.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cs4227_project.R;
import com.example.cs4227_project.products.facade_pattern.AttributeManager;
import com.example.cs4227_project.util.database_controllers.ProductDatabaseController;
import com.example.cs4227_project.util.enums.FilterAttributes;
import com.example.cs4227_project.util.enums.ProductType;
import com.example.cs4227_project.util.LogTags;
import com.example.cs4227_project.order.command_pattern.AddStock;
import com.example.cs4227_project.order.command_pattern.CommandControl;
import com.example.cs4227_project.order.command_pattern.Stock;
import com.example.cs4227_project.products.abstract_factory_pattern.AbstractFactory;
import com.example.cs4227_project.products.abstract_factory_pattern.FactoryProducer;
import com.example.cs4227_project.products.abstract_factory_pattern.Product;
import com.example.cs4227_project.util.ui_controllers.ProductTypeController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddStockFragment extends Fragment implements View.OnClickListener {

    private Uri filePath;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private ImageView userPicture;
    private String path;
    private EditText pName, q1, q2, q3, price;
    private Spinner size1, size2, size3, colour, brand, style;
    private Map<Spinner, FilterAttributes> filterSpinners;
    private RadioGroup genderGroup;
    private RadioGroup categoryGroup;
    private boolean female;
    private ProductType pType;

    private boolean genderSelected = false;
    private boolean categorySelected = false;

    private static final int PICK_IMAGE_REQUEST = 22;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private List<String> size = new ArrayList<>();

    public AddStockFragment() {
        // Required empty public constructor
    }

    public static AddStockFragment newInstance() {
        return new AddStockFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_stock, container, false);

        Button uploadPic = rootView.findViewById(R.id.uploadImage);
        Button submit = rootView.findViewById(R.id.finish);

        SecureRandom rand = new SecureRandom();
        int randomInt = rand.nextInt(10000);
        path = "images/" + randomInt;

        //initialise UI objects
        pName = rootView.findViewById(R.id.productName);
        q1 = rootView.findViewById(R.id.quantity1);
        q2 = rootView.findViewById(R.id.quantity2);
        q3 = rootView.findViewById(R.id.quantity3);
        price = rootView.findViewById(R.id.price);

        size1 = rootView.findViewById(R.id.size1);
        size2 = rootView.findViewById(R.id.size2);
        size3 = rootView.findViewById(R.id.size3);
        colour = rootView.findViewById(R.id.colour);
        brand = rootView.findViewById(R.id.brand);
        style = rootView.findViewById(R.id.style);

        putSpinnersInMap();

        //set up radio groups and listeners
        genderGroup = rootView.findViewById(R.id.genderGroup);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //populate spinners based on selected button
                validateGenderInput();
                genderSelected = true;
                if(genderSelected && categorySelected) {
                    populateSpinners();
                }
            }
        });
        categoryGroup = rootView.findViewById(R.id.categoryGroup);
        categoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //populate spinners based on selected button
                validateCategoryInput();
                categorySelected = true;
                if(genderSelected && categorySelected) {
                    populateSpinners();
                }
            }
        });

        uploadPic.setOnClickListener(this);
        submit.setOnClickListener(this);

        userPicture = rootView.findViewById(R.id.productPic);
        Picasso.get().load("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png").fit().centerCrop().into(userPicture);

        return rootView;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadPic(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child(path);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws NullPointerException {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Log.d(LogTags.ADDSTOCK,""+ filePath);
            Picasso.get().load(filePath).centerCrop().fit().into(userPicture);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK  && data != null){
            Log.d(LogTags.ADDSTOCK," checking activity");
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            Log.d(LogTags.ADDSTOCK," " + photo);
            userPicture.setImageBitmap(photo);
            Uri tempUri = getImageUri(getContext(), photo);
            filePath = tempUri;
            Log.d(LogTags.ADDSTOCK," " + tempUri);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Log.d(LogTags.ADDSTOCK,"get bitmap");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String imagePath = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(imagePath);
    }

    public void validateGenderInput() {
        int genderId = genderGroup.getCheckedRadioButtonId();
        if(genderId == R.id.female) {
            Log.d(LogTags.COMMAND_DP, "Is female selected= " + female);
            female=true;
            ProductTypeController.setFemale(true);
        } else if(genderId == R.id.male) {
            Log.d(LogTags.COMMAND_DP, "Is female selected= " + female);
            female=false;
            ProductTypeController.setFemale(false);
        }
    }

    public void validateCategoryInput() {
        int checkedId = categoryGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.clothes) {
            pType = ProductType.CLOTHES;
        }else if(checkedId == R.id.shoes){
            pType = ProductType.SHOE;
        }else if(checkedId == R.id.accessories){
            pType = ProductType.ACCESSORIES;
        }
        ProductTypeController.setType(pType);
        Log.d(LogTags.COMMAND_DP, "Product type" + pType.getValue());
    }

    public Map<String, String> validateProductQuantitiesInput() {
        Map<String, String> sizeQuantities = new HashMap<>();

        //get text from inputs
        String s1 = size1.getSelectedItem().toString();
        String s2 = size2.getSelectedItem().toString();
        String s3 = size3.getSelectedItem().toString();
        String quant1 = q1.getText().toString();
        String quant2 = q2.getText().toString();
        String quant3 = q3.getText().toString();

        if(!s1.isEmpty() && !quant1.isEmpty()){
            sizeQuantities.put(s1, quant1);
            size.add(s1);
        }
        if(!s2.isEmpty() && !quant2.isEmpty()){
            sizeQuantities.put(s2, quant2);
            size.add(s2);
        }
        if(!s3.isEmpty() && !quant3.isEmpty()){
            sizeQuantities.put(s3, quant3);
            size.add(s3);
        }
        return sizeQuantities;
    }

    public void validateInput(){
        String productName = pName.getText().toString();
        String cost = price.getText().toString();
        String brandName = brand.getSelectedItem().toString();
        String color = colour.getSelectedItem().toString();
        String productStyle = style.getSelectedItem().toString();

        validateGenderInput();
        validateCategoryInput();
        Map<String, String> sizeQuantities = validateProductQuantitiesInput();

        if(productName.isEmpty()){
            pName.setError("Must have product name");
        } else if(sizeQuantities.size() == 0){
            Toast.makeText(getActivity(), "Product must contain stock", Toast.LENGTH_SHORT).show();
        } else if(brandName.equals("") || color.equals("") || cost.equals("") || productStyle.equals("")) {
            Toast.makeText(getActivity(), "All fields must be filled", Toast.LENGTH_SHORT).show();
        }else {
            createProduct(sizeQuantities, productName, color, brandName, cost, productStyle);
        }
    }

    public void createProduct(Map<String, String> sizeQuantities, String productName, String color, String brandName, String cost, String style){
        Map<String, Object> productData = new HashMap<>();
        AbstractFactory productFactory = FactoryProducer.getFactory(female);

        //Create Id to use as product id and for the stock id.
        String collection = pType.getValue() + female;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(collection).document();
        String productId = ref.getId();

        //Create hashmap containing product values.
        productData.put("id", productId);
        productData.put("name", productName);
        productData.put("price", Double.parseDouble(cost));
        productData.put("brand", brandName);
        productData.put("colour", color);
        productData.put("style", style);
        productData.put("size", size);
        productData.put("imageURL", path);


        //Create product and add to database
        Product p = productFactory.getProduct(pType, productData);
        Log.d(LogTags.COMMAND_DP, "New Product create: " + p.toString());

        ProductDatabaseController productDb = new ProductDatabaseController();
        productDb.addProductToDB(collection, productId, p);

        createStock(productId, sizeQuantities);
    }

    public void createStock(String id, Map<String, String> sizeQ){
        CommandControl commandController = new CommandControl();
        Stock stock = new Stock(id, sizeQ, pType.getValue(), female);
        AddStock addStock = new AddStock(stock);
        commandController.addCommand(addStock);
        commandController.executeCommands();
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if(i == R.id.uploadImage){
            selectImage();
        }
        if(i == R.id.finish) {
            validateInput();
            uploadPic();
        }
    }

    public void populateSpinners() {
        AttributeManager attributeManager = AttributeManager.getInstance();
        for(Map.Entry<Spinner, FilterAttributes> entry : filterSpinners.entrySet()) {
            List<String> values = new ArrayList<>();
            values.addAll(attributeManager.getAttributes(entry.getValue()));
            entry.getKey().setAdapter(initSpinner(values));
            Log.d(LogTags.SET_UP_FILTERS, "Set up "+entry.getValue().getValue()+" spinner");
        }
    }

    private ArrayAdapter<String> initSpinner(List<String> data) {
        ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return aa;
    }

    private void putSpinnersInMap() {
        filterSpinners = new HashMap<>();
        filterSpinners.put(size1, FilterAttributes.SIZES);
        filterSpinners.put(size2, FilterAttributes.SIZES);
        filterSpinners.put(size3, FilterAttributes.SIZES);
        filterSpinners.put(colour, FilterAttributes.COLOURS);
        filterSpinners.put(brand, FilterAttributes.BRANDS);
        filterSpinners.put(style, FilterAttributes.STYLES);
    }
}