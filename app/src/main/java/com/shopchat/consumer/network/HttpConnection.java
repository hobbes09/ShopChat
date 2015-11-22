
package com.shopchat.consumer.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.shopchat.consumer.models.LoginModel;
import com.shopchat.consumer.utils.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Asynchronous HTTP connections
 *
 * @author Sudipta
 */
public class HttpConnection implements Runnable {

    public static final int TIMEOUT = 30000;
    public static final int DID_START = 0;
    public static final int DID_ERROR = 1;
    public static final int DID_SUCCEED = 2;
    public static final int DID_UNSUCCESS = 3;

    public static final int REQUEST_LOGIN = 6;
    public static final int REQUEST_COMMON = 7;
    public static final int REQUEST_OTP_REGISTRATION = 8;
    public static final int REQUEST_OTP_CONFIRMATION = 9;
    public static final int REQUEST_OTP_REGENERATION = 10;

    private static final int GET = 0;
    private static final int POST = 1;
    private static final int PUT = 2;
    private static final int DELETE = 3;
    private static final int BITMAP = 4;
    private static final int SOAP = 5;

    private String url;
    private int method;
    private final Handler handler;
    private int requestId;
    private String data;
    private LoginModel loginModel;
    private String requestBody;
    private DefaultHttpClient httpClient;
    private Message message = null;
    private int responseCode;
    private List<NameValuePair> nameValuePairsPost;

    public HttpConnection() {
        this(new Handler());
    }

    public HttpConnection(Handler _handler) {
        handler = _handler;
    }

    public void create(int method, String url, List<NameValuePair> pair) {
        this.method = method;
        this.url = url;
        this.nameValuePairsPost = pair;
        ConnectionManager.getInstance().push(this);
    }

    public void get(String url, LoginModel loginModel) {
        this.loginModel = loginModel;
        create(GET, url, null);

    }

    public void post(String url, List<NameValuePair> pair, String requestBody, int requestId, LoginModel loginModel) {
        this.requestId = requestId;
        this.loginModel = loginModel;
        this.requestBody = requestBody;
        create(POST, url, pair);
    }


    public void put(String url, List<NameValuePair> pair) {
        create(PUT, url, pair);
    }

    public void delete(String url) {
        create(DELETE, url, null);
    }

    public void bitmap(String _url) {

        create(BITMAP, _url, null);
    }

    public void bitmap(String _url, int pos, int sampleSize) {

        create(BITMAP, _url, null);

    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    @Override
    public void run() {
        handler.sendMessage(Message.obtain(handler, HttpConnection.DID_START));
        httpClient = new SSLHttpClient();
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT);
        try {
            HttpResponse response = null;
            switch (method) {
                case GET:
                    HttpGet httpGet = new HttpGet(url);
                    httpGet.setHeader("Content-Type", "application/json");
                    httpGet.setHeader("Accept", "application/json");
                    httpGet.setHeader("Cookie", loginModel.getCookieName() + "=" + loginModel.getCookieValue());
                    response = httpClient.execute(httpGet);
                    responseCode = response.getStatusLine().getStatusCode();
                    Log.d(Constants.TAG, "Url is::" + url);
                    Log.d(Constants.TAG, "Response Code::"
                            + responseCode);
                    processStreamEntity(response.getEntity());
                    break;
                case POST:
                    HttpPost httpPost = new HttpPost(url);
                    if (requestId == REQUEST_LOGIN) {
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairsPost));
                        response = httpClient.execute(httpPost);
                    } else {
                        httpPost.setHeader("Accept", "application/json");
                        httpPost.setHeader("Content-type", "application/json");
                        httpPost.setHeader("Cookie", loginModel.getCookieName() + "=" + loginModel.getCookieValue());
                        httpPost.setEntity(new StringEntity(requestBody));
                        response = httpClient.execute(httpPost);
                    }

                    responseCode = response.getStatusLine().getStatusCode();
                    Log.d(Constants.TAG, "Url is::" + url);
                    Log.d(Constants.TAG, "Response Code::"
                            + responseCode);
                    processStreamEntity(response.getEntity());
                    break;
                case SOAP:

                    break;
                case PUT:
                    HttpPut httpPut = new HttpPut(url);
                    httpPut.setEntity(new StringEntity(data));
                    response = httpClient.execute(httpPut);
                    responseCode = response.getStatusLine().getStatusCode();
                    processStreamEntity(response.getEntity());
                    break;
                case DELETE:
                    response = httpClient.execute(new HttpDelete(url));
                    Log.d(Constants.TAG, "Url is::" + url);
                    break;
                case BITMAP:
                    response = httpClient.execute(new HttpGet(url));
                    responseCode = response.getStatusLine().getStatusCode();
                    processBitmapEntity(response.getEntity());
                    Log.d(Constants.TAG, "Url is::" + url);
                    break;
            }

        } catch (Exception e) {
            Log.d(Constants.TAG, e.toString());
            handler.sendMessage(Message.obtain(handler,
                    HttpConnection.DID_ERROR, ""));
        }
        ConnectionManager.getInstance().didComplete(this);
    }

    private void processStreamEntity(HttpEntity entity)
            throws IllegalStateException, IOException {
        String response = null;
        // To Skip HTML reading
        if (requestId == REQUEST_LOGIN) {
            if (responseCode != HttpStatus.SC_OK) {
                response = readBuffer(entity);
            }
        } else {
            response = readBuffer(entity);
        }


        Log.d(Constants.TAG, "Response Data::"
                + response);

        if (responseCode == HttpStatus.SC_OK || responseCode == HttpStatus.SC_ACCEPTED) {
            if (requestId == REQUEST_LOGIN) {
                List<Cookie> list = httpClient.getCookieStore().getCookies();
                if (!list.isEmpty()) {
                    Cookie cookie = list.get(0);
                    LoginModel loginModel = new LoginModel(cookie.getName(), cookie.getValue());
                    message = Message.obtain(handler, DID_SUCCEED,
                            loginModel);
                }
            } else {
                message = Message.obtain(handler, DID_SUCCEED,
                        response.toString());
            }
            handler.sendMessage(message);
        } else {
            if (requestId == REQUEST_OTP_REGISTRATION || requestId == REQUEST_OTP_CONFIRMATION || requestId == REQUEST_OTP_REGENERATION) {
                if (responseCode == HttpStatus.SC_BAD_REQUEST) {
                    message = Message.obtain(handler, DID_UNSUCCESS, response);
                    handler.sendMessage(message);
                } else if (responseCode == HttpStatus.SC_CONFLICT) {
                    message = Message.obtain(handler, DID_UNSUCCESS, response);
                    handler.sendMessage(message);
                } else {
                    message = Message.obtain(handler, DID_ERROR, "");
                    handler.sendMessage(message);
                }
            } else if (requestId == REQUEST_LOGIN) {
                if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
                    message = Message.obtain(handler, DID_UNSUCCESS, response);
                    handler.sendMessage(message);
                } else {
                    message = Message.obtain(handler, DID_ERROR, "");
                    handler.sendMessage(message);
                }
            } else {
                message = Message.obtain(handler, DID_ERROR, "");
                handler.sendMessage(message);
            }
        }

    }

    private String readBuffer(HttpEntity entity) {
        InputStream is = null;
        StringBuilder resultBuilder = new StringBuilder();
        try {
            is = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                resultBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultBuilder.toString();
    }

    private void processBitmapEntity(HttpEntity entity) throws IOException {

        if (responseCode == HttpStatus.SC_OK) {
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);

            InputStream inputStream = bufHttpEntity.getContent();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = -1;

            Bitmap bm = BitmapFactory.decodeStream(inputStream, new Rect(-1,
                    -1, -1, -1), options);

        } else {

            message = Message.obtain(handler, DID_UNSUCCESS, "");
        }

    }

}
