/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.fragmentcommunicate;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity
    implements SimpleFragment.OnFragmentInteractionListener{

    private Button mButton;
    private Button mPrevButton;
    private Boolean isFragmentDisplayed = false;
    // key used to save the isFragmentDisplayed value over configuration changes
    static final String STATE_FRAGMENT = "state_of_fragment";
    // default choice is 2 (none)
    private int mRadioButtonChoice = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mButton = findViewById(R.id.open_button);
        mPrevButton = findViewById(R.id.previous_button);
        // on config change, onCreate is called to redraw the UI. In this case, we need to
        // check the saved value of isFragmentDisplayed so that
        // the appropriate button label is applied
        if(savedInstanceState != null) {
            isFragmentDisplayed = savedInstanceState.getBoolean(STATE_FRAGMENT);
            if(isFragmentDisplayed) {
                mButton.setText(R.string.close);
            }
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFragmentDisplayed) {
                    displayFragment();
                } else {
                    closeFragment();
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainActivity();
            }
        });
    }

    public void displayFragment() {
        SimpleFragment simpleFragment = SimpleFragment.newInstance(mRadioButtonChoice);
        // get the FragmentManager and start a transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //  add the simple fragment
        fragmentTransaction.add(R.id.fragment_container,
                simpleFragment).commit();
        // update button text
        mButton.setText(R.string.close);
        // set flag to indicate fragment is open.
        isFragmentDisplayed = true;
    }

    public void closeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // check to see if fragment
        SimpleFragment simpleFragment = (SimpleFragment) fragmentManager
                .findFragmentById(R.id.fragment_container);

        if(simpleFragment != null) {
            // create and commit a remove transaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(simpleFragment).commit();
            // update button text
            mButton.setText(R.string.open);
            // set flag to indicate fragment is closed.
            isFragmentDisplayed = false;


        }
    }

    private void returnToMainActivity() {
        finish();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // save the state of the fragment (true = open, false = closed)
        savedInstanceState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRadioButtonChoice(int choice) {
        mRadioButtonChoice = choice;
        String toastMsg = "Choice is " + Integer.toString(choice);
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

}
