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

import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.rao.XPathEditStrategy;
import org.seasar.jcr.util.JCRUtil;

/**
 * @author waki41
 * 
 */
public class QBEStrategy implements XPathEditStrategy {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.jcr.rao.XPathEditStrategy#createXPath(org.seasar.jcr.JCRDtoDesc
     * )
     */
    public String createXPath(String path, Object targetFieldObject,
            Object[] args) {

        JCRCommandDesc cmdDesc = (JCRCommandDesc) targetFieldObject;
        JCRDtoDesc dtoDesc = cmdDesc.getJCRDtoDesc();

        StringBuffer sb = new StringBuffer();
        if (dtoDesc.getFieldValueMap().keySet().size() > 0) {
            for (Iterator iter = dtoDesc.getFieldValueMap().keySet().iterator(); iter
                    .hasNext();) {
                if (sb.length() > 0)
                    sb.append(" and ");

                String propertyName = (String) iter.next();
                Object propertyValue = dtoDesc.getFieldValueMap().get(
                        propertyName);

                sb.append("@");
                sb.append(propertyName);
                sb.append("='");
                sb.append(propertyValue);
                sb.append("'");

            }
        }

        // S2JCR_CLASS_ATTR
        if (sb.length() > 0)
            sb.append(" and ");
        sb.append("@");
        sb.append(S2JCRConstants.S2JCR_CLASS_ATTR);
        sb.append("='");
        sb.append(JCRUtil.getClassName(cmdDesc.getTargetDtoClass()));
        sb.append("'");
        if (cmdDesc.isVersionable()) {
            sb.append(" and @jcr:isCheckedOut");
        }

        return path + "[" + sb.toString() + "]";
    }

}
