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
package org.seasar.jcr.jackrabbit.converter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.QueryResult;

import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.StringUtil;
import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.BeanAnnotationReader;
import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.util.JCRUtil;

public class JackrabbitConverter implements JcrConverter {

    private AnnotationReaderFactory annotationReaderFactory;

    public JackrabbitConverter() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.util.JcrConverter#convertPathToNode(javax.jcr.Node,
     * java.lang.String[])
     */
    public Node convertPathToNode(Node baseNode, String[] path)
            throws Throwable {

        int lastNodeIndex = path.length - 1;
        for (int i = 0; i < path.length; i++) {
            if (!StringUtil.isEmpty(path[i])) {
                if (i == lastNodeIndex) {
                    baseNode = baseNode.addNode(path[i]);
                } else {
                    try {
                        baseNode = baseNode.getNode(path[i]);
                    } catch (PathNotFoundException e) {
                        baseNode = baseNode.addNode(path[i]);
                    } catch (RepositoryException e) {
                        throw e;
                    }
                }
            }
        }

        return baseNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.util.JcrConverter#convertDtoToNode(javax.jcr.Node,
     * org.seasar.jcr.JCRDtoDesc)
     */
    public void convertDtoToNode(Node targetNode, JCRCommandDesc cmdDesc)
            throws Throwable {

        JCRDtoDesc dtoDesc = cmdDesc.getJCRDtoDesc();
        for (Iterator ite = dtoDesc.getFieldValueMap().keySet().iterator(); ite
                .hasNext();) {

            String propertyName = (String) ite.next();
            Object propertyValue = dtoDesc.getFieldValueMap().get(propertyName);

            String resolvedPropertyName = getResolvedPropertyName(propertyName,
                    cmdDesc);

            if (propertyValue != null) {
                targetNode.setProperty(resolvedPropertyName,
                        convertToValue(propertyValue));
            }
        }

        targetNode.setProperty(S2JCRConstants.S2JCR_CLASS_ATTR, JCRUtil
                .getClassName(dtoDesc.getDtoClass()));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.util.JcrConverter#convert(java.lang.Object)
     */
    public Value convertToValue(Object object) {

        Value ret = null;
        if (object instanceof String) {
            ret = ValueFactoryImpl.getInstance().createValue((String) object);
        }

        if (object instanceof InputStream) {
            ret = ValueFactoryImpl.getInstance().createValue(
                    (InputStream) object);
        }

        if (object instanceof Calendar) {
            ret = ValueFactoryImpl.getInstance().createValue((Calendar) object);
        }

        if (object instanceof Double) {
            Double d = (Double) object;
            ret = ValueFactoryImpl.getInstance().createValue(d.doubleValue());
        }

        if (object instanceof Long) {
            Long l = (Long) object;
            ret = ValueFactoryImpl.getInstance().createValue(l.longValue());
        }

        if (object instanceof Boolean) {
            Boolean b = (Boolean) object;
            ret = ValueFactoryImpl.getInstance().createValue(b.booleanValue());
        }

        if (object instanceof Integer) {
            Integer ival = (Integer) object;
            ret = ValueFactoryImpl.getInstance().createValue(ival.longValue());
        }

        // Date -> Calendar
        if (object instanceof Date || object instanceof java.sql.Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) object);
            ret = ValueFactoryImpl.getInstance().createValue(cal);
        }

        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.jcr.converter.JcrConverter#convertQResultToDto(javax.jcr.query
     * .QueryResult, java.lang.Object)
     */
    public List convertQResultToDto(QueryResult qr, JCRCommandDesc cmdDesc)
            throws Throwable {

        List returnList = new ArrayList();

        NodeIterator queryResultNodeIterator = qr.getNodes();

        while (queryResultNodeIterator.hasNext()) {

            Node node = queryResultNodeIterator.nextNode();
            Object convertObject = convertNodeToDto(node, cmdDesc);
            if (convertObject != null) {
                returnList.add(convertObject);
            }
        }

        return returnList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.jcr.converter.JcrConverter#convertQResultToDto(javax.jcr.query
     * .QueryResult, java.lang.Object)
     */
    public void convertDtoToQResult(QueryResult qr, JCRCommandDesc cmdDesc)
            throws Throwable {

        NodeIterator queryResultNodeIterator = qr.getNodes();

        while (queryResultNodeIterator.hasNext()) {

            Node node = queryResultNodeIterator.nextNode();
            convertDtoToNode(node, cmdDesc);

        }

    }

    private Object convertNodeToDto(final Node src, final JCRCommandDesc cmdDesc)
            throws Throwable {

        // JCRDtoDesc dtoDesc = cmdDesc.getJCRDtoDesc();
        Class dtoClass = cmdDesc.getTargetDtoClass();

        // check S2JCR_CLASS
        if (!checkNodeClass(src, JCRUtil.getClassName(dtoClass))) {
            return null;
        }

        BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dtoClass);

        int propertyDescSize = destBeanDesc.getPropertyDescSize();
        Object returnObject = dtoClass.newInstance();

        for (int i = 0; i < propertyDescSize; i++) {

            PropertyDesc destPropertyDesc = destBeanDesc.getPropertyDesc(i);

            String propertyName = destPropertyDesc.getPropertyName();
            Class fieldType = destPropertyDesc.getPropertyType();

            String resolvedPropertyName = getResolvedPropertyName(propertyName,
                    cmdDesc);

            Object fieldObject = convert(fieldType, src, resolvedPropertyName);

            destPropertyDesc.setValue(returnObject, fieldObject);
        }

        // overwrite path
        String pathProperty = cmdDesc.getPathProperty();
        PropertyDesc destPropertyDesc = destBeanDesc
                .getPropertyDesc(pathProperty);
        destPropertyDesc.setValue(returnObject, src.getPath());

        return returnObject;

    }

    private boolean checkNodeClass(Node src, String className) {
        try {
            Property property = src
                    .getProperty(S2JCRConstants.S2JCR_CLASS_ATTR);
            if (property != null) {
                String clsName = property.getString();
                if (clsName.equals(className)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    private Object convert(Class clazz, Node src, String propertyName)
            throws Throwable {
        Object ret = null;
        try {
            if (clazz == String.class) {
                ret = src.getProperty(propertyName).getString();
            } else if (clazz == InputStream.class) {
                ret = src.getProperty(propertyName).getStream();
            } else if (clazz == Calendar.class) {
                ret = src.getProperty(propertyName).getDate();
            } else if (clazz == Long.class) {
                ret = new Long(src.getProperty(propertyName).getLong());
            } else if (clazz == Integer.class) {
                Long l = new Long(src.getProperty(propertyName).getLong());
                ret = new Integer(l.intValue());
            } else if (clazz == Double.class) {
                ret = new Double(src.getProperty(propertyName).getDouble());
            } else if (clazz == Boolean.class) {
                ret = new Boolean(src.getProperty(propertyName).getBoolean());
            } else if (clazz == Date.class) {
                ret = src.getProperty(propertyName).getDate().getTime();
            } else if (clazz == java.sql.Date.class) {
                ret = new java.sql.Date(src.getProperty(propertyName).getDate()
                        .getTime().getTime());
            } else {
                ret = null;
            }

        } catch (PathNotFoundException pnfe) {
            // noop
        } catch (Throwable e) {
            throw e;
        }

        return ret;

    }

    private String getResolvedPropertyName(String propertyName,
            JCRCommandDesc cmdDesc) {

        Class dtoClass = cmdDesc.getTargetDtoClass();
        BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dtoClass);

        if (destBeanDesc.hasPropertyDesc(propertyName)) {
            PropertyDesc pd = destBeanDesc.getPropertyDesc(propertyName);
            BeanAnnotationReader beanReader = annotationReaderFactory
                    .createBeanAnnotationReader(dtoClass);
            String newPropertyName = beanReader.getPropertyAnnotation(pd);
            if (newPropertyName != null) {
                return newPropertyName;
            }
        }

        return propertyName;
    }

    public AnnotationReaderFactory getAnnotationReaderFactory() {
        return annotationReaderFactory;
    }

    public void setAnnotationReaderFactory(
            AnnotationReaderFactory annotationReaderFactory) {
        this.annotationReaderFactory = annotationReaderFactory;
    }

}
