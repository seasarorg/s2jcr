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

import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.exception.S2JCRCommonException;
import org.seasar.jcr.impl.JCRDtoDescImpl;
import org.seasar.jcr.rao.CommandType;
import org.seasar.jcr.rao.XPathEditStrategy;

public class GetCommand extends AbstractAutoJCRXPathCommand {

    private static final ArrayList CHECKLIST = new ArrayList();
    
    public GetCommand(S2JCRSessionFactory sessionFactory, Method method,
            Class raoClass, JcrConverter jcrConverter, 
            AnnotationReaderFactory annotationReaderFactory) {
        super(sessionFactory, method, raoClass, jcrConverter, annotationReaderFactory);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.JcrCommand#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws RepositoryException {
        
        List returnList = new ArrayList();
        
        Session session = getSession();
        
        try {
            
            JCRCommandDesc cmdDesc = getCommandDesc();            
            CommandType cmdType = cmdDesc.getCommandType(cmdDesc.getTargetDtoClass(),args);

            JCRDtoDesc dtoDesc = createJCRDtoDesc(cmdType, args);
            cmdDesc.setJCRDtoDesc(dtoDesc);            

            String nodePath = S2JCRConstants.XPATH_PREFIX + getPath();
            
            XPathEditStrategy strategy = 
                XPathEditorFactoryImpl.createXPathEditor(cmdType);
            
            String xpath = strategy.createXPath(cmdDesc,args);
            nodePath = nodePath + xpath;
            
            QueryManager qm = session.getWorkspace().getQueryManager();
            Query query = qm.createQuery(nodePath, Query.XPATH);
            QueryResult queryResult = query.execute();

            returnList = jcrConverter.convertQResultToDto(queryResult, dtoDesc);
            
        } catch (Throwable e) {
            
            throw new S2JCRCommonException("EJCR0001",e);
            
        } finally {
            
            session.logout();
            
        }
        
        return returnValue(returnList);

    }

    /**
     * @param cmdType
     * @param args
     * @return
     */
    private JCRDtoDesc createJCRDtoDesc(CommandType cmdType, Object[] args) {
        try {
            if (cmdType==CommandType.AUTO_DTO) {
                return new JCRDtoDescImpl(args[0]);
            } else if (cmdType==CommandType.DEFAULT) {
                return new JCRDtoDescImpl(getCommandDesc().getTargetDtoClass().newInstance());
            } else if (cmdType==CommandType.AUTO_XPATH_ANNOTATION) {
                return new JCRDtoDescImpl(getCommandDesc().getTargetDtoClass().newInstance());
            }
            return null;
        } catch (Throwable e) {
            throw new S2JCRCommonException("EJCR0001",e);
        }
    }

    /**
     * @param returnList
     * @return
     */
    protected Object returnValue(List returnList) {
        
        if (getCommandDesc().getMethodReturnType().isInstance(CHECKLIST)) {
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
