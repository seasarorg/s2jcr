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

import java.util.Iterator;

import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.rao.XPathEditStrategy;

/**
 * @author waki41
 *
 */
public class QBEStrategy implements XPathEditStrategy {

    /* (non-Javadoc)
     * @see org.seasar.jcr.rao.XPathEditStrategy#createXPath(org.seasar.jcr.JCRDtoDesc)
     */
    public String createXPath(JCRDtoDesc dtoDesc, Object[] args) {
        
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

}
