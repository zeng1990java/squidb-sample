package com.macbook.zxb.squidbmysample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.macbook.zxb.squidbmysample.datas.LoginUser;
import com.macbook.zxb.squidbmysample.dbs.UserDatabase;
import com.macbook.zxb.squidbmysample.modelspec.Person;
import com.yahoo.squidb.data.DatabaseDao;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;

import java.util.Date;
import java.util.Random;


public class UserActivity extends ActionBarActivity {

    final static String USERNAME = "username";

    public static void start(Activity activity, String username){
        Intent i = new Intent(activity, UserActivity.class);
        i.putExtra(USERNAME, username);
        activity.startActivity(i);
    }

    EditText mUsernameView;
    DatabaseDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mUsernameView = (EditText) findViewById(R.id.username);
        LoginUser.getInstance().setUsername(getIntent().getStringExtra(USERNAME));
        UserDatabase db = new UserDatabase(this);
        dao = new DatabaseDao(db);
    }

    public void save(View v){
        String username = mUsernameView.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            toast("username cann't be empty");
            return;
        }

        Person p = new Person();
        p.setUsername(username);
        p.setAge(new Random().nextInt(50));
        p.setBirthday(System.currentTimeMillis());

        dao.persist(p);
    }

    public void query(View v){
        SquidCursor<Person> voters = dao.query(Person.class, Query.select());
        toast("count="+voters.getCursor().getCount());
    }

    void toast(CharSequence text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
