package com.hisalari.db.interfaces;

import com.hisalari.db.DataBase;

import java.sql.SQLException;

public interface DataSourcePool {

    /**
     * 获取连接池
     * @param dataBase
     * @return
     */
    public Object getDataSourceFromFactory(DataBase dataBase);

    /**
     * 创建连接池
     * @param dataBase
     * @return
     */
    public Object createDataSource(DataBase dataBase) throws SQLException;


}
