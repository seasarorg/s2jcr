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

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.seasar.jcr.AnnotationReaderFactory;
import org.seasar.jcr.JCRCommandDesc;
import org.seasar.jcr.JCRDtoDesc;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.converter.JcrConverter;
import org.seasar.jcr.exception.S2JCRCommonException;
import org.seasar.jcr.impl.JCRDtoDescImpl;

public class AddCommand extends AbstractAutoJCRXPathCommand {

    public AddCommand(S2JCRSessionFactory sessionFactory, Method method,
            Class raoClass, JcrConverter jcrConverter,
            AnnotationReaderFactory annotationReaderFactory) {
        super(sessionFactory, method, raoClass, jcrConverter,
                annotationReaderFactory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.JcrCommand#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws RepositoryException {

        Session session = getSession();

        try {

            JCRCommandDesc cmdDesc = getCommandDesc();
            boolean versionable = cmdDesc.isVersionable();

            JCRDtoDesc dtoDesc = new JCRDtoDescImpl(args[0]);
            cmdDesc.setJCRDtoDesc(dtoDesc);

            Node baseNode = session.getRootNode();

            Node currentNode = jcrConverter.convertPathToNode(baseNode,
                    getTargetNodes());
            if (versionable) {
                currentNode.addMixin("mix:versionable");
            }
            jcrConverter.convertDtoToNode(currentNode, cmdDesc);

            session.save();

            // checkin
            if (versionable) {
                currentNode.checkin();
            }

        } catch (Throwable e) {
            throw new S2JCRCommonException("EJCR0001", e);

        } finally {

            session.logout();

        }

        return Long.valueOf(1);
    }

}
