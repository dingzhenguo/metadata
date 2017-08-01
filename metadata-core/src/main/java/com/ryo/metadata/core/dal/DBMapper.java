package com.ryo.metadata.core.dal;


import com.ryo.metadata.core.domain.MetaField;
import com.ryo.metadata.core.domain.MetaModel;

import java.util.List;

/**
 * 数据库访问层
 * Created by bbhou on 2017/7/31.
 */
public interface DBMapper {

    //1. 查询(表名称，字段类型，字段注释)
    //2. 执行SQL(数据入库)

    /**
     * 查询所有得标信息
     * @return
     */
    List<MetaModel> selectAllTables();

    /**
     * 查询当前表的所有字段信息
     * @param tableName 表名称
     * @return
     */
    List<MetaField> selectAllFields(String tableName);

}