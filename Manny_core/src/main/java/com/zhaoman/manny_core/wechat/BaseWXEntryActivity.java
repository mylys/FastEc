package com.zhaoman.manny_core.wechat;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.zhaoman.manny_core.net.RestClient;
import com.zhaoman.manny_core.net.callback.IError;
import com.zhaoman.manny_core.net.callback.IFailure;
import com.zhaoman.manny_core.net.callback.ISuccess;

/**
 * Author:zhaoman
 * Date:2018/11/13
 * Description:
 */
public abstract class BaseWXEntryActivity extends BaseWXActivity {
    //用户登录成功后回调
    protected abstract void onSignInSuccess(String userInfo);

    //微信发送请求到第三方应用后的回调
    @Override
    public void onReq(BaseReq baseReq) {

    }

    //第三方应用发送请求到微信后的回调
    @Override
    public void onResp(BaseResp baseResp) {
        final String code= ((SendAuth.Resp)baseResp).code;
        final StringBuilder authUrl = new StringBuilder();
        authUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                .append(MannyWeChat.APP_ID)
                .append("&secret=")
                .append(MannyWeChat.SECRET)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");
        Log.d("TAG",authUrl.toString());

    }


    private void getAuth(String authUrl){

        RestClient.builder()
                .url(authUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject authObj=JSON.parseObject(response);
                        final String accessToken=authObj.getString("access_token");
                        final String openId=authObj.getString("openid");

                        final StringBuilder userInfoUrl = new StringBuilder();
                        userInfoUrl
                                .append("https://api.weixin.qq.com/sns/userinfo?access_token=")
                                .append(accessToken)
                                .append("&openid=")
                                .append(openId)
                                .append("&lang=")
                                .append("zh_CN");

                        Log.d("TAG",userInfoUrl.toString());

                    }
                })
                .build().get();

    }




    private void getuserInfo(String userInfoUrl){
        RestClient.builder()
                .url(userInfoUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        onSignInSuccess(response);
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {

                    }
                })
                .build()
                .get();

    }
}
