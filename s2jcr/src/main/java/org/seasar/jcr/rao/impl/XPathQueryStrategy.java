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

import org.seasar.framework.exception.SIllegalArgumentException;
import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.rao.XPathEditStrategy;
import org.seasar.jcr.util.JCRUtil;

/**
 * @author waki41
 * 
 */
public class XPathQueryStrategy implements XPathEditStrategy {

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
        String targetXPath = (String) cmdDesc.getAnnotationField(cmdDesc
                .getMethod().getName()
                + S2JCRConstants.XPATH_QUERY_SUFFIX);

        StringBuffer sb = new StringBuffer(replace(targetXPath, "?", args));

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

    private String replace(final String targetString, String replaceString,
            Object[] param) {

        int cnt = 0;
        int pos = 0;
        while (targetString.indexOf(replaceString, pos) > 0) {
            cnt++;
            pos = targetString.indexOf(replaceString, pos) + 1;
        }

        if (param.length != cnt)
            throw new SIllegalArgumentException("EJCR0001", param);

        pos = 0;
        int i = 0;

        String newString = targetString;
        String newReplaceString = "\\" + replaceString;
        while (newString.indexOf(replaceString, pos) > 0) {

            pos = newString.indexOf(replaceString, pos) + 1;
            newString = newString.replaceFirst(newReplaceString, "'" + param[i]
                    + "'");
            i++;
        }

        return newString;

    }

}
