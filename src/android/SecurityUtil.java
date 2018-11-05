package com.runchain.plugins.security;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    JSONObject resultObj = new JSONObject();

    File dataDirectory = context.getFilesDir();

   String filePath = FileUtil.getSDPath();

    File jsscFilePath = new File(filePath+"/"+"jssc");

    if(!jsscFilePath.exists()){
        jsscFilePath.mkdir();
    }
    String pubFilePath = jsscFilePath + "/";

    String privateFilePath =  jsscFilePath + "/";


    if ("genKeyPairMethod".equals(action)) {

      String username = args.getString(0);

      Log.d(LOG_TAG, "username: " + username);

      File pubFile = new File(pubFilePath + username + ".publickey");

      File priFile = new File(privateFilePath + username + ".privatekey");

      Log.d(LOG_TAG, "pubFilePath: " + pubFilePath);

      Log.d(LOG_TAG, "privateFilePath: " + privateFilePath);

      try {
        Map<String, Object> keyMap = RSAUtil.genKeyPair(username);

        String publicKey = RSAUtil.getPublicKey(keyMap);

        String privateKey = RSAUtil.getPrivateKey(keyMap);

        Log.d(LOG_TAG, "dataDirectory:" + dataDirectory.getAbsolutePath());


        if (pubFile.exists() || priFile.exists()) {
          code = "-1";
          message = "file is exist";
        } else {
          FileUtil.createFile(pubFile.getAbsolutePath());
          FileUtil.write(pubFile.getAbsolutePath(), publicKey);
          FileUtil.createFile(priFile.getAbsolutePath());
          FileUtil.write(priFile.getAbsolutePath(), privateKey);
        }

      } catch (Exception e) {
        code = "-1";
        message = e.getMessage();
      }

      resultObj.put("code", code);

      resultObj.put("message", message);

      callbackContext.success(resultObj);

    } else if ("getPublicKeyMethod".equals(action)) {
      String username = args.getString(0);
      try {
        String publicKey = FileUtil.read(pubFilePath + username + ".publickey");

        resultObj.put("publicKey", publicKey);

      } catch (Exception e) {
        code = "-1";
        message = e.getMessage();
      }
      resultObj.put("code", code);

      resultObj.put("message", message);

      callbackContext.success(resultObj);

    } else if ("signMethod".equals(action)) {

      String username = args.getString(0);

      String signData = args.getString(1);

      File pubFile = new File(pubFilePath + username + ".publickey");

      File priFile = new File(privateFilePath + username + ".privatekey");

      Log.d(LOG_TAG, "signMethod-->username: " + username);

      Log.d(LOG_TAG, "signMethod-->signData: " + signData);
      try {

        if (!pubFile.exists()) {
          code = "-1";
          message = "please init key";
        } else {
          String publicKey = FileUtil.read(pubFile.getAbsolutePath());
          String privateKey = FileUtil.read(priFile.getAbsolutePath());
          byte[] data = signData.getBytes("utf-8");
          String sign = RSAUtil.sign(data, privateKey);
          resultObj.put("sign", sign);
          Log.d(LOG_TAG, "sign: " + sign);
        }
      } catch (Exception e) {
        code = "-1";
        message = e.getMessage();
      }

      resultObj.put("code", code);

      resultObj.put("message", message);

      callbackContext.success(resultObj);

    } else if ("verifyMethod".equals(action)) {

      String username = args.getString(0);

      String signData = args.getString(1);

      String sign = args.getString(2);

      Log.d(LOG_TAG, "verifyMethod-->username: " + username);

      Log.d(LOG_TAG, "verifyMethod-->signData: " + signData);

      Log.d(LOG_TAG, "verifyMethod-->sign: " + sign);

      String publicKey = FileUtil.read(pubFilePath + username + ".publickey");

      try {

        byte[] data = signData.getBytes("utf-8");

        boolean status = RSAUtil.verify(data, publicKey, sign);

        if (status == false) {
          code = "-1";
        }

      } catch (Exception e) {
        code = "-1";
        message = e.getMessage();
      }
      resultObj.put("code", code);

      resultObj.put("message", message);

      callbackContext.success(resultObj);

    }
    return true;
  }

}
