/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signUpModeActive = true;
  TextView signInTextView;
  EditText usernameEditText;
  EditText passwordEditText;


  public void showUserList() {
    Intent intent =new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);
  }


  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
      signupClick(v);
    }
    return false;
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.signInTextView) {
      Button signUpButton = findViewById(R.id.signupButton);

      if (signUpModeActive) {
        signUpModeActive = false;
        signUpButton.setText("Login");
        signInTextView.setText("or, Sign Up");
      } else {
        signUpModeActive = true;
        signUpButton.setText("Sign up");
        signInTextView.setText("or, Login");
      }
    } else if (v.getId() == R.id.icon || v.getId() == R.id.layout){
      InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  public void signupClick(View view) {
    if (usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()) {
      Toast.makeText(this, "A username and a password are required.", Toast.LENGTH_SHORT).show();
    } else {
      if (signUpModeActive) {
        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Toast.makeText(MainActivity.this, "Welcome! You just signed up!", Toast.LENGTH_SHORT).show();
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      } else {
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user != null) {
              Toast.makeText(MainActivity.this, "Login!--OK!", Toast.LENGTH_SHORT).show();
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram");
    
    signInTextView = findViewById(R.id.signInTextView);
    signInTextView.setOnClickListener(this);

    usernameEditText = findViewById(R.id.usernameEditText);
    passwordEditText = findViewById(R.id.passwordEditText);
    passwordEditText.setOnKeyListener(this);

    ImageView icon = findViewById(R.id.icon);
    RelativeLayout layout = findViewById(R.id.layout);
    icon.setOnClickListener(this);
    layout.setOnClickListener(this);

    if (ParseUser.getCurrentUser() != null) {
      showUserList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}