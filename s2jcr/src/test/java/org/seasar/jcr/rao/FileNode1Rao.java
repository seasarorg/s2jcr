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
package org.seasar.jcr.rao;

import java.util.List;

import org.seasar.jcr.dto.FileNode1Dto;

public interface FileNode1Rao {

    public Class BEAN = FileNode1Dto.class;

    public String getWithPrice_XPATH_QUERY = "@price > ? and @price < ?";

    public void add(FileNode1Dto dto);

    public FileNode1Dto get(FileNode1Dto nodePath);

    public FileNode1Dto get(String id);

    public FileNode1Dto getWithNodePath(String nodePath);

    public FileNode1Dto getFoo();

    public List find(FileNode1Dto nodePath);

    public Object update(FileNode1Dto dto);

    public void delete(String title);

    public FileNode1Dto getWithPrice(Double minimum, Double maximum);

}
