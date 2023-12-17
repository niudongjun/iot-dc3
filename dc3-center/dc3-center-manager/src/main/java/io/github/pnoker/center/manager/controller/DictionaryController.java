/*
 * Copyright 2016-present the IoT DC3 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pnoker.center.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pnoker.center.manager.entity.query.DictionaryQuery;
import io.github.pnoker.center.manager.service.DictionaryService;
import io.github.pnoker.common.base.Controller;
import io.github.pnoker.common.constant.service.ManagerServiceConstant;
import io.github.pnoker.common.entity.R;
import io.github.pnoker.common.entity.bo.DictionaryBO;
import io.github.pnoker.common.entity.builder.DictionaryBuilder;
import io.github.pnoker.common.entity.vo.DictionaryVO;
import io.github.pnoker.common.valid.Parent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 字典 Controller
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@RestController
@RequestMapping(ManagerServiceConstant.DICTIONARY_URL_PREFIX)
public class DictionaryController implements Controller {

    @Resource
    private DictionaryBuilder dictionaryBuilder;

    @Resource
    private DictionaryService dictionaryService;

    /**
     * 查询驱动 Dictionary
     *
     * @param entityQuery {@link DictionaryQuery}
     * @return R Of DictionaryVO Page
     */
    @PostMapping("/driver")
    public R<Page<DictionaryVO>> driverDictionary(@RequestBody(required = false) DictionaryQuery entityQuery) {
        try {
            entityQuery.setTenantId(getTenantId());
            Page<DictionaryBO> entityPageBO = dictionaryService.driverDictionary(entityQuery);
            Page<DictionaryVO> entityPageVO = dictionaryBuilder.buildVOPageByBOPage(entityPageBO);
            return R.ok(entityPageVO);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 查询模板 Dictionary
     *
     * @param entityQuery {@link DictionaryQuery}
     * @return R Of DictionaryVO Page
     */
    @PostMapping("/profile")
    public R<Page<DictionaryVO>> profileDictionary(@RequestBody(required = false) DictionaryQuery entityQuery) {
        try {
            entityQuery.setTenantId(getTenantId());
            Page<DictionaryBO> entityPageBO = dictionaryService.profileDictionary(entityQuery);
            Page<DictionaryVO> entityPageVO = dictionaryBuilder.buildVOPageByBOPage(entityPageBO);
            return R.ok(entityPageVO);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 查询位号 Dictionary
     *
     * @param entityQuery {@link DictionaryQuery}
     * @return R Of DictionaryVO Page
     */
    @PostMapping("/profile_point")
    public R<Page<DictionaryVO>> pointDictionaryForProfile(@Validated(Parent.class) @RequestBody(required = false) DictionaryQuery entityQuery) {
        try {
            entityQuery.setTenantId(getTenantId());
            Page<DictionaryBO> entityPageBO = dictionaryService.pointDictionaryForProfile(entityQuery);
            Page<DictionaryVO> entityPageVO = dictionaryBuilder.buildVOPageByBOPage(entityPageBO);
            return R.ok(entityPageVO);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 查询位号 Dictionary
     *
     * @param entityQuery {@link DictionaryQuery}
     * @return R Of DictionaryVO Page
     */
    @PostMapping("/device_point")
    public R<Page<DictionaryVO>> pointDictionaryForDevice(@Validated(Parent.class) @RequestBody(required = false) DictionaryQuery entityQuery) {
        try {
            entityQuery.setTenantId(getTenantId());
            Page<DictionaryBO> entityPageBO = dictionaryService.pointDictionaryForDevice(entityQuery);
            Page<DictionaryVO> entityPageVO = dictionaryBuilder.buildVOPageByBOPage(entityPageBO);
            return R.ok(entityPageVO);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 查询设备 Dictionary
     *
     * @param entityQuery {@link DictionaryQuery}
     * @return R Of DictionaryVO Page
     */
    @PostMapping("/device")
    public R<Page<DictionaryVO>> deviceDictionary(@RequestBody(required = false) DictionaryQuery entityQuery) {
        try {
            entityQuery.setTenantId(getTenantId());
            Page<DictionaryBO> entityPageBO = dictionaryService.deviceDictionary(entityQuery);
            Page<DictionaryVO> entityPageVO = dictionaryBuilder.buildVOPageByBOPage(entityPageBO);
            return R.ok(entityPageVO);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 查询设备 Dictionary
     *
     * @param entityQuery {@link DictionaryQuery}
     * @return R Of DictionaryVO Page
     */
    @PostMapping("/driver_device")
    public R<Page<DictionaryVO>> deviceDictionaryForDriver(@Validated(Parent.class) @RequestBody(required = false) DictionaryQuery entityQuery) {
        try {
            entityQuery.setTenantId(getTenantId());
            Page<DictionaryBO> entityPageBO = dictionaryService.deviceDictionaryForDriver(entityQuery);
            Page<DictionaryVO> entityPageVO = dictionaryBuilder.buildVOPageByBOPage(entityPageBO);
            return R.ok(entityPageVO);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }
}
