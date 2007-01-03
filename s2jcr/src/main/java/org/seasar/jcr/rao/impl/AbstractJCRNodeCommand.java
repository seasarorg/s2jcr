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

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;

public abstract class AbstractJCRNodeCommand extends AbstractJCRCommand {

    private Class raoClass;
    private Method method;
    private ArgsMetaData argsMeta;

    private BeanDesc beanDesc;

    protected String path;
    protected String[] targetNodes;
    
    protected Class targetDtoClass;
    
    protected JcrConverter jcrConverter;
    
    public AbstractJCRNodeCommand(S2JCRSessionFactory sessionFactory, Method method,
            Class raoClass, JcrConverter jcrConverter) {
        super(sessionFactory);
        this.method = method;
        this.raoClass = raoClass;
        this.jcrConverter = jcrConverter;
        
        this.beanDesc = BeanDescFactory.getBeanDesc(raoClass);
        this.path = (String) beanDesc.getFieldValue("PATH",raoClass);
        this.targetDtoClass = (Class) beanDesc.getFieldValue("DTO",raoClass);
        this.targetNodes = path.split("/");

    }

    public ArgsMetaData getArgsMeta() {
        return argsMeta;
    }

    public void setArgsMeta(ArgsMetaData argsMeta) {
        this.argsMeta = argsMeta;
    }

    public Class getRaoClass() {
        return raoClass;
    }

    public void setRaoClass(Class raoClass) {
        this.raoClass = raoClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String[] getTargetNodes() {
        return targetNodes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class getTargetDtoClass() {
        return targetDtoClass;
    }

}
