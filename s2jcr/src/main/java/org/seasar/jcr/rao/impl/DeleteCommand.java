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
package org.seasar.jcr.rao.impl;

import java.lang.reflect.Method;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.exception.S2JCRCommonException;

public class DeleteCommand extends AbstractAutoJCRXPathCommand {

    public DeleteCommand(S2JCRSessionFactory sessionFactory, Method method,
            Class raoClass, JcrConverter jcrConverter) {
        super(sessionFactory, method, raoClass, jcrConverter);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.JcrCommand#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws RepositoryException {
        
        Session session = getSession();
        
        long nodeCount = 0;
        
        try {
            
            Query query = session.getWorkspace().getQueryManager().createQuery(
                    S2JCRConstants.XPATH_PREFIX + getPath(),
                    Query.XPATH);

            QueryResult queryResult = query.execute();

            NodeIterator queryResultNodeIterator = queryResult.getNodes();
            nodeCount = queryResultNodeIterator.getSize();
            while (queryResultNodeIterator.hasNext()) {
                
                Node node = queryResultNodeIterator.nextNode();
                node.remove();
            }           
        
            session.save();           
            
        } catch (Throwable e) {
            
            throw new S2JCRCommonException("EJCR0001", e);
            
        } finally {
            
            session.logout();
            
        }
        
        return Long.valueOf(nodeCount);

    }

}
