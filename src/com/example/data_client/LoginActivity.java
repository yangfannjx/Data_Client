package com.example.data_client;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.data_client.service.ServiceRulesException;
import com.example.data_client.service.UserService;
import com.example.data_client.service.UserServiceImpl;

public class LoginActivity extends Activity {

	private EditText textLoginName;
	private EditText textLoginPwd;
	private Button btnLogin;
	private Button btnReset;
	private static ProgressDialog dialog;

	private UserService userService = new UserServiceImpl();

	private static final int FLAG_LOGIN_SUCCESS = 1;
	private static final String MSG_LOGIN_ERROR = "登陆出错";
	private static final String MSG_LOGIN_SUCCESS = "登陆成功";
	public static final String MSG_LOGIN_FAILED = "登陆名或密码错误";
	public static final String MSG_SERVER_ERROR = "请求服务器错误";
	public static final String MSG_REQUEST_TIMEOUT = "请求服务器超时";
	public static final String MSG_RESPONSE_TIMEOUT = "服务器响应超时";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		init();

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String loginName = textLoginName.getText().toString();
				final String loginPwd = textLoginPwd.getText().toString();
				Toast.makeText(LoginActivity.this,
						loginName + "---" + loginPwd, Toast.LENGTH_SHORT)
						.show();
				
				if(dialog == null){
					dialog = new ProgressDialog(LoginActivity.this);
				}
				
				dialog.setTitle("请等待");
				dialog.setMessage("登陆中……");
				dialog.setCancelable(false);
				dialog.show();

				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							userService.userLogin(loginName, loginPwd);
							handler.sendEmptyMessage(FLAG_LOGIN_SUCCESS);

						}catch (ConnectTimeoutException e){
							Message msg = handler.obtainMessage();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg", MSG_REQUEST_TIMEOUT);
							msg.setData(data);
							handler.sendMessage(msg);
						}catch (SocketTimeoutException e){
							Message msg = handler.obtainMessage();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg", MSG_RESPONSE_TIMEOUT);
							msg.setData(data);
							handler.sendMessage(msg);
						}catch (ServiceRulesException e) {// 业务异常
							Message msg = handler.obtainMessage();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg", e.getMessage());
							msg.setData(data);
							handler.sendMessage(msg);
						} 
						catch (Exception e) {
							Message msg = handler.obtainMessage();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg", MSG_LOGIN_ERROR);
							msg.setData(data);
							handler.sendMessage(msg);
						}

					}
				}).start();
			}
		});

		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				textLoginName.setText("");
				textLoginPwd.setText("");
			}
		});
	}

	private void init() {
		textLoginName = (EditText) findViewById(R.id.text_login_name);
		textLoginPwd = (EditText) findViewById(R.id.text_login_pwd);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnReset = (Button) findViewById(R.id.btn_reset);
	}

	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private static class IHandler extends Handler {
		private final WeakReference<Activity> mActivity;

		public IHandler(LoginActivity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if(dialog != null){
				dialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				String errorMsg = (String) msg.getData().getSerializable(
						"ErrorMsg");
				((LoginActivity) mActivity.get()).showTip(errorMsg);
				break;
			case FLAG_LOGIN_SUCCESS:
				((LoginActivity) mActivity.get()).showTip(MSG_LOGIN_SUCCESS);
				break;

			default:
				break;
			}
		}
	}

	private IHandler handler = new IHandler(this);
}
