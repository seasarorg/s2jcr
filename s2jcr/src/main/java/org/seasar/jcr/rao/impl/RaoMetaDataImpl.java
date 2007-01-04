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

//import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.rao.JCRCommand;
import org.seasar.jcr.rao.RaoMetaData;

public class RaoMetaDataImpl implements RaoMetaData {

    private static final String[] ADD_NODE = new String[] { "addNode", "add" };

    private static final String[] UPDATE_NODE = new String[] { "updateNode", "update" };

    private static final String[] DELETE_NODE = new String[] { "deleteNode", "removeNode", "delete", "remove" };

    private static final String[] GET_NODE = new String[] { "getNode", "get", "find" };

    private Class raoClass;

    private Map jcrCommands = new HashMap();
    
    private S2JCRSessionFactory sessionFactory;

    private JcrConverter jcrConverter;
    
    private AnnotationReaderFactory annotationReaderFactory;
    
    /**
     * 
     * @param sessionFactory
     * @param raoClass
     */
    public RaoMetaDataImpl(S2JCRSessionFactory sessionFactory, Class raoClass, 
            JcrConverter jcrConverter, AnnotationReaderFactory annotationReaderFactory) {

        this.sessionFactory = sessionFactory;

        this.raoClass = raoClass;
        this.jcrConverter = jcrConverter;
        this.annotationReaderFactory = annotationReaderFactory;
        
        Method[] methods = raoClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            
            setupMethod(methods[i]);
            
        }

    }

    private void setupMethod(Method method) {

        String methodName = method.getName();
        
        if (isMethod(ADD_NODE, methodName)) {
            setupAdd(method);
        } else if (isMethod(GET_NODE, methodName)) {
            setupGet(method);
        } else if (isMethod(UPDATE_NODE, methodName)) {
            setupUpdate(method);
        } else if (isMethod(DELETE_NODE, methodName)) {
            setupDelete(method);
        } else {

        }
    }

    private boolean isMethod(String[] methodNameList, String methodName) {
        for (int i = 0; i < methodNameList.length; ++i) {
            if (methodName.startsWith(methodNameList[i])) {
                return true;
            }
        }
        return false;
    }

//    private String[] getPropertyNames(String methodName) {
//        return getSuffixValues(methodName, PROPERTY_KEY_SUFFIX);
//    }

//    private String getSuffixValue(String methodName, String suffix) {
//        String queryKey = methodName + suffix;
//        if (raoBeanDesc.hasField(queryKey)) {
//            Field queryNamesField = raoBeanDesc.getField(queryKey);
//            return (String) FieldUtil.get(queryNamesField, null);
//        }
//        else {
//            return "";
//        }
//    }

//    private String[] getSuffixValues(String methodName, String suffix) {
//        String value = getSuffixValue(methodName, suffix);
//        if (value.equals("")) {
//            return new String[0];
//        }
//        else {
//            return StringUtil.split(value, ",");
//        }
//    }

    private void setupAdd(Method method) {
        
        AddCommand cmd = new AddCommand(sessionFactory, method, raoClass, 
                jcrConverter, annotationReaderFactory);
        jcrCommands.put(method.getName(), cmd);
    }

    private void setupGet(Method method) {
        
        GetCommand cmd = new GetCommand(sessionFactory, method, raoClass,
                jcrConverter, annotationReaderFactory);
        jcrCommands.put(method.getName(), cmd);
    }

    private void setupUpdate(Method method) {
        
        UpdateCommand cmd = new UpdateCommand(sessionFactory, method, raoClass,
                jcrConverter, annotationReaderFactory);
        jcrCommands.put(method.getName(), cmd);
    }

    private void setupDelete(Method method) {
        
        DeleteCommand cmd = new DeleteCommand(sessionFactory, method, raoClass,
                jcrConverter, annotationReaderFactory);
        jcrCommands.put(method.getName(), cmd);
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.RaoMetaData#getJcrCommand(java.lang.String)
     */
    public JCRCommand getJcrCommand(String methodName) throws MethodNotFoundRuntimeException {
        JCRCommand cmd = (JCRCommand) jcrCommands.get(methodName);
        if (cmd == null) {
            throw new MethodNotFoundRuntimeException(raoClass, methodName,
                    null);
        }
        return cmd;
    }

    public JcrConverter getJcrConverter() {
        return jcrConverter;
    }

    public void setConverter(JcrConverter jcrConverter) {
        this.jcrConverter = jcrConverter;
    }

    public AnnotationReaderFactory getAnnotationReaderFactory() {
        return annotationReaderFactory;
    }

    public void setAnnotationReaderFactory(
            AnnotationReaderFactory annotationReaderFactory) {
        this.annotationReaderFactory = annotationReaderFactory;
    }

}