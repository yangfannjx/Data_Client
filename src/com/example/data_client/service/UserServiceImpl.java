package com.example.data_client.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.example.data_client.LoginActivity;

public class UserServiceImpl implements UserService {

	public final static String TAG = "YANG";

	@Override
	public void userLogin(String loginName, String loginPwd) throws Exception {

		Log.i(TAG, loginName);
		Log.i(TAG, loginPwd);

		/**
		 * GET请求方式
		 */
/*		HttpClient client = new DefaultHttpClient();
		String uri = "http://192.168.1.125:8080/yangfan/Login.do?loginName="
				+ loginName + "&loginPwd=" + loginPwd;
		HttpGet get = new HttpGet(uri);
		HttpResponse response = client.execute(get);*/
		
		/**
		 * POSE请求方式
		 */
		HttpParams params = new BasicHttpParams();
		//设置请求字符集
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		//设置客户端和服务器的连接超时时间--抛出ConnectTimeoutException
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		//设置服务器的响应超时时间--抛出SocketTimeoutException
		HttpConnectionParams.setSoTimeout(params, 3000);
		SchemeRegistry schreg = new SchemeRegistry();
		//注册访问服务器的方式
		schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schreg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 433));
		
		ClientConnectionManager conman = new ThreadSafeClientConnManager(params, schreg);
		HttpClient client = new DefaultHttpClient(conman, params);
//		HttpClient client = new DefaultHttpClient();
		String uri = "http://192.168.1.125:8080/yangfan/Login.do";
		HttpPost post = new HttpPost(uri);
		NameValuePair paramloginName = new BasicNameValuePair("loginName", loginName);
		NameValuePair paramloginPwd = new BasicNameValuePair("loginPwd", loginPwd);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(paramloginName);
		parameters.add(paramloginPwd);
		post.setEntity(new UrlEncodedFormEntity(parameters,HTTP.UTF_8));
		HttpResponse response = client.execute(post);
		
		/**
		 *服务器返回
		 * 
		 */
		int statusCode = response.getStatusLine().getStatusCode();
		Log.i(TAG, statusCode+"");
		
		if(statusCode != HttpStatus.SC_OK){
			throw new ServiceRulesException(LoginActivity.MSG_SERVER_ERROR);
		}
		
		String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		
		if(result.equals("success")){
			
		}else{
			throw new ServiceRulesException(LoginActivity.MSG_LOGIN_FAILED);
		}
	}

}
