/*
 * Copyright 2016 http://www.hswebframework.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.hswebframework.web.controller;

import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.commons.entity.Entity;
import org.hswebframework.web.commons.entity.param.QueryParamEntity;
import org.hswebframework.web.controller.message.ResponseMessage;
import org.hswebframework.web.logging.AccessLogger;
import org.hswebframework.web.service.QueryByEntityService;
import org.hswebframework.web.service.QueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.hswebframework.web.controller.message.ResponseMessage.ok;

/**
 * 通用查询控制器。
 *
 * @param <E>  实体类型
 * @param <PK> 主键类型
 * @param <Q>  查询条件实体类型,默认提供{@link QueryParamEntity}实现
 * @author zhouhao
 * @see QueryParamEntity
 * @see 3.0
 */
public interface QueryController<E, PK, Q extends Entity>  {

    /**
     * 获取实现了{@link QueryByEntityService}和{@link QueryService}的服务类
     *
     * @param <T> 服务类泛型
     * @return 服务类实例
     */
    <T extends QueryByEntityService<E> & QueryService<E, PK>> T getService();

    /**
     * 根据参数动态查询。<br>
     * 参数泛型如果为QueryParamEntity,
     * 客户的参数 ?terms[0].column=name&terms[0].value=小明
     * 则执行查询条件 where name = '小明'
     * 具体使用方法参照 {@link QueryParamEntity}
     *
     * @param param 参数
     * @return 查询结果
     */
    @Authorize(action = "read")
    @GetMapping
    @AccessLogger("根据条件查询")
    default ResponseMessage list(Q param) {
        return ok(getService().selectPager(param));
    }

    @Authorize(action = "read")
    @GetMapping(path = "/{id}")
    @AccessLogger("根据主键查询")
    default ResponseMessage getByPrimaryKey(@PathVariable PK id) {
        return ok(getService().selectByPk(id));
    }

}
