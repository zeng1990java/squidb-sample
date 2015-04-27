/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache 2.0 License.
 * See the accompanying LICENSE file for terms.
 */
package com.yahoo.squidb.sql;

import com.yahoo.squidb.data.AbstractModel;
import com.yahoo.squidb.utility.SquidUtilities;

import java.util.Arrays;
import java.util.List;

/**
 * A database object from which a select operation can be performed, such as a {@link Table} or {@link View}
 */
public abstract class SqlTable<T extends AbstractModel> extends DBObject<SqlTable<T>> {

    protected final Class<? extends T> modelClass;

    /**
     * @param expression the string-literal representation of this SqlTable
     */
    protected SqlTable(Class<? extends T> modelClass, String expression) {
        super(expression);
        this.modelClass = modelClass;
    }

    /**
     * @param expression the string-literal representation of this SqlTable
     * @param qualifier the string-literal representation of a qualifying object, e.g. a database name
     */
    protected SqlTable(Class<? extends T> modelClass, String expression, String qualifier) {
        super(expression, qualifier);
        this.modelClass = modelClass;
    }

    /**
     * @return the model class represented by this table
     */
    public Class<? extends T> getModelClass() {
        return modelClass;
    }

    /**
     * Clone the given {@link Field fields} with this object's name as their qualifier. This is useful for selecting
     * from views, subqueries, or aliased tables.
     *
     * @param fields the fields to clone
     * @return the given fields cloned and with this object as their qualifier
     */
    public Field<?>[] qualifyFields(Field<?>... fields) {
        if (fields == null) {
            return null;
        }

        return qualifyFields(Arrays.asList(fields));
    }

    /**
     * Clone the given {@link Field fields} with this object's name as their qualifier. This is useful for selecting
     * from views, subqueries, or aliased tables.
     *
     * @param fields the fields to clone
     * @return the given fields cloned and with this object as their qualifier
     */
    public Field<?>[] qualifyFields(List<Field<?>> fields) {
        if (fields == null) {
            return null;
        }
        Field<?>[] result = new Field<?>[fields.size()];
        int i = 0;
        for (Field<?> field : fields) {
            result[i] = qualifyField(field);
            i++;
        }

        return result;
    }

    /**
     * Clone the given {@link Field} with this object's name as its qualifier. This is useful for selecting
     * from views, subqueries, or aliased tables.
     *
     * @param field the field to clone
     * @return a clone of the given field with this object as its qualifier
     */
    @SuppressWarnings("unchecked")
    public <F extends Field<?>> F qualifyField(F field) {
        if (field instanceof Property<?>) {
            return (F) ((Property<?>) field).asSelectionFromTable(this, field.getName());
        } else {
            return (F) Field.field(field.getName(), getName());
        }
    }

    /**
     * @return the fields associated to this data source
     */
    protected Field<?>[] allFields() {
        if (modelClass == null) {
            return new Field<?>[0];
        }
        return SquidUtilities.getProperties(modelClass);
    }

}
