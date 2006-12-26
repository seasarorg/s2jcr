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

import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.S2JCRSessionFactory;

public abstract class AbstractJCRNodeCommand extends AbstractJCRCommand {

    private Class beanClass;
    private Method method;
    private ArgsMetaData argsMeta;

    private AnnotationReaderFactory annotationReaderFactory;
    
    public AnnotationReaderFactory getAnnotationReaderFactory() {
        return annotationReaderFactory;
    }

    /**
     * @param sessionFactory
     */
    public AbstractJCRNodeCommand(S2JCRSessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ArgsMetaData getArgsMeta() {
        return argsMeta;
    }

    public void setArgsMeta(ArgsMetaData argsMeta) {
        this.argsMeta = argsMeta;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setAnnotationReaderFactory(
            AnnotationReaderFactory annotationReaderFactory) {
        this.annotationReaderFactory = annotationReaderFactory;
    }

}
