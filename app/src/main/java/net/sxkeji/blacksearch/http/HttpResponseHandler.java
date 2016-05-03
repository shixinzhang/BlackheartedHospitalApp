/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package net.sxkeji.blacksearch.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;


public class HttpResponseHandler {
    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;

    private Handler handler;
    private View successedView;

    public HttpResponseHandler() {
        if (Looper.myLooper() != null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    HttpResponseHandler.this.handleMessage(msg);
                }
            };
        }
    }

    /**
     * 成功处理
     */
    public void onSuccess(String content) {
    }

    public void onSuccess(int statusCode, Headers headers, String content) {
        Log.e("responseHandler" ,"result+++++" + content);
        dealSuccessCallback(statusCode, headers, content);
    }

    public void onSuccess(int statusCode, String content) {
        onSuccess(content);

    }

    public void onFailure(Request request, IOException e) {
        Log.e("responseHandler" ,"result+++++" + e.toString());
    }

    //
    // 后台线程调用方法，通过Handler sendMessage把结果转到UI主线程
    //

    protected void sendSuccessMessage(Response response) {
        try {
            sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{new Integer(response.code()), response.headers(), response.body().string()}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendFailureMessage(Request request, IOException e) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, request}));
    }


    protected void handleSuccessMessage(int statusCode, Headers headers, String responseBody) {
        onSuccess(statusCode, headers, responseBody);
    }

    protected void handleFailureMessage(Request request, IOException e) {
        onFailure(request, e);
    }


    protected void handleMessage(Message msg) {
        Object[] response;

        switch (msg.what) {
            case SUCCESS_MESSAGE:
                response = (Object[]) msg.obj;
                handleSuccessMessage(((Integer) response[0]).intValue(), (Headers) response[1], (String) response[2]);
                break;
            case FAILURE_MESSAGE:
                response = (Object[]) msg.obj;
                handleFailureMessage((Request) response[1], (IOException) response[0]);
                break;
        }
    }

    protected void sendMessage(Message msg) {
        if (handler != null) {
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
    }

    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg = null;
        if (handler != null) {
            msg = this.handler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
    }

    public void onErrorCode(int statusCode, String content) {

    }

    private void dealSuccessCallback(int statusCode, Headers headers, String content) {
        if (statusCode == 200) {
            if (!TextUtils.isEmpty(content)) {

                    onSuccess(statusCode, content);

            } else {
               Log.e("dealSuccessCallback" ,"success but content is null");
            }
        } else {
            if (content.contains("appAlertMessage")) {
//                try {
//                    JSONObject jsonObject = new JSONObject(content);
//                    if (jsonObject.has("appAlertMessage")) {
//                        if (!TextUtils.isEmpty(jsonObject.optString("appAlertMessage"))) {
//                            UIUtils.showToastSafe(BaseApplication.getApplication(), jsonObject.optString("appAlertMessage"));
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                onErrorCode(statusCode, content);
            }
            if (statusCode == 401) {

            }

        }

    }
}
