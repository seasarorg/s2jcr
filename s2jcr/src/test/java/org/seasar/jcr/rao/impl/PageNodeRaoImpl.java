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

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.seasar.jcr.dto.FileNodeDto;
import org.seasar.jcr.rao.AbstractRao;
import org.seasar.jcr.rao.PageNodeRao;

/**
 * @author waki41
 * 
 */
public class PageNodeRaoImpl extends AbstractRao implements PageNodeRao {

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#add(org.seasar.jcr.dto.FileNodeDto)
     */
    public void add(FileNodeDto dto) {

        try {
            Session session = getSession();
            Node node = session.getRootNode();
            Node wakiNode = node.addNode("waki");
            wakiNode.setProperty("name", "waki");
            session.save();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#get(org.seasar.jcr.dto.FileNodeDto)
     */
    public FileNodeDto get(FileNodeDto nodePath) {
        try {
            Session session = getSession();
            Node node = session.getRootNode();
            Node wakiNode = node.getNode("waki");
            String wakiName = wakiNode.getProperty("name").getString();
            System.out.println("name=" + wakiName);
            session.save();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#getWithNodePath(java.lang.String)
     */
    public FileNodeDto getWithNodePath(String nodePath) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#get2()
     */
    public FileNodeDto get2() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#find(org.seasar.jcr.dto.FileNodeDto)
     */
    public List find(FileNodeDto nodePath) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.jcr.rao.PageNodeRao#update(org.seasar.jcr.dto.FileNodeDto)
     */
    public Object update(FileNodeDto dto) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.jcr.rao.PageNodeRao#delete(org.seasar.jcr.dto.FileNodeDto)
     */
    public void delete(FileNodeDto dto) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#getWithPrice(java.lang.Double,
     * java.lang.Double)
     */
    public FileNodeDto getWithPrice(Double minimum, Double maximum) {
        // TODO Auto-generated method stub
        return null;
    }

}
