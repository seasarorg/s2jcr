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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.jcr.JCRDtoDesc;

public class JCRDtoDescImpl implements JCRDtoDesc {

    private BeanDesc beanDesc;
    
    private Object targetObject;
    
    public JCRDtoDescImpl(Object targetObject) {
        this.targetObject = targetObject;
        this.beanDesc = BeanDescFactory.getBeanDesc(targetObject.getClass());
    }
    
    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.impl.JcrDTODesc#getPropertyList()
     */
    public Map getFieldValueMap() {
        
        Map map = new HashMap();
        
        //TODO factoryフィールドをもつ場合
        for(int i=0;i<beanDesc.getFieldSize();i++) {
            Field field = beanDesc.getField(i);
            String fieldName = field.getName();
            
            if (!isAnnotationField(fieldName)) {
                Object obj = beanDesc.getFieldValue(fieldName, targetObject);
                if (obj != null ) {
                    map.put(fieldName, obj);
                }                    
            }
        }
        
        return map;
    
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.JCRDTODesc#getDtoClass()
     */
    public Class getDtoClass() {
        return targetObject.getClass();
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.JCRDTODesc#getBeanDesc()
     */
    public BeanDesc getBeanDesc() {
        return beanDesc;
    }

    private boolean isAnnotationField(String fieldName) {
        if (fieldName.endsWith("_PROPERTY")) {
            return true;
        }
        
        return false;
    }
}
