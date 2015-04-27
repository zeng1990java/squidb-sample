package com.macbook.zxb.squidbmysample.dbs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.macbook.zxb.squidbmysample.datas.LoginUser;
import com.macbook.zxb.squidbmysample.modelspec.Person;
import com.yahoo.squidb.data.AbstractDatabase;
import com.yahoo.squidb.sql.Table;

/**
 * Created by zxb on 15/4/27.
 */
public class UserDatabase extends AbstractDatabase{
    /**
     * Create a new AbstractDatabase
     *
     * @param context the Context, must not be null
     */
    public UserDatabase(Context context) {
        super(context);
    }

    @Override
    protected String getName() {
        return LoginUser.getInstance().getUsername()+".db";
    }

    @Override
    protected int getVersion() {
        return 1;
    }

    @Override
    protected Table[] getTables() {
        return new Table[]{
                Person.TABLE
        };
    }

    @Override
    protected void onTablesCreated(SQLiteDatabase db) {

    }

    @Override
    protected boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return false;
    }
}
