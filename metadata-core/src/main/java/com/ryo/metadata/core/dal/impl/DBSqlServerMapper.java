package com.ryo.metadata.core.dal.impl;

import com.ryo.medata.util.util.PathUtil;
import com.ryo.metadata.core.constant.DBClassNameConstant;
import com.ryo.metadata.core.dal.DBMapper;
import com.ryo.metadata.core.domain.MetaField;
import com.ryo.metadata.core.domain.MetaModel;
import com.ryo.metadata.core.util.DalUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * SQL Server 数据库访问层
 * [](http://www.cnblogs.com/songxingzhu/p/5849029.html)
 * Created by bbhou on 2017/7/31.
 */
public class DBSqlServerMapper implements DBMapper {

    private static final String FILE_PATH = PathUtil.getRootPath()+"/metadata-core/src/main/resources/jdbc_sqlserver.properties";

    static {
        try {
            Class.forName(DBClassNameConstant.SQL_SERVER);
//            PropertiesIOUtil.loadProperties();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<MetaModel> selectAllTables() {
        List<MetaModel> metaModelList = new LinkedList<>();

        String sql = "SELECT _obj.Name, convert(varchar(100), _ext.value) FROM SysObjects AS _obj " +
                "left join sys.extended_properties AS _ext " +
                "on _obj.id = _ext.major_id and _ext.minor_id=0" +
                "Where _obj.XType='U'";
        ResultSet resultSet = DalUtil.query(sql);
        try {
            while(resultSet.next()) {
                String tableName = resultSet.getString(1);  //表名称
                String comment = resultSet.getString(2);    //注释
                MetaModel metaModel = new MetaModel();
                metaModel.setName(tableName);
                metaModel.setDescription(comment);
                metaModel.setCreateTime(new Date());
                metaModel.setUpdateTime(new Date());
                metaModelList.add(metaModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metaModelList;
    }

    @Override
    public List<MetaField> selectAllFields(String tableName) {
        List<MetaField> metaFieldList = new LinkedList<>();
        String sql = "SELECT _col.TABLE_SCHEMA, _col.COLUMN_NAME, _col.IS_NULLABLE, _col.DATA_TYPE, convert(varchar(100), _ext.value) " +
                "FROM SysObjects AS _sys LEFT JOIN sys.extended_properties AS _ext " +
                "ON _sys.id=_ext.major_id, INFORMATION_SCHEMA.columns AS _col WHERE _col.TABLE_NAME='"+tableName+"' and _sys.name='"+tableName+"'";
        ResultSet resultSet = DalUtil.query(sql);    //指定需要的列信息
        try {
            while(resultSet.next()) {
                String dbObjectName = resultSet.getString(1)+"."+tableName;
                String columnName = resultSet.getString(2);
                String isNullableStr = resultSet.getString(3);
                String dataType = resultSet.getString(4);
                String comment = resultSet.getString(5);

                MetaField metaField = new MetaField();
                metaField.setName(columnName);
                metaField.setDataType(dataType);
                metaField.setNullable(getBoolVal(isNullableStr));
                metaField.setDescription(comment);
                metaField.setDbObjectName(dbObjectName);
                metaField.setCreateTime(new Date());
                metaField.setUpdateTime(new Date());
                metaFieldList.add(metaField);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return metaFieldList;
    }

    /**
     * 获取对应的bool值
     * @param string
     * @return
     */
    private  boolean getBoolVal(String string) {
        if("YES".equals(string)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
//        DBMapper dbMapper = new DBSqlServerMapper();
//        List<MetaField> metaFieldList = dbMapper.selectAllFields("meta_field");
//        System.out.println(metaFieldList);
        System.out.println(PathUtil.getPath());
        System.out.println(PathUtil.getRootPath());

//        /Users/houbinbin/IT/fork/metadata/metadata-core/src/main/resources/jdbc_sqlserver.properties
    }
}
