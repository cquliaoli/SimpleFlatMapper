package org.simpleflatmapper.jdbc.impl;

import org.simpleflatmapper.jdbc.MultiIndexFieldMapper;
import org.simpleflatmapper.jdbc.QueryBinder;
import org.simpleflatmapper.jdbc.QueryPreparer;
import org.simpleflatmapper.jdbc.named.NamedSqlQuery;
import org.simpleflatmapper.map.Mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MultiIndexQueryPreparer<T> implements QueryPreparer<T> {


    private final NamedSqlQuery query;
    private final MultiIndexFieldMapper<T>[] multiIndexFieldMappers;
    private final String[] generatedKeys;

    public MultiIndexQueryPreparer(NamedSqlQuery query, MultiIndexFieldMapper<T>[] multiIndexFieldMappers, String[] generatedKeys) {
        this.query = query;
        this.multiIndexFieldMappers = multiIndexFieldMappers;
        this.generatedKeys = generatedKeys;
    }

    @Override
    public QueryBinder<T> prepare(Connection connection) throws SQLException {
        return new MultiIndexQueryBinder<T>(query, multiIndexFieldMappers, generatedKeys, connection);
    }

    @Override
    public PreparedStatement prepareStatement(Connection connection) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mapper<T, PreparedStatement> mapper() {
        throw new UnsupportedOperationException();
    }
}
