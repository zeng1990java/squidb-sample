package com.macbook.zxb.squidbmysample.modelspec;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.TableModelSpec;

/**
 * Created by zxb on 15/4/27.
 */
@TableModelSpec(className = "Person", tableName = "person")
public class PersonSpec {
    public String username;
    public int age;
    @ColumnSpec(name = "creationDate")
    public long birthday;
}
