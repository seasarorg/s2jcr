/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.jcr.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.jackrabbit.core.state.db.SimpleDbPersistenceManager;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 *
 */
public class S2SimpleDbPersistenceManager extends SimpleDbPersistenceManager 
    implements JcrDbPersistenceManager {

    protected DataSource dataSource;
    
    public S2SimpleDbPersistenceManager() {
        S2Container container = SingletonS2ContainerFactory.getContainer();
        dataSource = (DataSource)container.getComponent("dataSource");
    }

    protected void initConnection() throws Exception {
        con = getConnection();
    }

    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        
        return DataSourceUtil.getConnection(dataSource);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.persistence.JcrDbPersistenceManager#setDataSource(javax.sql.DataSource)
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
