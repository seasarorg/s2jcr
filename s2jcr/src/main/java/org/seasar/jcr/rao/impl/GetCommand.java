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
import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.exception.S2JCRCommonException;
import org.seasar.jcr.impl.JCRCommandDescImpl;

public class GetCommand extends AbstractAutoJCRXPathCommand {

    private static final ArrayList CHECKLIST = new ArrayList();
    
    public GetCommand(S2JCRSessionFactory sessionFactory, Method method,
            Class raoClass, JcrConverter jcrConverter) {
        super(sessionFactory, method, raoClass, jcrConverter);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.JcrCommand#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws RepositoryException {
        
        JCRCommandDesc cmdDesc = new JCRCommandDescImpl(getMethod(), args[0], getTargetDtoClass());

        List returnList = new ArrayList();
        
        Session session = getSession();
        
        try {
            
            String nodePath = S2JCRConstants.XPATH_PREFIX + getPath();
            if (cmdDesc.includeDtoParam()) {
                String xpath = cmdDesc.createXPath();
                nodePath = nodePath + xpath;
            }
            
            QueryManager qm = session.getWorkspace().getQueryManager();
            Query query = qm.createQuery(nodePath, Query.XPATH);
            QueryResult queryResult = query.execute();

            returnList = jcrConverter.convertQResultToDto(queryResult, cmdDesc.getJCRDtoDesc());
            
            
        } catch (Throwable e) {
            
            throw new S2JCRCommonException("EJCR0001",e);
            
        } finally {
            
            session.logout();
            
        }
        
        return returnValue(returnList);

    }

    /**
     * @param returnList
     * @return
     */
    protected Object returnValue(List returnList) {
        
        if (getMethod().getReturnType().isInstance(CHECKLIST)) {
            return returnList;
        }
        
        return returnSingleObject(returnList);
    }

    /**
     * @param returnList
     * @return
     */
    protected Object returnSingleObject(List returnList) {
        if (returnList.size()>0) {
            return returnList.get(0);
        } else {
            return null;
        }
    }

}
