package com.example.data_client;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	private EditText textLoginName;
	private EditText textLoginPwd;
	private Button btnLogin;
	private Button btnReset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		init();

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String loginName = textLoginName.getText().toString();
				String loginPwd = textLoginPwd.getText().toString();
				Toast.makeText(LoginActivity.this,
						loginName + "---" + loginPwd, Toast.LENGTH_SHORT)
						.show();
			}
		});

		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
}
