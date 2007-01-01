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

import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.exception.S2JCRCommonException;
import org.seasar.jcr.impl.JCRDtoDescImpl;

public class UpdateCommand extends AbstractAutoJCRXPathCommand {

    public UpdateCommand(S2JCRSessionFactory sessionFactory, Method method,
            Class raoClass, JcrConverter jcrConverter) {
        super(sessionFactory, method, raoClass, jcrConverter);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.JcrCommand#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws RepositoryException {
        
        JCRDtoDesc dtoDesc = new JCRDtoDescImpl(args[0]);

        Session session = getSession();
        
        try {
            
            Query query = session.getWorkspace().getQueryManager().createQuery("//" + getPath(),
                    Query.XPATH);

            QueryResult queryResult = query.execute();
            NodeIterator queryResultNodeIterator = queryResult.getNodes();
            
            int i = 0;
            Node[] cloneNodes = new Node[(int)queryResultNodeIterator.getSize()];
            
            while (queryResultNodeIterator.hasNext()) {
                
                Node node = queryResultNodeIterator.nextNode();
                node.checkout();
                jcrConverter.convertDtoToNode(node, dtoDesc);
                cloneNodes[i] = node;
                i++;
            }           
        
            session.save();           
            checkin(cloneNodes);
            
        } catch (Throwable e) {
            
            throw new S2JCRCommonException("EJCR0001");
            
        } finally {
            
            session.logout();
            
        }
        
        return null;

    }

    /**
     * 
     */
    private void checkin(Node[] nodes) throws Throwable {

        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.checkin();
        }
    }

}
