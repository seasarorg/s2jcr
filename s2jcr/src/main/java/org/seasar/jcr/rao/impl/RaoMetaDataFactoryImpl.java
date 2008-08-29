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

import java.util.HashMap;
import java.util.Map;

import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.rao.RaoMetaData;
import org.seasar.jcr.rao.RaoMetaDataFactory;

public class RaoMetaDataFactoryImpl implements RaoMetaDataFactory {

    private S2JCRSessionFactory sessionFactory;

    private Map raoMetaDataCache = new HashMap();

    private JcrConverter jcrConverter;

    private AnnotationReaderFactory annotationReaderFactory;

    public RaoMetaDataFactoryImpl(S2JCRSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public RaoMetaDataFactoryImpl(S2JCRSessionFactory sessionFactory,
            JcrConverter jcrConverter,
            AnnotationReaderFactory annotationReaderFactory) {
        this.sessionFactory = sessionFactory;
        this.jcrConverter = jcrConverter;
        this.annotationReaderFactory = annotationReaderFactory;
    }

    public synchronized RaoMetaData getRaoMetaData(Class raoClass) {
        String key = raoClass.getName();
        RaoMetaData rmd = (RaoMetaData) raoMetaDataCache.get(key);
        if (rmd != null) {
            return rmd;
        }
        rmd = new RaoMetaDataImpl(sessionFactory, raoClass, jcrConverter,
                annotationReaderFactory);
        raoMetaDataCache.put(key, rmd);
        return rmd;
    }

}
