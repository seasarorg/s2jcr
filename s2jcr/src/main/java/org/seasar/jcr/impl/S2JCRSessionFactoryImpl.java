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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

import org.seasar.framework.log.Logger;
import org.seasar.jcr.S2JCRSessionFactory;
import org.seasar.jcr.event.EventListenerDefinition;
import org.seasar.jcr.repository.RepositoryFactory;
import org.seasar.jcr.util.JCRUtil;

public class S2JCRSessionFactoryImpl implements S2JCRSessionFactory {

    /**
     * Logger for this class
     */
    private static Logger logger = Logger
            .getLogger(S2JCRSessionFactoryImpl.class);

    private Repository repository;

    private String workspaceName;

    private Map namespaces;

    private Map overwrittenNamespaces;

    private EventListenerDefinition eventListeners[] = new EventListenerDefinition[] {};

    /**
     * Constructor with all the required fields.
     * 
     * @param repository
     * @param workspaceName
     * @throws RepositoryException
     */
    public S2JCRSessionFactoryImpl(Repository repository, String workspaceName)
            throws RepositoryException {

        this.repository = repository;
        this.workspaceName = workspaceName;

        if (getRepository() == null) {
            throw new IllegalArgumentException("repository is required");
        }
    }

    /**
     * Constructor with the repository.
     * 
     * @param repository
     * @param workspaceName
     * @param credentials
     * @throws RepositoryException
     */
    public S2JCRSessionFactoryImpl(Repository repository)
            throws RepositoryException {
        this(repository, null);
    }

    /**
     * Empty constructor.
     * 
     * @throws RepositoryException
     */
    public S2JCRSessionFactoryImpl() throws RepositoryException {
        this((Repository) null);
    }

    public S2JCRSessionFactoryImpl(RepositoryFactory repositoryFactory,
            String workspaceName) throws RepositoryException {
        this(repositoryFactory.getRepository(), workspaceName);
    }

    public S2JCRSessionFactoryImpl(RepositoryFactory repositoryFactory)
            throws RepositoryException {
        this(repositoryFactory, null);
    }

    public Session getSession() throws RepositoryException {

        return repository.login(new SimpleCredentials("defaultuser",
                "defaultpwd".toCharArray()));

    }

    protected Session addListeners(Session session) throws RepositoryException {
        if (eventListeners != null && eventListeners.length > 0) {
            Workspace ws = session.getWorkspace();
            ObservationManager manager = ws.getObservationManager();
            logger.debug("adding listeners "
                    + Arrays.asList(eventListeners).toString()
                    + " for session " + session);

            for (int i = 0; i < eventListeners.length; i++) {
                manager.addEventListener(eventListeners[i].getListener(),
                        eventListeners[i].getEventTypes(), eventListeners[i]
                                .getAbsPath(), eventListeners[i].isDeep(),
                        eventListeners[i].getUuid(), eventListeners[i]
                                .getNodeTypeName(), eventListeners[i]
                                .isNoLocal());
            }
        }
        return session;
    }

    public EventListenerDefinition[] getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(
            EventListenerDefinition[] eventListenerDefinitions) {
        if (eventListeners != null && eventListeners.length > 0
                && !JCRUtil.supportsObservation(getRepository()))
            throw new IllegalArgumentException(
                    "repository "
                            + getRepositoryInfo()
                            + " does NOT support Observation; remove Listener definitions");
        this.eventListeners = eventListenerDefinitions;
    }

    public void destroy() throws RepositoryException {
        unregisterNamespaces();
    }

    public void registerNamespaces() throws RepositoryException {
        if (namespaces == null || namespaces.isEmpty()) {
            return;
        }

        logger.debug("registering custom namespaces " + namespaces);

        NamespaceRegistry registry = getSession().getWorkspace()
                .getNamespaceRegistry();

        // for storing original namespaces
        overwrittenNamespaces = new HashMap(namespaces.size());

        // Get prefixs
        String[] prefixes = registry.getPrefixes();
        // sort the array
        Arrays.sort(prefixes);

        // search occurences
        for (Iterator iter = namespaces.keySet().iterator(); iter.hasNext();) {
            String prefix = (String) iter.next();
            int position = Arrays.binarySearch(prefixes, prefix);
            if (position >= 0) {
                logger.debug("prefix " + prefix
                        + " was already registered; unregistering it");
                // save old namespace
                overwrittenNamespaces.put(prefix, registry.getURI(prefix));
                try {
                    // unregister the namespace
                    registry.unregisterNamespace(prefix);
                } catch (NamespaceException e) {
                    logger.warn(
                            "Could not unregister the namespace: " + prefix, e);
                }
            }
        }

        // do the registration
        for (Iterator iter = namespaces.entrySet().iterator(); iter.hasNext();) {
            Map.Entry namespace = (Map.Entry) iter.next();
            try {
                registry.registerNamespace((String) namespace.getKey(),
                        (String) namespace.getValue());
            } catch (NamespaceException e) {
                logger.warn("Could not register the namespace: "
                        + namespace.getKey(), e);
            }
        }
    }

    protected void unregisterNamespaces() throws RepositoryException {

        if (namespaces == null || namespaces.isEmpty()) {
            return;
        }

        logger.debug("unregistering custom namespaces " + namespaces);

        NamespaceRegistry registry = getSession().getWorkspace()
                .getNamespaceRegistry();

        for (Iterator iter = namespaces.keySet().iterator(); iter.hasNext();) {
            String prefix = (String) iter.next();
            registry.unregisterNamespace(prefix);
        }

        logger.debug("reverting back overwritten namespaces "
                + overwrittenNamespaces);
        if (overwrittenNamespaces != null) {
            for (Iterator iter = overwrittenNamespaces.entrySet().iterator(); iter
                    .hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                registry.registerNamespace((String) entry.getKey(),
                        (String) entry.getValue());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.marevol.utils.jcr.impl.SessionFactory#getRepository()
     */
    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof S2JCRSessionFactoryImpl)
            return (this.hashCode() == obj.hashCode());
        return false;

    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + repository.hashCode();
        // add the optional params (can be null)
        if (workspaceName != null)
            result = 37 * result + workspaceName.hashCode();

        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SessionFactory for ");
        buffer.append(getRepositoryInfo());
        buffer.append("|workspace=");
        buffer.append(getWorkspaceName());
        return buffer.toString();
    }

    /**
     * A toString representation of the Repository.
     * 
     * @return
     */
    private String getRepositoryInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getRepository().getDescriptor(Repository.REP_NAME_DESC));
        buffer.append(" ");
        buffer.append(getRepository()
                .getDescriptor(Repository.REP_VERSION_DESC));
        return buffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.marevol.utils.jcr.impl.SessionFactory#getNamespaces()
     */
    public Map getNamespaces() {
        return namespaces;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.marevol.utils.jcr.impl.SessionFactory#setNamespaces(java.util.Map)
     */
    public void setNamespaces(Map namespaces) {
        this.namespaces = namespaces;
    }

}
