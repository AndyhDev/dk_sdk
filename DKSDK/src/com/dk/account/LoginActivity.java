package com.dk.account;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dk.style.dKColor;
import com.dk.sdk.R;
import com.dk.util.network.LoginListener;
import com.dk.util.network.dKLogin;
import com.dk.util.network.dKSession;

public class LoginActivity extends Activity implements OnClickListener, LoginListener{
	private ImageButton loginBnt;
	private TextView userName;
	private TextView password;
	private String user;
	private String passw;
	private Handler handler = new Handler();
	
	public static final String DK_USERNAME = "username";
	public static final String DK_PASSWORD = "password";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.dk_account_login);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(dKColor.RED));
		
		bar.setDisplayShowHomeEnabled(false);
		bar.setTitle(getString(R.string.dklogin));
		
		loginBnt = (ImageButton) findViewById(R.id.dk_account_login);
		loginBnt.setOnClickListener(this);
		
		userName = (TextView) findViewById(R.id.dk_account_username);
		userName.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {}
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){
	        	userName.setError(null);
	        }
	    });
		
		password = (TextView) findViewById(R.id.dk_account_password);
		password.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {}
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){
	        	password.setError(null);
	        }
	    });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.dk_login, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		int id = item.getItemId();
		if(id == R.id.action_register){
        	Uri uri = Uri.parse("https://dk-force.de/register");
        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        	startActivity(intent);
            return true;
		}else{
			return super.onOptionsItemSelected(item);
	    }
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.dk_account_login){
			boolean error = false;
			user = userName.getText().toString();
			if(user.length() == 0){
				error = true;
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				userName.startAnimation(shake);
				userName.setError(getString(R.string.dk_no_username));
			}
			passw = password.getText().toString();
			if(passw.length() == 0){
				error = true;
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				password.startAnimation(shake);
				password.setError(getString(R.string.dk_no_password));
			}
			if(error){
				return;
			}
			loginBnt.setEnabled(false);
			password.setEnabled(false);
			userName.setEnabled(false);
			setProgressBarIndeterminateVisibility(true);
			dKLogin login = new dKLogin(this, user, passw);
			login.setListener(this);
			login.login();
		}
	}

	@Override
	public void onLoginSuccess(dKSession session) {
		setProgressBarIndeterminateVisibility(false);
		final Dialog dlg = new Dialog(this);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dlg.setContentView(R.layout.dk_account_ok);
		dlg.show();
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				dlg.dismiss();
				Intent intent = new Intent();
				intent.putExtra(DK_USERNAME, user);
				intent.putExtra(DK_PASSWORD, passw);
				setResult(RESULT_OK, intent);
				finish();
			}
		}, 1500);
	}

	@Override
	public void onLoginError(int code) {
		setProgressBarIndeterminateVisibility(false);
		loginBnt.setEnabled(true);
		password.setEnabled(true);
		userName.setEnabled(true);
		
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		userName.startAnimation(shake);
		userName.setError(getString(R.string.dk_wrong_username));


		password.startAnimation(shake);
		password.setError(getString(R.string.dk_wrong_username));
	}
}
