package com.luns.neuro.mlkn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.luns.neuro.mlkn.library.ConnectionDetector;
import com.luns.neuro.mlkn.library.SharedPrefManager;
import com.luns.neuro.mlkn.library.User;
import com.luns.neuro.mlkn.library.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.luns.neuro.mlkn.library.MyApplication.TAG;

public class ProfessionalProfile extends AppCompatActivity{
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Button btnBack, btnNext;
    View llyt1,llyt2,llyt3;
    private int intCurrent=0;
    private ProgressBar progressBar;
    private ImageView imgVProfilePhoto;
    private Bitmap bitmap0;
    private int TAKE_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_REQUEST = 1;
    private String strImageSelector="";
    String[] nationality ={"Kenya","Uganda","Tanzania","Rwanda","Ethiopia","South Sudan","Somalia","Congo","Burundi","South Africa","Nigeria","Ghana","Chad","Angola","Mozambique","Madagascar","Sierra Leone","Egypt","Libya","Tunisia"};
    //String[] county ={"Nairobi","Kiambu","Kisumu","Mombasa","Machakos","Turkana","Kakamega","Bungoma","Narok","Nakuru","Eldoret","Lamu","Nyeri","Garissa","Isiolo","Murang'a","Vihiga","Busia","Taita Taveta","Kisii","Bomet","West Pokot"};
    //String[] physicalAddress ={"Juja","Thika","Gatundu","Muchatha","Mang'u","Kilimani","HighPoint","Kinangop","Kapsiyiwa"};
    //String[] profession ={"Electrician","Plumber","Carpenter","Mechanic","Mason","Artist","Watchman","Cleaner","Tutor","Painter","Gardener"};
    //String[] highestEducation ={"Other","Professor","PHD","Masters","Higher National Diploma","Degree","Diploma","Certificate","HighSchool","Primary"};
    String[]  physicalAddressArr={},countyArr={},edcertlevelArr ={},professionArr={},allLocationsArr={};
    private AutoCompleteTextView edtNationality,edtCounty,edtPhysicalAddress,edtProfessionalTitle,edtSecondProfessionalTitle,edtThirdProfessionalTitle,edtHighestEducationLevel;
    private EditText edtFullName,edtIdNo,edtNextOfKinConctact,edtSpecialization;
    private String strFullName,strIdNo,strNextOfKinConctact,strSpecialization,strSecondProfessionalTitle,strHighestEducationLevel;
    private String strNationality,strCounty,strPhysicalAddress,strProfessionalTitle,strThirdProfessionalTitle;

    private String strPrfImageUrlBndls="",strFullNameBndls="",strIdNoBndls="",strNextOfKinConctactBndls="",strSpecializationBndls="";
    private String strSecondProfessionalTitleBndls="",strHighestEducationLevelBndls="",strThirdProfessionalTitleBndls="";
    private String strProfessionalTitleBndls="",strPhysicalAddressBndls="",strCountyBndls="",strNationalityBndls="";


    ArrayList<String> countiesArrayList = new ArrayList<String>();
    ArrayList<String> physicalAddressArrayList = new ArrayList<String>();
    String [] countiesAL ={};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professional_profile);
        try {
            Bundle extras = getIntent().getExtras();
            professionArr = extras.getStringArray("professionArr");
            edcertlevelArr = extras.getStringArray("edcertlevelArr");
            allLocationsArr = extras.getStringArray("allLocationsArr");
            Log.e(TAG, "QQQQ: " + Arrays.toString(allLocationsArr));
            strPrfImageUrlBndls = extras.getString("strPrfImageUrl");
            strFullNameBndls = extras.getString("strFullName");
            strIdNoBndls = extras.getString("strIdNo");
            strNextOfKinConctactBndls = extras.getString("strNextOfKinConctact");
            strSpecializationBndls = extras.getString("strSpecialization");
            strSecondProfessionalTitleBndls = extras.getString("strSecondProfessionalTitle");
            strHighestEducationLevelBndls = extras.getString("strHighestEducationLevel");
            strThirdProfessionalTitleBndls = extras.getString("strThirdProfessionalTitle");
            strProfessionalTitleBndls = extras.getString("strProfessionalTitle");
            strPhysicalAddressBndls = extras.getString("strPhysicalAddress");
            strCountyBndls = extras.getString("strCounty");
            strNationalityBndls = extras.getString("strNationality");

        }catch (NullPointerException f){
            ///////////////////
        }
        progressBar =  findViewById(R.id.progressBarPP);
        intCurrent=1;
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        addBottomDots(intCurrent);
        llyt1=findViewById(R.id.llyt1);
        llyt1.setVisibility(View.VISIBLE);
        llyt2=findViewById(R.id.llyt2);
        llyt2.setVisibility(View.GONE);
        llyt3=findViewById(R.id.llyt3);
        llyt3.setVisibility(View.GONE);
        imgVProfilePhoto =findViewById(R.id.imgVProfilePhoto);
        imgVProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strImageSelector="0";
                selectImage(strImageSelector);

            }
        });
        bitmap0 = BitmapFactory.decodeResource(getResources(),R.drawable.placer);
        imgVProfilePhoto.setImageBitmap(bitmap0);


        for (String strElement : allLocationsArr) {
            String[] separatorArray = strElement.split("@@");
            String strPhysicalLocality = separatorArray[0];
            String strCountyElement = separatorArray[1];
            countiesArrayList.add(strCountyElement);

            Log.e(TAG, "XXXX: " + strCountyElement);
        }

        //countiesAL = (String[]) countiesArrayList.toArray(new String[countiesArrayList.size()]);


        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,nationality);
        //Getting the instance of AutoCompleteTextView
        edtNationality =  (AutoCompleteTextView)findViewById(R.id.edtNationality);
        edtNationality.setThreshold(1);//will start working from first character
        edtNationality.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        edtNationality.setTextColor(Color.BLACK);
        edtNationality.setText(strNationalityBndls);
        if (!edtNationality.getText().toString().equals("")||!edtNationality.getText().toString().isEmpty())
            edtNationality.setEnabled(false);

        ////Cleaning duplicate counties Elements
        Set<String> source1=new HashSet<>();
        source1.addAll(countiesArrayList);
        countiesArrayList.clear();
        countiesArrayList.addAll(source1);

        countyArr = (String[]) countiesArrayList.toArray(new String[countiesArrayList.size()]);
        Log.e(TAG, "ZZZZ: " + Arrays.toString(countyArr));

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,countyArr);
        //Getting the instance of AutoCompleteTextView
        edtCounty =  (AutoCompleteTextView)findViewById(R.id.edtCounty);
        edtCounty.setThreshold(1);//will start working from first character
        edtCounty.setAdapter(adapter2);//setting the adapter data into the AutoCompleteTextView
        edtCounty.setTextColor(Color.BLACK);
        edtCounty.setText(strCountyBndls);

        String strControlCounty=edtCounty.getText().toString();

        physicalAddressArrayList.clear();
        for (String strElement : allLocationsArr) {
            String[] separatorArray = strElement.split("@@");
            String strPhysicalLocality = separatorArray[0];
            String strCountyElement = separatorArray[1];
            if (strControlCounty.equals(strCountyElement))
                physicalAddressArrayList.add(strPhysicalLocality);
        }
        physicalAddressArr = (String[]) physicalAddressArrayList.toArray(new String[physicalAddressArrayList.size()]);
        Log.e(TAG, "WWWW: " + Arrays.toString(physicalAddressArr));

        edtPhysicalAddress =  (AutoCompleteTextView)findViewById(R.id.edtPhysicalAddress);
        edtPhysicalAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    String strControlCounty=edtCounty.getText().toString();
                    edtPhysicalAddress.setText("");
                    physicalAddressArrayList.clear();
                    for (String strElement : allLocationsArr) {
                        String[] separatorArray = strElement.split("@@");
                        String strPhysicalLocality = separatorArray[0];
                        String strCountyElement = separatorArray[1];
                        if (strControlCounty.equals(strCountyElement))
                            physicalAddressArrayList.add(strPhysicalLocality);
                    }
                    physicalAddressArr = (String[]) physicalAddressArrayList.toArray(new String[physicalAddressArrayList.size()]);
                    Log.e(TAG, "WWWW: " + Arrays.toString(physicalAddressArr));

                }
            }
        });

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,physicalAddressArr);
        //Getting the instance of AutoCompleteTextView
        edtPhysicalAddress.setThreshold(1);//will start working from first character
        edtPhysicalAddress.setAdapter(adapter3);//setting the adapter data into the AutoCompleteTextView
        edtPhysicalAddress.setTextColor(Color.BLACK);
        edtPhysicalAddress.setText(strPhysicalAddressBndls);


        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,professionArr);
        //Getting the instance of AutoCompleteTextView
        edtProfessionalTitle =  (AutoCompleteTextView)findViewById(R.id.edtProfessionalTitle);
        edtProfessionalTitle.setThreshold(1);//will start working from first character
        edtProfessionalTitle.setAdapter(adapter4);//setting the adapter data into the AutoCompleteTextView
        edtProfessionalTitle.setTextColor(Color.BLACK);
        edtProfessionalTitle.setText(strProfessionalTitleBndls);

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter4S = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,professionArr);
        //Getting the instance of AutoCompleteTextView
        edtSecondProfessionalTitle =  (AutoCompleteTextView)findViewById(R.id.edtSecondProfessionalTitle);
        edtSecondProfessionalTitle.setThreshold(1);//will start working from first character
        edtSecondProfessionalTitle.setAdapter(adapter4S);//setting the adapter data into the AutoCompleteTextView
        edtSecondProfessionalTitle.setTextColor(Color.BLACK);
        edtSecondProfessionalTitle.setText(strSecondProfessionalTitleBndls);

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter4T = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,professionArr);
        //Getting the instance of AutoCompleteTextView
        edtThirdProfessionalTitle =  (AutoCompleteTextView)findViewById(R.id.edtThirdProfessionalTitle);
        edtThirdProfessionalTitle.setThreshold(1);//will start working from first character
        edtThirdProfessionalTitle.setAdapter(adapter4T);//setting the adapter data into the AutoCompleteTextView
        edtThirdProfessionalTitle.setTextColor(Color.BLACK);
        edtThirdProfessionalTitle.setText(strThirdProfessionalTitleBndls);

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,edcertlevelArr);
        //Getting the instance of AutoCompleteTextView
        edtHighestEducationLevel =  (AutoCompleteTextView)findViewById(R.id.edtHighestEducationLevel);
        edtHighestEducationLevel.setThreshold(1);//will start working from first character
        edtHighestEducationLevel.setAdapter(adapter5);//setting the adapter data into the AutoCompleteTextView
        edtHighestEducationLevel.setTextColor(Color.BLACK);
        edtHighestEducationLevel.setText(strHighestEducationLevelBndls);


        edtFullName =findViewById(R.id.edtFullName);
        edtFullName.setText(strFullNameBndls);
        if (!edtFullName.getText().toString().equals("")||!edtFullName.getText().toString().isEmpty())
            edtFullName.setEnabled(false);

        edtIdNo=findViewById(R.id.edtIdNo);
        edtIdNo.setText(strIdNoBndls);
        if (!edtIdNo.getText().toString().equals("")||!edtIdNo.getText().toString().isEmpty())
            edtIdNo.setEnabled(false);

        edtNextOfKinConctact=findViewById(R.id.edtNextOfKinConctact);
        edtNextOfKinConctact.setText(strNextOfKinConctactBndls );
        edtSpecialization=findViewById(R.id.edtSpecialization);
        edtSpecialization.setText(strSpecializationBndls);

        btnBack = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack.setText("Back");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intCurrent ==3 ) {
                    intCurrent--;
                    llyt1.setVisibility(View.GONE);
                    llyt2.setVisibility(View.VISIBLE);
                    llyt3.setVisibility(View.GONE);
                    addBottomDots(intCurrent);
                    btnNext.setText("Next");
                } else if (intCurrent ==2){
                    intCurrent--;
                    llyt1.setVisibility(View.VISIBLE);
                    llyt2.setVisibility(View.GONE);
                    llyt3.setVisibility(View.GONE);
                    addBottomDots(intCurrent);
                    btnBack.setVisibility(View.GONE);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setVisibility(View.VISIBLE);
              if (intCurrent ==1) {
                    intCurrent++;
                    llyt1.setVisibility(View.GONE);
                    llyt2.setVisibility(View.VISIBLE);
                    llyt3.setVisibility(View.GONE);
                    addBottomDots(intCurrent);
                } else if (intCurrent ==2){
                    llyt1.setVisibility(View.GONE);
                    llyt2.setVisibility(View.GONE);
                    llyt3.setVisibility(View.VISIBLE);
                    intCurrent++;
                    addBottomDots(intCurrent);
                    btnNext.setText("Save");
              }else {
                  confirmSave();

              }

                }
        });
    }
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private String UPLOAD_URL ="https://www.instrov.com/malakane_init/mlkn_upload_profile.php";
    private String KEY_IMAGE="prfimage";
    private String KEY_USEREMAIL="usrEmail";
    private String KEY_USERPHONENUMBER="usrPhonenumber";
    private String KEY_UFNAME="strFullName";
    private String KEY_UIDNO= "strIdNo";
    private String KEY_UNEXTOFKINCONTACT="strNextOfKinConctact";
    private String KEY_USPECIALIZATION="strSpecialization";
    private String KEY_USECONDPROFESSIONALTITLE="strSecondProfessionalTitle";
    private String KEY_UHIGHESTEDUCATIONLEVEL="strHighestEducationLevel";
    private String KEY_UNATIONALITY="strNationality";
    private String KEY_UCOUNTY="strCounty";
    private String KEY_UPHYSICALADDRESS="strPhysicalAddress";
    private String KEY_UPROFESSIONALTITLE= "strProfessionalTitle";
    private String KEY_UTHIRDPROFESSIONALTITLE="strThirdProfessionalTitle";

    StringRequest stringRequest;
    private void uploadData(){
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            progressBar.setVisibility(View.VISIBLE);
            //loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
            stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Disimissing the progress dialog
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfessionalProfile.this, "Professional profile updated successfully" , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), My_Profile.class);
                            startActivity(intent);
                            finish();
                            //Showing toast message of the response

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            progressBar.setVisibility(View.GONE);
                            ////////////////////
                            if(volleyError instanceof TimeoutError ||volleyError instanceof NoConnectionError){
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_network_timeout),
                                        Toast.LENGTH_LONG).show();
                            }else if (volleyError instanceof AuthFailureError){
                                //
                                Toast.makeText(ProfessionalProfile.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ServerError){
                                //
                                Toast.makeText(ProfessionalProfile.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof NetworkError){
                                //
                                Toast.makeText(ProfessionalProfile.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }else if (volleyError instanceof ParseError){
                                Toast.makeText(ProfessionalProfile.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                            }
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    String image =  getStringImage(bitmap0);
                    //Getting User details
                    User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                    //strPhonenumber = user.getPhonenumber();
                    //strEmail = user.getUser_priviledge();
                    //Creating parameters
                    Map<String,String> params = new Hashtable<String, String>();
                    //Adding parameters
                    params.put(KEY_IMAGE, image);
                    params.put(KEY_USEREMAIL, user.getUser_priviledge());
                    params.put(KEY_USERPHONENUMBER, user.getPhonenumber());
                    params.put(KEY_UFNAME, strFullName);
                    params.put(KEY_UIDNO, strIdNo);
                    params.put(KEY_UNEXTOFKINCONTACT, strNextOfKinConctact);
                    params.put(KEY_USPECIALIZATION, strSpecialization);
                    params.put(KEY_USECONDPROFESSIONALTITLE, strSecondProfessionalTitle);
                    params.put(KEY_UHIGHESTEDUCATIONLEVEL, strHighestEducationLevel);
                    params.put(KEY_UNATIONALITY, strNationality);
                    params.put(KEY_UCOUNTY, strCounty);
                    params.put(KEY_UPHYSICALADDRESS, strPhysicalAddress);
                    params.put(KEY_UPROFESSIONALTITLE, strProfessionalTitle);
                    params.put(KEY_UTHIRDPROFESSIONALTITLE, strThirdProfessionalTitle);
                    //returning parameters
                    return params;
                }
            };

            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            //requestQueue.add(request);
            //DefaultRetryPolicy  retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            //requestQueue.setRetryPolicy(retryPolicy);
            //.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Adding request to the queue
            requestQueue.add(stringRequest);
        } else {
            //Snackbar.make(recyclerView, "No Internet connection, check settings and try again.", Snackbar.LENGTH_LONG)
            //      .setAction("Action", null).show();
            Snackbar snackbar = Snackbar
                    .make(llyt1, "No internet connection! Check settings and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            uploadData();                 }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }

    }
    private void confirmSave(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save changes to your profile?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                // TODO Auto-generated method stub
                strFullName = edtFullName.getText().toString().trim();
                strIdNo= edtIdNo.getText().toString().trim();
                strNextOfKinConctact = edtNextOfKinConctact.getText().toString().trim();
                strNationality = edtNationality.getText().toString().trim();
                strCounty = edtCounty.getText().toString().trim();
                strPhysicalAddress= edtPhysicalAddress.getText().toString().trim();
                strProfessionalTitle= edtProfessionalTitle.getText().toString().trim();
                strSecondProfessionalTitle= edtSecondProfessionalTitle.getText().toString().trim();
                strThirdProfessionalTitle= edtThirdProfessionalTitle.getText().toString().trim();
                strSpecialization= edtSpecialization.getText().toString().trim();
                strHighestEducationLevel= edtHighestEducationLevel.getText().toString().trim();

                if(strFullName !="" && strIdNo !="" && strNextOfKinConctact!="" &&
                        strNationality!="" && strCounty!=""&& strPhysicalAddress!=""&& strProfessionalTitle!="" && strSpecialization!=""&& strHighestEducationLevel!=""

                        && !strFullName.isEmpty() && !strIdNo.isEmpty()&&
                        !strNextOfKinConctact.isEmpty() && !strNationality.isEmpty()  && !strCounty.isEmpty()  && !strPhysicalAddress.isEmpty()
                        && !strProfessionalTitle.isEmpty() && !strSpecialization.isEmpty()  && !strHighestEducationLevel.isEmpty()
                ){
                    uploadData();
                }else{
                    Snackbar.make(llyt1,"Please fill in all starred fields.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                /////////
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog alert = builder.create();
        //alert.getWindow().setBackgroundDrawableResource(R.color.btn_bg);
        alert.show();
    }


    String userChoosenTask;
    private void selectImage(final String strImageSelectorReceiver) {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Back" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfessionalProfile.this);
        builder.setTitle("Get Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(ProfessionalProfile.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        if (strImageSelectorReceiver=="0"){openCamera();}

                } else if (items[item].equals("Choose from Gallery")) {
                    //userChoosenTask="Choose from Library";
                    //   if(result)
                    if (strImageSelectorReceiver=="0"){showFileChooser();}
                } else if (items[item].equals("Back")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public String getStringImage(Bitmap bmp){
        //try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
        //}catch (NullPointerException jj){

        //}
        // return new;
    }


    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, TAKE_IMAGE_REQUEST);  }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap0 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imgVProfilePhoto.setImageBitmap(bitmap0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == TAKE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                bitmap0 = (Bitmap) data.getExtras().get("data");
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgVProfilePhoto.setImageBitmap(bitmap0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }
/////////////////////////

    private void addBottomDots(int currentPage) {
        dots = new TextView[4];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 1; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            //dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_professionalprofile, menu);
//        // Associate searchable configuration with the SearchView
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.nav_btMenuProfProf) {
////            item.getItemId()
////            Menu menu = navigation.getMenu();
//            item.setTitle("Cancel");
//
//            //findItem(R.id.navigation_home).setChecked(true);
//
//
//        }
//        return super.onOptionsItemSelected(item);
//    }


    int x=0;
    @Override
    public void onBackPressed() {
        x++;
        // super.onBackPressed();
        if (x==0) {
         //   llytregistration.setVisibility(View.GONE);
           // llytlogin.setVisibility(View.VISIBLE);
            //x=true;
        }else if (x==1){
            finish();
        }

    }

}