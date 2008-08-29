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
package org.seasar.jcr;

import java.lang.reflect.Method;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.jcr.rao.CommandType;

public interface JCRCommandDesc {

    Class getTargetDtoClass();

    String getPath();

    String[] getTargetNodes();

    Class getMethodReturnType();

    CommandType getCommandType(Class dtoClass, Object[] args);

    String getAnnotationField(String fieldName);

    void setJCRDtoDesc(JCRDtoDesc dtoDesc);

    JCRDtoDesc getJCRDtoDesc();

    Method getMethod();

    BeanDesc getBeanDesc();

    String getPathProperty();
}