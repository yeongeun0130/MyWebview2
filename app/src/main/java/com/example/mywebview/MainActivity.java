package com.example.mywebview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editUrl;
    WebView webView;
    Spinner spinner;
    ArrayList<String> searchDatas;
    ArrayAdapter<String> dataAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUrl = findViewById(R.id.editUrl);
        webView = findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());

        findViewById(R.id.btnClear).setOnClickListener(this);
        findViewById(R.id.btnGoNaver).setOnClickListener(this);
        findViewById(R.id.btnGoGoogle).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);

        spinner=findViewById(R.id.history);
        searchDatas=new ArrayList<String>();
        dataAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,searchDatas);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editUrl.setText(spinner.getItemAtPosition(position).toString());
                findViewById(R.id.btnGoNaver).performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setVisibility(View.GONE);

        editUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    spinner.setVisibility(View.VISIBLE);
                else
                    spinner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu1:
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Hello")
                        .setMessage("안녕하세요")
                        .setPositiveButton("확인", null)
                        .setIcon(R.drawable.trash)
                        .show();
                break;
            case R.id.menu2:
                View dlgView = View.inflate(this, R.layout.profile, null);
                AlertDialog.Builder dlg2 =new AlertDialog.Builder(this);
                dlg2.setTitle("안녕하세요")
                        .setMessage("내용")
                        .setView(dlgView)
                        .setNegativeButton("취소",null)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText =dlgView.findViewById(R.id.dlg_nickname);
                                String nickname = editText.getText().toString();
                                if(nickname.length()>0)
                                    setTitle(nickname+"의 검색기");
                            }
                        })
                        .show();
                break;
            case R.id.menu3:
                finish();
                break;
            case R.id.menu4:
                Intent intent = new Intent(MainActivity.this,SpinnerActivity.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        switch (v.getId()) {
            case R.id.btnClear:
                editUrl.setText("");
                break;

            case R.id.btnGoNaver:
                webView.loadUrl("http://search.naver.com/search.naver?query=" + editUrl.getText().toString());
                addHistoryData(editUrl.getText().toString());
                break;

            case R.id.btnGoGoogle:
                webView.loadUrl("http://www.google.com/search?q=" + editUrl.getText().toString());
                addHistoryData(editUrl.getText().toString());
                break;

            case R.id.btnBack:
                webView.goBack();
                break;

        }
        dataAdapter.notifyDataSetChanged();
    }

    private void addHistoryData(String data) {
        if(!searchDatas.contains(data))
            searchDatas.add(0,data);
        if(searchDatas.size()>5)
            searchDatas.remove(5);
    }
}