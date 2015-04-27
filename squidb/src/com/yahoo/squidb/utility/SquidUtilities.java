/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache 2.0 License.
 * See the accompanying LICENSE file for terms.
 */
package com.yahoo.squidb.utility;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.yahoo.squidb.data.AbstractModel;
import com.yahoo.squidb.sql.Property;
import com.yahoo.squidb.sql.SqlTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

/**
 * Various utility functions for SquiDB
 */
public class SquidUtilities {

    /**
     * Reads a list of {@link Property properties} from a model class by reflection
     *
     * @param modelClass the model class
     * @return an array of Property objects
     */
    public static Property<?>[] getProperties(Class<? extends AbstractModel> modelClass) {
        try {
            return (Property<?>[]) modelClass.getField("PROPERTIES").get(null);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inspect a model class for its declared {@link SqlTable} by reflection
     *
     * @param modelClass the model class
     * @return the SqlTable declared for this model, if one exists
     */
    public static SqlTable<?> getSqlTable(Class<? extends AbstractModel> modelClass) {
        Field[] fields = modelClass.getFields();
        if (fields != null) {
            for (Field field : fields) {
                if (!SqlTable.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    continue;
                }
                try {
                    return (SqlTable<?>) field.get(null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    /**
     * Put an arbitrary object into a {@link ContentValues}
     *
     * @param target the ContentValues store
     * @param key the key to use
     * @param value the value to store
     */
    public static void putInto(ContentValues target, String key, Object value, boolean errorOnFail) {
        if (value == null) {
            target.putNull(key);
        } else if (value instanceof Boolean) {
            target.put(key, (Boolean) value);
        } else if (value instanceof Byte) {
            target.put(key, (Byte) value);
        } else if (value instanceof Double) {
            target.put(key, (Double) value);
        } else if (value instanceof Float) {
            target.put(key, (Float) value);
        } else if (value instanceof Integer) {
            target.put(key, (Integer) value);
        } else if (value instanceof Long) {
            target.put(key, (Long) value);
        } else if (value instanceof Short) {
            target.put(key, (Short) value);
        } else if (value instanceof String) {
            target.put(key, (String) value);
        } else if (value instanceof byte[]) {
            target.put(key, (byte[]) value);
        } else if (errorOnFail) {
            throw new UnsupportedOperationException("Could not handle type " + value.getClass());
        }
    }

    /**
     * A version of {@link Collection#addAll(Collection)} that works on varargs without calling Arrays.asList, which is
     * a performance and memory boost
     */
    public static <T> void addAll(Collection<T> collection, T... objects) {
        if (objects != null) {
            for (T obj : objects) {
                collection.add(obj);
            }
        }
    }

    // --- serialization

    /**
     * Copy database files to the given folder. Useful for debugging.
     *
     * @param folder the directory to copy files into
     */
    public static void copyDatabases(Context context, String folder) {
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        for (String db : context.databaseList()) {
            File dbFile = context.getDatabasePath(db);
            try {
                copyFile(dbFile, new File(folderFile.getAbsolutePath() + File.separator + db));
            } catch (Exception e) {
                Log.e("ERROR", "ERROR COPYING DB " + db, e);
            }
        }
    }

    /**
     * Copy a file from one place to another
     */
    private static void copyFile(File in, File out) throws Exception {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        try {
            copyStream(fis, fos);
        } catch (Exception e) {
            throw e;
        } finally {
            fis.close();
            fos.close();
        }
    }

    /**
     * Copy a stream from source to destination
     */
    private static void copyStream(InputStream source, OutputStream dest) throws IOException {
        int bytes;
        byte[] buffer;
        int BUFFER_SIZE = 1024;
        buffer = new byte[BUFFER_SIZE];
        while ((bytes = source.read(buffer)) != -1) {
            if (bytes == 0) {
                bytes = source.read();
                if (bytes < 0) {
                    break;
                }
                dest.write(bytes);
                dest.flush();
                continue;
            }

            dest.write(buffer, 0, bytes);
            dest.flush();
        }
    }
}
