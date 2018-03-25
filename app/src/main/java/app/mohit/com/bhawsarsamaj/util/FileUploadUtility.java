package app.mohit.com.bhawsarsamaj.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import app.mohit.com.bhawsarsamaj.MainActivity;

public class FileUploadUtility {

  // static String SERVER_PATH = "http://codemansion.co.in/FileUploadTest/file_upload.php";

    static String SERVER_PATH = "http://openapp-bhawsarapp.1d35.starter-us-east-1.openshiftapps.com/upload";

    private static final String TAG = FileUploadUtility.class.getSimpleName();

    public static String doFileUpload(final String selectedPath, final Handler handler) {

            String picid = "";

    //    new Thread(new Runnable() {
     //       @Override
    //        public void run() {
                String responseFromServer = null;
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                File file  = new File(selectedPath);
                try {
                    InputStream is = new FileInputStream(file);
                    byte[] bytes = IOUtils.toByteArray(is);
                    multipartEntity.addPart(file.getName(),
                            new ByteArrayBody(bytes,
                                    ContentType.APPLICATION_OCTET_STREAM, file.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HttpClient client = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(SERVER_PATH);
                postRequest.setEntity(multipartEntity);
                HttpResponse response;
                try {
                    response = client.execute(postRequest);
                    responseFromServer = response.getStatusLine().getReasonPhrase();
                    if (response != null) {
                        System.out.println("Response : "  );
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

                        Log.e("myApp=====>", sb.toString());
                         int code = response.getStatusLine().getStatusCode();
                        System.out.println(response.getStatusLine().getStatusCode());
                        if (code == 200) {
                            Log.d(TAG, " File uploaded succuss 200");
                            Header[] headers = response.getAllHeaders();

                            for(Header header : headers){
                                Log.e( "header ",header.toString());
                            }
                            picid = response.getHeaders("picid")[0].getValue();
                            Log.e("picid",picid);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Debug", "error: " + e.getMessage() );
                    sendMessageBack(responseFromServer, 0, handler);
                }finally {
                    client.getConnectionManager().shutdown();
                }

            //    responseFromServer = processResponse(conn, responseFromServer);
                sendMessageBack(responseFromServer, 1, handler);
                return picid;
     //m       }
     //m   }).start();

    }




    private static String processResponse(HttpURLConnection conn, String responseFromServer) {
        DataInputStream inStream;
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;

            while ((str = inStream.readLine()) != null) {
                responseFromServer = str;
            }
            inStream.close();

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        return responseFromServer;
    }

    static void sendMessageBack(String responseFromServer, int success, Handler handler) {
        Message message = new Message();
        message.obj = responseFromServer;
        message.arg1 = success;
        handler.sendMessage(message);
    }

    private void olduploadcode() throws Exception{
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String selectedPath =null;
        final Handler handler;
        String lineEnd = "rn";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";

            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(new File(selectedPath));
            // open a URL connection to the Servlet
            URL url = new URL(SERVER_PATH);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                    + selectedPath + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
    }





}
