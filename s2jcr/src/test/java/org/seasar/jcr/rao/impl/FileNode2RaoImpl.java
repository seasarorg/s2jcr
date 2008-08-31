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

import org.seasar.framework.util.StringUtil;
import org.seasar.jcr.S2JCRConstants;
import org.seasar.jcr.dto.FileNode2Dto;
import org.seasar.jcr.rao.AbstractRao;
import org.seasar.jcr.rao.FileNode2Rao;

/**
 * @author waki41
 * 
 */
public class FileNode2RaoImpl extends AbstractRao implements FileNode2Rao {

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#add(org.seasar.jcr.dto.FileNodeDto)
     */
    public void add(FileNode2Dto dto) {

        try {
            Session session = getSession();
            Node node = session.getRootNode();
            String[] paths = dto.getPath().split("/");
            for (int i = 0; i < paths.length; i++) {
                if (!StringUtil.isEmpty(paths[i])) {
                    node = node.addNode(paths[i]);
                }
            }
            node.setProperty("title", dto.getTitle()); // TODO
            node.setProperty(S2JCRConstants.S2JCR_CLASS_ATTR, dto.getClass()
                    .toString());
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
    public FileNode2Dto get(FileNode2Dto dto) {
        // TODO
        // try {
        // Session session = getSession();
        // Node node = session.getRootNode();
        // String[] paths = dto.getPath().split("/");
        // for (int i = 0; i < paths.length; i++) {
        // if (!StringUtil.isEmpty(paths[i])) {
        // node = node.addNode(paths[i]);
        // }
        // }
        //            
        // session.save();
        // } catch (RepositoryException e) {
        // e.printStackTrace();
        // }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#getWithNodePath(java.lang.String)
     */
    public FileNode2Dto getWithNodePath(String nodePath) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#get2()
     */
    public FileNode2Dto get2() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#find(org.seasar.jcr.dto.FileNodeDto)
     */
    public List find(FileNode2Dto nodePath) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.jcr.rao.PageNodeRao#update(org.seasar.jcr.dto.FileNodeDto)
     */
    public Object update(FileNode2Dto dto) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.jcr.rao.PageNodeRao#delete(org.seasar.jcr.dto.FileNodeDto)
     */
    public void delete(FileNode2Dto dto) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.jcr.rao.PageNodeRao#getWithPrice(java.lang.Double,
     * java.lang.Double)
     */
    public FileNode2Dto getWithPrice(Double minimum, Double maximum) {
        // TODO Auto-generated method stub
        return null;
    }

}
