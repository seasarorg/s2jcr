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

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.FieldUtil;
import org.seasar.jcr.BeanAnnotationReader;

public class FieldBeanAnnotationReader implements BeanAnnotationReader {

    public static final String PATH_SUFFIX = "_PATH";

    public static final String PROPERTY_SUFFIX = "_PROPERTY";

    private BeanDesc beanDesc;

    public FieldBeanAnnotationReader(Class beanClass_) {
        this.beanDesc = BeanDescFactory.getBeanDesc(beanClass_);
    }

    public String getPropertyAnnotation(PropertyDesc pd) {
        String propertyName = pd.getPropertyName();
        String nodeNameKey = propertyName + PROPERTY_SUFFIX;
        if (beanDesc.hasField(nodeNameKey)) {
            Field field = beanDesc.getField(nodeNameKey);
            return (String) FieldUtil.get(field, null);
        }
        return null;
    }

    public String getPathPropertyAnnotation() {
        int size = beanDesc.getFieldSize();
        for (int i = 0; i < size; i++) {
            String name = beanDesc.getField(i).getName();
            if (name.endsWith(PATH_SUFFIX)) {
                return name;
            }
        }
        return "path"; // default
    }

}
