package app.mohit.com.bhawsarsamaj;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
//import com.androidtutorialshub.loginregister.R;
import app.mohit.com.bhawsarsamaj.model.User;
import app.mohit.com.bhawsarsamaj.util.FileUploadUtility;
import app.mohit.com.bhawsarsamaj.util.InputValidation;
import app.mohit.com.bhawsarsamaj.util.ServerUtil;
//import com.androidtutorialshub.loginregister.sql.DatabaseHelper;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Handler.Callback {

    private final AppCompatActivity activity = RegisterActivity.this;

    private static NestedScrollView nestedScrollView;


    private TextInputEditText userid;
    private TextInputEditText username;
    private TextInputEditText address;
    private TextInputEditText city;
    private TextInputEditText password;
    private TextInputEditText passwordConfirm;
    private TextInputLayout useridLayout;
    private TextInputLayout usernameLayout;
    private TextInputLayout addressLayout;
    private TextInputLayout citynameLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;

    private AppCompatButton onRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private ImageView img;
    private String gender = "male";
    private String dob ;


    private InputValidation inputValidation;
  //  private DatabaseHelper databaseHelper;
    private User user;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SELECT_AUDIO = 2;
    private String selectedPath;
    private Handler handler;
    private TextView tvStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    //    getSupportActionBar().hide();

        findViewById(R.id.selectFile).setOnClickListener(this);
      //m  findViewById(R.id.uploadFile).setOnClickListener(this);
        tvStatus = findViewById(R.id.tvStatus);
        handler = new Handler(this);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            initViews();
            initListeners();
            initObjects();


        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_AUDIO) {
                Uri selectedImageUri = data.getData();
                selectedImageUri = handleImageUri(selectedImageUri);
                selectedPath = getRealPathFromURI(selectedImageUri);
                tvStatus.setText("Selected Path :: " + selectedPath);
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize = 2;
               android.graphics.Bitmap bitmap = BitmapFactory.decodeFile(selectedPath, options);
                img.setImageBitmap(BitmapFactory.decodeFile(selectedPath));
               // img.setImageURI(selectedImageUri);
                Log.i(TAG, " Path :: " + selectedPath);
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    gender = "male";
                    break;
            case R.id.female:
                if (checked)
                    gender = "female";
                    break;
        }
    }

    @SuppressLint("NewApi")
    public String getRealPathFromURI(Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }



        public static Uri handleImageUri(Uri uri) {
        if (uri.getPath().contains("content")) {
            Pattern pattern = Pattern.compile("(content://media/.*\\d)");
            Matcher matcher = pattern.matcher(uri.getPath());
            if (matcher.find())
                return Uri.parse(matcher.group(1));
            else
                throw new IllegalArgumentException("Cannot handle this URI");
        }
        return uri;
    }

    public void openGallery() {
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            // do your stuff..

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image "), SELECT_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(getApplicationContext(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }


    @Override
    public boolean handleMessage(Message msg) {
        Log.i("File Upload", "Response :: " + msg.obj);
        String success = 1 == msg.arg1 ? "File Upload Success" : "File Upload Error";
        Log.i(TAG, success);
        tvStatus.setText(success);
        return false;
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);


        userid = (TextInputEditText) findViewById(R.id.userid);
        useridLayout = (TextInputLayout) findViewById(R.id.useridLayout);

        username = (TextInputEditText) findViewById(R.id.username);
        usernameLayout = (TextInputLayout) findViewById(R.id.usernameLayout);

        address = (TextInputEditText) findViewById(R.id.addressitem);
        addressLayout = (TextInputLayout) findViewById(R.id.addressLayout);

        city = (TextInputEditText) findViewById(R.id.city);
        citynameLayout = (TextInputLayout) findViewById(R.id.cityname);


        password = (TextInputEditText) findViewById(R.id.password);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);


        passwordConfirm = (TextInputEditText) findViewById(R.id.confirmPassword);
        confirmPasswordLayout = (TextInputLayout) findViewById(R.id.confirmPasswordLayout);





        onRegister = (AppCompatButton) findViewById(R.id.onRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

        ((RadioButton) findViewById(R.id.male)).setChecked(true);

        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
        img = (ImageView)findViewById(R.id.imageView1);

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        onRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
    //    databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onRegister:
                onRegisterUser();
                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
             if (v.getId() == R.id.selectFile) {
                openGallery();
            }
           /* if (v.getId() == R.id.uploadFile) {
                if (null != selectedPath && !selectedPath.isEmpty()) {
                    tvStatus.setText("Uploading..." + selectedPath);
                    FileUploadUtility.doFileUpload(selectedPath, handler);
                }
            }*/
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void onRegisterUser() {
        if (!inputValidation.isInputEditTextFilled(userid, useridLayout,  "Enter contact no.")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(username, usernameLayout, "Enter full name")) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(address, addressLayout,  "Enter Address")) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(city, citynameLayout,  "Enter City")) {
            return;
        }

        if (null == selectedPath || selectedPath.isEmpty()) {
            Snackbar.make(nestedScrollView,  "Select Face Pic", Snackbar.LENGTH_LONG).show();
            return;
        }


        if (!inputValidation.isInputEditTextFilled(password, passwordLayout, "Enter password")) {
            return;
        }

        if (!inputValidation.isInputEditTextMatches(password, passwordConfirm,
                confirmPasswordLayout, getString(R.string.error_password_match))) {
            return;
        }

   if(true){
            HashMap map = new HashMap();
            map.put("userid",userid.getText().toString().trim());
            map.put("username",username.getText().toString().trim());
            map.put("gender",gender);
            map.put("address",address.getText().toString().trim());
            map.put("city",city.getText().toString().trim());
            map.put("password",password.getText().toString().trim());
            map.put("moto","registeruser");
            map.put("dob",dateView.getText());

            String picid = "";


               tvStatus.setText("Uploading..." + selectedPath);
               Log.e("Selected Path",selectedPath);
               picid = FileUploadUtility.doFileUpload(selectedPath, handler);
               Log.e("Pic ID received ",picid);



           map.put("profileid",picid);


       String str =  ServerUtil.getResponseFromServer(map);
       Log.d("==========","done..."+str);





       user.setName(userid.getText().toString().trim());
            user.setEmail(username.getText().toString().trim());
            user.setPassword(address.getText().toString().trim());

       //     databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView,  "sdsfds", Snackbar.LENGTH_LONG).show();
            emptyInputEditText();



        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }


    }

    static String SERVER_PATH = "http://openapp-bhawsarapp.1d35.starter-us-east-1.openshiftapps.com/request";




    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        userid.setText(null);
        username.setText(null);
        address.setText(null);
        password.setText(null);
        passwordConfirm.setText(null);
    }
}
