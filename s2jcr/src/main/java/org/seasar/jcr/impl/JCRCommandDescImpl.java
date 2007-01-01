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
import java.util.Iterator;

import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.exception.S2JCRCommonException;

public class JCRCommandDescImpl implements JCRCommandDesc {

    private static final String DTO_SUFFIX = "Dto";

    private Method method;
    
    private JCRDtoDesc dtoDesc;
    
    public JCRCommandDescImpl(Method method, Object param, String targetNodeDto) {
        this.method = method;
        try {
            if (param instanceof String) {
                this.dtoDesc = new JCRDtoDescImpl(Class.forName(targetNodeDto).newInstance());            
            } else {
                this.dtoDesc = new JCRDtoDescImpl(param);            
            }
        } catch (Exception e) {
            throw new S2JCRCommonException("EJCR001",e, new Object[]{"target dto class name"});
        }
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.JCRCommandDesc#isUsingPathString()
     */
    public boolean includeDtoParam() {
        
        //TODO 厳密にするときは変更するかも
        if (method.getParameterTypes()[0] == String.class) {
            return false;
        }
        
        String dtoName = method.getParameterTypes()[0].getName();
        if (dtoName.endsWith(DTO_SUFFIX)) {
            return true;            
        }
        
        return false;
        
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.JCRCommandDesc#createSql()
     */
    public String createXPath() {
        
        if (dtoDesc.getFieldValueMap().keySet().size()==0) return "";
            
        StringBuffer sb = new StringBuffer();

        for (Iterator iter = dtoDesc.getFieldValueMap().keySet().iterator(); iter.hasNext();) {
            
            if (sb.length()>0) sb.append(" and ");
            
            String propertyName = (String) iter.next();                    
            Object propertyValue = dtoDesc.getFieldValueMap().get(propertyName);
            
            sb.append("@");
            sb.append(propertyName);
            sb.append("='");
            sb.append(propertyValue);
            sb.append("'");
            
        }
            
        return "[" + sb.toString() + "]";
        
    }

    public JCRDtoDesc getDtoDesc() {
        return dtoDesc;
    }

    public void setDtoDesc(JCRDtoDesc dtoDesc) {
        this.dtoDesc = dtoDesc;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /* (non-Javadoc)
     * @see org.seasar.jcr.JCRCommandDesc#getJCRDtoDescl()
     */
    public JCRDtoDesc getJCRDtoDesc() {
        return dtoDesc;
    }
    
}
