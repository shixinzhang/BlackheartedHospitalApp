package net.sxkeji.blacksearch.http;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import net.sxkeji.blacksearch.http.coreprogress.helper.ProgressHelper;
import net.sxkeji.blacksearch.http.coreprogress.listener.impl.UIProgressListener;
import net.sxkeji.blacksearch.utils.NetWorkUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 网络请求
 * Created by tiansj on 15/2/27.
 */
public class HttpClient {
    static volatile HttpClient singleton = null;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private String UTF_8 = "UTF-8";
    private MediaType MEDIA_TYPE = MediaType.parse("text/plain;");
    private OkHttpClient client = new OkHttpClient();
    private boolean isShowProgressDialog = true;
    private Context context;
    private ProgressDialog dialog;
    private View successedView;


    public HttpClient(Context context) {
        this.context = context;
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        initDialog();

    }

    private void initDialog() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在卖力追查中...");
        dialog.setCancelable(false);
    }

    private void dialogShow() {
        dialog.show();
    }

    private void dialogCancle() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    public static HttpClient builder(Context context) {
//        if (singleton == null) {
//            synchronized (HttpClient.class) {
//                if (singleton == null) {
//                    singleton = new HttpClient(context);
//                }
//            }
//        }
        synchronized (HttpClient.class) {
            singleton = new HttpClient(context);
        }
        return singleton;
    }

    /*
    * 控制ProgressDialog的显示 默认是显示的，设置isShow为false 可以取消显示
    * */
    public HttpClient showProgress(boolean isShow) {
        this.isShowProgressDialog = isShow;
        return this;
    }


    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Log.v("ConnectivityManager", e.getMessage());
        }
        return false;
    }

    public void get(String url, Map<String, String> map, final HttpResponseHandler httpResponseHandler) {
        if (NetWorkUtils.isNetworkAvailable(context)) {


            if (isShowProgressDialog) {
                dialogShow();
            }
            url = url + "?" + mapUrl(map);
            Log.e("get", "url++++++++++++" + url);
//            httpResponseHandler.setLoadingPage( successedView);
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) throws IOException {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendSuccessMessage(response);
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendFailureMessage(request, e);
                }
            });
        } else {

            Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();

        }
    }

    public void post(String url, Map<String, String> map, final HttpResponseHandler httpResponseHandler) {
        if (NetWorkUtils.isNetworkAvailable(context)) {


            if (isShowProgressDialog) {
                dialogShow();
            }
            Log.e("post", "url++++++" + url);


//            httpResponseHandler.setLoadingPage(successedView);
            FormEncodingBuilder builder = new FormEncodingBuilder();
            mapBuilder(map, builder);
            Request request = new Request.Builder().url(url).post(builder.build()).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendSuccessMessage(response);
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendFailureMessage(request, e);
                }
            });
        } else {

            Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();

        }

    }

    public void postImage(String url, Map<String, String> map, final HttpResponseHandler httpResponseHandler, UIProgressListener uiProgressRequestListener) {
        RequestBody requestBody = null;
        if (NetWorkUtils.isNetworkAvailable(context)) {


            if (isShowProgressDialog) {
                dialogShow();
            }


            File uploadFile = new File(map.get("imageFile"));
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                    .addFormDataPart("moduleName", map.get("moduleName"))
                    .addFormDataPart("token", map.get("token"))
                    .addFormDataPart("clientVersion", "1.0.0")

                    .addFormDataPart("imageFile", "image.png", RequestBody.create(MEDIA_TYPE_PNG, uploadFile))

                    .build();


            Request request = new Request.Builder()
                    .url(url)
//                .addHeader("Content-Type", "multipart/form-data;boundary=" + BOUNDARY)
                    .post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener))
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendSuccessMessage(response);
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendFailureMessage(request, e);
                }
            });
        } else {

            Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
        }

    }


    private void mapBuilder(Map<String, String> map, FormEncodingBuilder builder) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
            Log.e("mapBuilder", "m.getKey()+++++" + entry.getKey() + " " + entry.getValue());
        }
    }

    private String mapUrl(Map<String, String> map) {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append("=");
            buffer.append(entry.getValue());
            buffer.append("&");
        }
        buffer.setLength(buffer.length() - 1);
        return buffer.toString();
    }

}

