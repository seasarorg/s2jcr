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

import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.exception.S2JCRCommonException;
import org.seasar.jcr.util.ValueConverter;

public class UpdateCommand extends AbstractAutoJCRXPathCommand {

    public UpdateCommand(S2JCRSessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.JcrCommand#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws RepositoryException {
        
        JCRDtoDesc dtoDesc = new JCRDtoDescImpl(args[0]);

        String nodePath = dtoDesc.getPath();
        if (nodePath == null) {
            throw new S2JCRCommonException("EJCR0002");
        }

        Session session = null;
        
        try {
            
            Query query = getQueryManager().createQuery("//" + nodePath,
                    Query.XPATH);

            QueryResult queryResult = query.execute();
            NodeIterator queryResultNodeIterator = queryResult.getNodes();
            
            if ( queryResultNodeIterator.getSize() == 0) {
                throw new S2JCRCommonException("EJCR0002");
            }
            
            while (queryResultNodeIterator.hasNext()) {

                Node node = queryResultNodeIterator.nextNode();                    

                for (Iterator ite = dtoDesc.getFieldValueMap().keySet().iterator();ite.hasNext();) {
                    String propertyName = (String) ite.next();
                    if (dtoDesc.isPropertyField(propertyName)) {
                            
                        Object propertyValue = dtoDesc.getFieldValueMap().get(propertyName);
                        node.setProperty(propertyName, ValueConverter.convert(propertyValue));                        
                       
                    }
                }
             }
            
            session.save();           
            
        } catch (Throwable e) {
            
            throw new S2JCRCommonException("EJCR0001");
            
        } finally {
            
            session.logout();
            
        }
        
        return null;

    }

}
