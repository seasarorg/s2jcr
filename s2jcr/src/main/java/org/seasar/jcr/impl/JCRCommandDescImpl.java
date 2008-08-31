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
package org.seasar.jcr.impl;

import java.lang.reflect.Method;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.FieldNotFoundRuntimeException;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.BeanAnnotationReader;
import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.rao.CommandType;

public class JCRCommandDescImpl implements JCRCommandDesc {

    public static final String VERSIONABLE = "VERSIONABLE";

    public static final String BEAN = "BEAN";

    private BeanDesc beanDesc;

    private Class raoClass;

    private Class targetDtoClass;

    private Method method;

    private JCRDtoDesc dtoDesc;

    private AnnotationReaderFactory annotationReaderFactory;

    private String pathProperty;

    private boolean versionable;

    public JCRCommandDescImpl(Method method, Class raoClass,
            AnnotationReaderFactory annotationReaderFactory) {

        this.method = method;
        this.raoClass = raoClass;
        this.annotationReaderFactory = annotationReaderFactory;

        this.beanDesc = BeanDescFactory.getBeanDesc(raoClass);
        this.targetDtoClass = (Class) beanDesc.getFieldValue(BEAN, raoClass);

        try {
            Boolean value = (Boolean) beanDesc.getFieldValue(VERSIONABLE,
                    raoClass);
            if (value != null && !value.booleanValue()) {
                versionable = false;
            } else {
                versionable = true;
            }
        } catch (Exception e) {
            versionable = true;
        }

        BeanAnnotationReader annotationReader = annotationReaderFactory
                .createBeanAnnotationReader(targetDtoClass);
        this.pathProperty = annotationReader.getPathPropertyAnnotation();

    }

    public void setJCRDtoDesc(JCRDtoDesc dtoDesc) {
        this.dtoDesc = dtoDesc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.JCRCommandDesc#getJCRDtoDescl()
     */
    public JCRDtoDesc getJCRDtoDesc() {
        return dtoDesc;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPathProperty() {
        return pathProperty;
    }

    public void setPathProperty(String pathProperty) {
        this.pathProperty = pathProperty;
    }

    public boolean isVersionable() {
        return versionable;
    }

    public void setVersionable(boolean versionable) {
        this.versionable = versionable;
    }

    public String getPath() {
        if (dtoDesc != null) {
            String path = (String) dtoDesc.getFieldValueMap().get(pathProperty);
            if (path == null) {
                return "*"; // TODO null?
            }
            return path;
        }
        return null;
    }

    public String[] getTargetNodes() {
        if (dtoDesc != null) {
            String path = (String) dtoDesc.getFieldValueMap().get(pathProperty);
            if (path == null) {
                return null; // TODO exception?
            }
            return path.split("/");
        }
        return null;
    }

    public Class getTargetDtoClass() {
        return targetDtoClass;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.JCRCommandDesc#getMethodReturnType()
     */
    public Class getMethodReturnType() {
        return method.getReturnType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.JCRCommandDesc#getCommandType()
     */
    public CommandType getCommandType(Class dtoClass, Object[] args) {

        try {
            getAnnotationField(method.getName()
                    + S2JCRConstants.XPATH_QUERY_SUFFIX);

            return CommandType.AUTO_XPATH_ANNOTATION;

        } catch (FieldNotFoundRuntimeException e) {
            // noop
        }

        String dtoName = dtoClass.getName();
        if (args.length == 1) {

            String objClassName = args[0].getClass().getName();
            if (objClassName.endsWith(S2JCRConstants.DTO_SUFFIX)) {
                return CommandType.AUTO_DTO;
            }

        }

        return CommandType.DEFAULT;

    }

    public BeanDesc getBeanDesc() {
        return beanDesc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.JCRCommandDesc#getAnnotationField(java.lang.String)
     */
    public String getAnnotationField(String fieldName) {
        return (String) beanDesc.getFieldValue(fieldName, raoClass);
    }

}
