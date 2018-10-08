package com.runchain.plugins.security;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtil extends CordovaPlugin {

    private static final String LOG_TAG = "SecurityUtil";
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** */
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    private CallbackContext mCallbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Activity activity = cordova.getActivity();

        Context context = activity.getApplicationContext();

        this.mCallbackContext = callbackContext;

        String code = "0";

        String message = "";

        Map<String, Object> resultMap = new HashMap<>();

        File dataDirectory = context.getFilesDir();

        String pubFilePath = dataDirectory.getAbsolutePath() + "/" + "public.key";

        String privateFilePath = dataDirectory.getAbsolutePath() + "/" + "private.key";

        File pubFile = new File(pubFilePath);

        File priFile = new File(privateFilePath);

        Log.d(LOG_TAG, "pubFilePath: " + pubFilePath);

        Log.d(LOG_TAG, "privateFilePath: " + privateFilePath);

        if ("genKeyPairMethod".equals(action)) {

            String msg = args.getString(0);

            Log.d(LOG_TAG, "MSG: " + msg);

            try {
                Map<String, Object> keyMap = RSAUtil.genKeyPair();

                String publicKey = RSAUtil.getPublicKey(keyMap);

                String privateKey = RSAUtil.getPrivateKey(keyMap);

                Log.d(LOG_TAG, "dataDirectory:" + dataDirectory.getAbsolutePath());


                if (pubFile.exists()||priFile.exists()) {
                    code = "-1";
                    message = "file is exist";
                }else{
                    FileUtil.createFile(pubFilePath);
                    FileUtil.write(pubFilePath, publicKey);
                    FileUtil.createFile(privateFilePath);
                    FileUtil.write(privateFilePath, privateKey);
                }

            } catch (Exception e) {
                code = "-1";
                message = e.getMessage();
            }

            resultMap.put("code", code);

            resultMap.put("message", message);

            callbackContext.success(JsonUtil.toJson(resultMap));

        } else if ("getPublicKeyMethod".equals(action)) {
            String msg = args.getString(0);
            try {

                callbackContext.success("publicKey");

            } catch (Exception e) {
                code = "-1";
                message = e.getMessage();
            }
            resultMap.put("code", code);

            resultMap.put("message", message);

            callbackContext.success(JsonUtil.toJson(resultMap));

        } else if ("signMethod".equals(action)) {

            String msg = args.getString(0);
            Log.d(LOG_TAG, "signMethod-->MSG: " + msg);
            try {

                if (!pubFile.exists()) {
                    code = "-1";
                    message = "please init key";
                } else {
                    String publicKey = FileUtil.read(pubFilePath);
                    String privateKey = FileUtil.read(privateFilePath);
                    byte[] data = msg.getBytes("utf-8");
                    String sign = RSAUtil.sign(data, privateKey);
                    resultMap.put("sign", sign);
                    Log.d(LOG_TAG, "sign: " + sign);
                }


            } catch (Exception e) {
                code = "-1";
                message = e.getMessage();
            }

            resultMap.put("code", code);

            resultMap.put("message", message);

            callbackContext.success(JsonUtil.toJson(resultMap));

        } else if ("verifyMethod".equals(action)) {

            String msg = args.getString(0);

            String sign = args.getString(1);

            Log.d(LOG_TAG, "verifyMethod-->msg: " + msg);

            Log.d(LOG_TAG, "verifyMethod-->sign: " + sign);

            String publicKey = FileUtil.read(pubFilePath);

            try {

                byte[] data = msg.getBytes("utf-8");

                boolean status = RSAUtil.verify(data, publicKey, sign);

                if(status==false){
                    code = "-1";
                }

            } catch (Exception e) {
                code = "-1";
                message = e.getMessage();
            }
            resultMap.put("code", code);

            resultMap.put("message", message);

            callbackContext.success(JsonUtil.toJson(resultMap));

        }
        return true;
    }

}
