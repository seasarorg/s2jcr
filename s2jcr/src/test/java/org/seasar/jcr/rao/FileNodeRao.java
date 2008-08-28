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

import org.seasar.jcr.dto.FileNodeDto;

public interface FileNodeRao {

    public Class DTO = FileNodeDto.class;

    public String ID = "title";

    public String PATH = "経理2/帳簿2/ファイル";

    public String getWithPrice_XPATH = "@price > ? and @price < ?";

    public String description_PROPERTY = "desc";

    public void add(FileNodeDto dto);

    public FileNodeDto get(FileNodeDto nodePath);

    public FileNodeDto get(String id);

    public FileNodeDto getWithNodePath(String nodePath);

    public FileNodeDto get2();

    public List find(FileNodeDto nodePath);

    public Object update(FileNodeDto dto);

    public void delete(String title);

    public FileNodeDto getWithPrice(Double minimum, Double maximum);

}
