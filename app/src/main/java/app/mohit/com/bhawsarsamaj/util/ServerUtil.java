package app.mohit.com.bhawsarsamaj.util;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import app.mohit.com.bhawsarsamaj.RegisterActivity;

/**
 * Created by user on 24-03-2018.
 */



public class ServerUtil {

    static String SERVER_PATH = "http://openapp-bhawsarapp.1d35.starter-us-east-1.openshiftapps.com/request";

    public static String getResponseFromServer(final HashMap map) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String responsestr = null;
                System.out.println("==>" + map);
                Log.d("myApp=====>", map.toString());


                HttpClient client = null;
                if (map.size() > 0) {
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    for (Object keys : map.keySet()) {
                        postParameters.add(new BasicNameValuePair(keys.toString(), map.get(keys).toString()));
                    }

                    try {

                        String responseFromServer = null;
                        client = new DefaultHttpClient();
                        HttpPost postRequest = new HttpPost(SERVER_PATH);
                        postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

                        HttpResponse response;


                        response = client.execute(postRequest);
                        Log.d("myApp=====>", "zxczxczxczxczxc");
                        if (response != null) {
                            int code = response.getStatusLine().getStatusCode();
                            System.out.println(response.getStatusLine().getStatusCode());
                            if (code == 200) {
                                InputStream ips = response.getEntity().getContent();
                                BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                                StringBuilder sb = new StringBuilder();
                                String s;
                                while (true) {
                                    s = buf.readLine();
                                    if (s == null || s.length() == 0)
                                        break;
                                    sb.append(s);
                                }
                                responsestr = sb.toString();

                                Log.e("RESPONSE ASYNC::", responsestr);
                                buf.close();
                                ips.close();
                            }
                        }

                //        Thread.sleep(10000);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Log.e("#######", e.getMessage(), e);
                    } finally {
                        client.getConnectionManager().shutdown();
                    }
                } else {
                    Log.d("asd", "emprtyddfdg");
                    System.out.println("Map is empty");
                }
                Log.d("asdadad","asdas"+responsestr);
          //      return responsestr;
            }

        }).start();

        return "";
    }



    public static String getResponseFromServerSync(final HashMap map) {
                 String responsestr = null;
                System.out.println("==>" + map);
                Log.e("myApp=====>", map.toString());
                 HttpClient client = null;
                if (map.size() > 0) {
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    for (Object keys : map.keySet()) {
                        postParameters.add(new BasicNameValuePair(keys.toString(), map.get(keys).toString()));
                    }
                     try {
                        String responseFromServer = null;
                        client = new DefaultHttpClient();
                        HttpPost postRequest = new HttpPost(SERVER_PATH);
                        postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
                        HttpResponse response;
                        response = client.execute(postRequest);
                         if (response != null) {
                            int code = response.getStatusLine().getStatusCode();
                            System.out.println(response.getStatusLine().getStatusCode());
                            if (code == 200) {
                                InputStream ips = response.getEntity().getContent();
                                BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                                StringBuilder sb = new StringBuilder();
                                String s;
                                while (true) {
                                    s = buf.readLine();
                                    if (s == null || s.length() == 0)
                                        break;
                                    sb.append(s);
                                }
                                responsestr = sb.toString();

                                Log.e("RESPONSE:", responsestr);
                                buf.close();
                                ips.close();
                            }
                        }
                     } catch (Throwable e) {
                        e.printStackTrace();
                        Log.e("#######", e.getMessage(), e);
                    } finally {
                        client.getConnectionManager().shutdown();
                    }
                } else {
                    Log.e("asd", "emprtyddfdg");
                    System.out.println("Map is empty");
                }
                Log.e("asdadad","asdas"+responsestr);
         return responsestr;
    }


    public static Bitmap getImageFromServer(HashMap map){
        {
            String responsestr = null;
            System.out.println("==>" + map);
            Log.e("get Image from server", map.toString());
            HttpClient client = null;
            InputStream ips = null;
            if (map.size() > 0) {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                for (Object keys : map.keySet()) {
                    postParameters.add(new BasicNameValuePair(keys.toString(), map.get(keys).toString()));
                }
                try {
                    String responseFromServer = null;
                    client = new DefaultHttpClient();
                    HttpPost postRequest = new HttpPost(SERVER_PATH);

                    postRequest.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
                    HttpResponse response;
                    response = client.execute(postRequest);
                    if (response != null) {
                        int code = response.getStatusLine().getStatusCode();
                        System.out.println(response.getStatusLine().getStatusCode());
                        if (code == 200) {
                            ips = response.getEntity().getContent();
                            Bitmap myBitmap = BitmapFactory.decodeStream(ips);
                            return myBitmap;
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    Log.e("#######", e.getMessage(), e);
                } finally {
                    try {
                        if (ips != null)
                            ips.close();
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                    client.getConnectionManager().shutdown();
                }
            } else {
                Log.e("asd", "emprtyddfdg");
                System.out.println("Map is empty");
            }
            Log.e("asdadad","asdas"+responsestr);
            return null;
        }
    }






}
