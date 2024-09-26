package io.summernova.admin.core.mybatis;

import io.summernova.admin.core.domain.field.Many2One;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author gongshuiwen
 */
public class Many2OneTypeHandler extends BaseTypeHandler<Many2One<?>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Many2One<?> parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.getId());
    }

    @Override
    public Many2One<?> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Many2One.ofId(rs.getLong(columnName));
    }

    @Override
    public Many2One<?> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Many2One.ofId(rs.getLong(columnIndex));
    }

    @Override
    public Many2One<?> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Many2One.ofId(cs.getLong(columnIndex));
    }
}