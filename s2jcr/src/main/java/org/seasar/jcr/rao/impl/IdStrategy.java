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

import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.rao.CommandType;
import org.seasar.jcr.rao.XPathEditStrategy;

/**
 * @author waki41
 * 
 */
public class IdStrategy implements XPathEditStrategy {

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.XPathEditStrategy#createXPath(org.seasar.jcr.JCRDtoDesc)
     */
    public String createXPath(Object targetFieldObject, Object[] args) {

        JCRCommandDesc cmdDesc = (JCRCommandDesc) targetFieldObject;

        if (!cmdDesc.hasId())
            return "";

        StringBuffer sb = new StringBuffer();

        String propertyName = cmdDesc.getIdFieldName();
        Object propertyValue = null;
        if (cmdDesc.getCommandType(cmdDesc.getTargetDtoClass(),args) == CommandType.AUTO_DTO) {            
            propertyValue = cmdDesc.getJCRDtoDesc().getFieldValueMap().get(propertyName);            
        } else {
            propertyValue = args[0];
        }
        
        sb.append("@");
        sb.append(propertyName);
        sb.append("='");
        sb.append(propertyValue);
        sb.append("'");

        return "[" + sb.toString() + "]";
    }

}
