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
package org.seasar.jcr.util;

import java.io.InputStream;

import javax.jcr.Node;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jcr.exception.S2JCRCommonException;

public class NodeUtil {

    private NodeUtil() {}
    
    public static void copyProperties(final Node src, final Object dto) {

        BeanDesc destBeanDesc = BeanDescFactory.getBeanDesc(dto
                .getClass());

        int propertyDescSize = destBeanDesc.getPropertyDescSize();
        for (int i = 0; i < propertyDescSize; i++) {
            PropertyDesc destPropertyDesc = destBeanDesc
                    .getPropertyDesc(i);
            
            String propertyName = destPropertyDesc.getPropertyName();
            Class fieldType = destPropertyDesc.getPropertyType();
            

            Object newObject = convert(fieldType, src, propertyName);
            
            destPropertyDesc.setValue(dto, newObject);
        }
    }
    
    private static Object convert(Class clazz, Node src, String propertyName) {
        //TODO support full type 
        try {
            if (clazz == String.class) {
                return src.getProperty(propertyName).getString();
            } else if (clazz == InputStream.class) {
                return src.getProperty(propertyName).getStream();            
            }
        } catch (Exception e) {
            throw new S2JCRCommonException("EJCR0001");
        }
        
        return null;
        
    }
}
