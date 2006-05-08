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

import javax.jcr.Credentials;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.ObservationManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.jcr.EventListenerDefinition;
import org.seasar.jcr.RepositoryFactory;
import org.seasar.jcr.SessionFactory;
import org.seasar.jcr.util.JCRUtil;

public class SessionFactoryImpl implements SessionFactory
{
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(SessionFactoryImpl.class);

    private Repository repository;

    private String workspaceName;

    private Credentials credentials;

    private Map namespaces;

    private Map overwrittenNamespaces;

    private EventListenerDefinition eventListeners[] = new EventListenerDefinition[] {};

    /**
     * Constructor with all the required fields.
     * 
     * @param repository
     * @param workspaceName
     * @param credentials
     * @throws RepositoryException 
     */
    public SessionFactoryImpl(Repository repository, String workspaceName,
            Credentials credentials) throws RepositoryException
    {
        this.repository = repository;
        this.workspaceName = workspaceName;
        this.credentials = credentials;

        if (getRepository() == null)
        {
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
    public SessionFactoryImpl(Repository repository) throws RepositoryException
    {
        this(repository, null, null);
    }

    /**
     * Empty constructor.
     * 
     * @throws RepositoryException 
     */
    public SessionFactoryImpl() throws RepositoryException
    {
        this((Repository) null);
    }

    public SessionFactoryImpl(RepositoryFactory repositoryFactory,
            String workspaceName, Credentials credentials)
            throws RepositoryException
    {
        this(repositoryFactory.getRepository(), workspaceName, credentials);
    }

    public SessionFactoryImpl(RepositoryFactory repositoryFactory)
            throws RepositoryException
    {
        this(repositoryFactory, null, null);
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#getSession()
     */
    public Session getSession() throws RepositoryException
    {
        return addListeners(repository.login(credentials, workspaceName));
    }

    /**
     * Hook for adding listeners to the newly returned session. We have to treat
     * exceptions manually and can't reply on the template.
     * 
     * @param session
     *            JCR session
     * @return the listened session
     */
    protected Session addListeners(Session session) throws RepositoryException
    {
        if (eventListeners != null && eventListeners.length > 0)
        {
            Workspace ws = session.getWorkspace();
            ObservationManager manager = ws.getObservationManager();
            if (log.isDebugEnabled())
                log.debug("adding listeners "
                        + Arrays.asList(eventListeners).toString()
                        + " for session " + session);

            for (int i = 0; i < eventListeners.length; i++)
            {
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

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#getEventListeners()
     */
    public EventListenerDefinition[] getEventListeners()
    {
        return eventListeners;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#setEventListeners(com.marevol.utils.jcr.EventListenerDefinition[])
     */
    public void setEventListeners(
            EventListenerDefinition[] eventListenerDefinitions)
    {
        if (eventListeners != null && eventListeners.length > 0
                && !JCRUtil.supportsObservation(getRepository()))
            throw new IllegalArgumentException(
                    "repository "
                            + getRepositoryInfo()
                            + " does NOT support Observation; remove Listener definitions");
        this.eventListeners = eventListenerDefinitions;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#destroy()
     */
    public void destroy() throws RepositoryException
    {
        unregisterNamespaces();
    }

    /**
     * Register the namespaces.
     * 
     * @throws RepositoryException
     */
    protected void registerNamespaces() throws RepositoryException
    {

        if (namespaces == null || namespaces.isEmpty())
        {
            return;
        }

        if (log.isDebugEnabled())
        {
            log.debug("registering custom namespaces " + namespaces);
        }
        NamespaceRegistry registry = getSession().getWorkspace()
                .getNamespaceRegistry();

        // for storing original namespaces
        overwrittenNamespaces = new HashMap(namespaces.size());

        // Get prefixs
        String[] prefixes = registry.getPrefixes();
        // sort the array
        Arrays.sort(prefixes);

        // search occurences
        for (Iterator iter = namespaces.keySet().iterator(); iter.hasNext();)
        {
            String prefix = (String) iter.next();
            int position = Arrays.binarySearch(prefixes, prefix);
            if (position >= 0)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("prefix " + prefix
                            + " was already registered; unregistering it");
                }
                // save old namespace
                overwrittenNamespaces.put(prefix, registry.getURI(prefix));
                // unregister the namespace
                registry.unregisterNamespace(prefix);
            }
        }

        // do the registration
        for (Iterator iter = namespaces.entrySet().iterator(); iter.hasNext();)
        {
            Map.Entry namespace = (Map.Entry) iter.next();
            registry.registerNamespace((String) namespace.getKey(),
                    (String) namespace.getValue());
        }
    }

    /**
     * Removes the namespaces.
     * 
     */
    protected void unregisterNamespaces() throws RepositoryException
    {

        if (namespaces == null || namespaces.isEmpty())
        {
            return;
        }

        if (log.isDebugEnabled())
        {
            log.debug("unregistering custom namespaces " + namespaces);
        }

        NamespaceRegistry registry = getSession().getWorkspace()
                .getNamespaceRegistry();

        for (Iterator iter = namespaces.keySet().iterator(); iter.hasNext();)
        {
            String prefix = (String) iter.next();
            registry.unregisterNamespace(prefix);
        }

        if (log.isDebugEnabled())
        {
            log.debug("reverting back overwritten namespaces "
                    + overwrittenNamespaces);
        }
        if (overwrittenNamespaces != null)
        {
            for (Iterator iter = overwrittenNamespaces.entrySet().iterator(); iter
                    .hasNext();)
            {
                Map.Entry entry = (Map.Entry) iter.next();
                registry.registerNamespace((String) entry.getKey(),
                        (String) entry.getValue());
            }
        }
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#getRepository()
     */
    public Repository getRepository()
    {
        return repository;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#setRepository(javax.jcr.Repository)
     */
    public void setRepository(Repository repository)
    {
        this.repository = repository;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#getWorkspaceName()
     */
    public String getWorkspaceName()
    {
        return workspaceName;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#setWorkspaceName(java.lang.String)
     */
    public void setWorkspaceName(String workspaceName)
    {
        this.workspaceName = workspaceName;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#getCredentials()
     */
    public Credentials getCredentials()
    {
        return credentials;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#setCredentials(javax.jcr.Credentials)
     */
    public void setCredentials(Credentials credentials)
    {
        this.credentials = credentials;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj instanceof SessionFactoryImpl)
            return (this.hashCode() == obj.hashCode());
        return false;

    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        int result = 17;
        result = 37 * result + repository.hashCode();
        // add the optional params (can be null)
        if (credentials != null)
            result = 37 * result + credentials.hashCode();
        if (workspaceName != null)
            result = 37 * result + workspaceName.hashCode();

        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
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
    private String getRepositoryInfo()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getRepository().getDescriptor(Repository.REP_NAME_DESC));
        buffer.append(" ");
        buffer.append(getRepository()
                .getDescriptor(Repository.REP_VERSION_DESC));
        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#getNamespaces()
     */
    public Map getNamespaces()
    {
        return namespaces;
    }

    /* (non-Javadoc)
     * @see com.marevol.utils.jcr.impl.SessionFactory#setNamespaces(java.util.Map)
     */
    public void setNamespaces(Map namespaces)
    {
        this.namespaces = namespaces;
    }

}
