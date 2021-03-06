package com.ryo.metadata.core.dal.impl;

import com.ryo.metadata.core.dal.JdbcMapper;
import com.ryo.metadata.core.util.vo.JdbcVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

/**
 *
 * @author bbhou
 * @date 2017/8/1
 */
public abstract class AbstractJdbcMapper implements JdbcMapper {

    private static final Logger LOGGER = LogManager.getLogger(AbstractJdbcMapper.class);

    protected JdbcVo jdbcVo;

    public AbstractJdbcMapper(JdbcVo jdbcVo) {
        this.jdbcVo = jdbcVo;
    }

    /**
     * 获取数据库连接
     * @return
     */
    protected abstract Connection getConnection();

    @Override
    public ResultSet query(String querySql) {
        ResultSet rs = null;
        Connection connection = getConnection();
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            rs = stmt.executeQuery(querySql);
        } catch (Exception e) {
            LOGGER.error("query meet ex: "+e, e);
        }
        return rs;
    }

    @Override
    public void execute(String sql) {
        try(Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            LOGGER.error("execute meet ex: "+e, e);
        }
    }

    @Override
    public void executeBatch(List<String> stringList) {
        LOGGER.info("executeBatch with sql: "+ stringList);

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            for (String sql : stringList) {
                statement.addBatch(sql);
                LOGGER.debug(sql);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("executeBatch meet ex: " + e, e);
        }

    }

    @Override
    public void executeBatchTx(List<String> stringList) throws SQLException {
        LOGGER.info("executeBatchTx with sql: "+ stringList);
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            for(String sql : stringList) {
                statement.addBatch(sql);
                LOGGER.debug(sql);
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("executeBatchTx meet ex: "+e, e);
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public DatabaseMetaData metaData() throws SQLException {
        return getConnection().getMetaData();
    }
}
