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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.exception.S2JCRCommonException;
import org.seasar.jcr.util.NodeUtil;

public class GetCommand extends AbstractAutoJCRXPathCommand {

    public GetCommand(S2JCRSessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.JcrCommand#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws RepositoryException {
        
        JCRDtoDesc dtoDesc = new JCRDtoDescImpl(args[0]);

        //TODO annotation readerで読めるようにもする(非CoC対応)
        String nodePath = dtoDesc.getPath();
        if (nodePath == null) {
            throw new S2JCRCommonException("EJCR0002");
        }

        List returnList = new ArrayList();
        Object returnObject = null;
        
        try {
            
            Query query = getQueryManager().createQuery("//" + nodePath,
                    Query.XPATH);

            //TODO getRowsの場合との比較 in jackrabbit
            QueryResult queryResult = query.execute();
            NodeIterator queryResultNodeIterator = queryResult.getNodes();
            
            if ( queryResultNodeIterator.getSize() == 0) {
                throw new S2JCRCommonException("EJCR0002");
            }
            
            returnObject = dtoDesc.getDtoClass().newInstance();

            while (queryResultNodeIterator.hasNext()) {

                Node node = queryResultNodeIterator.nextNode();                    
                NodeUtil.copyProperties(node, returnObject);
                       
                returnList.add(returnObject);
            }           
            
        } catch (Throwable e) {
            
            throw new S2JCRCommonException("EJCR0001");
            
        } finally {
            
            getSessionFactory().getSession().logout();
            
        }
        
        return returnList;

    }

}
