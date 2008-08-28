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

import java.lang.reflect.Method;

import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.impl.JCRCommandDescImpl;

public abstract class AbstractJCRNodeCommand extends AbstractJCRCommand {

    protected JcrConverter jcrConverter;

    private AnnotationReaderFactory annotationReaderFactory;

    private JCRCommandDesc commandDesc;

    public AbstractJCRNodeCommand(S2JCRSessionFactory sessionFactory,
            Method method, Class raoClass, JcrConverter jcrConverter,
            AnnotationReaderFactory annotationReaderFactory) {
        super(sessionFactory);

        this.commandDesc = new JCRCommandDescImpl(method, raoClass,
                annotationReaderFactory);
        this.jcrConverter = jcrConverter;
        this.annotationReaderFactory = annotationReaderFactory;

    }

    public String[] getTargetNodes() {
        return commandDesc.getTargetNodes();
    }

    public String getPath() {
        return commandDesc.getPath();
    }

    public Class getTargetDtoClass() {
        return commandDesc.getTargetDtoClass();
    }

    public JCRCommandDesc getCommandDesc() {
        return commandDesc;
    }

}
