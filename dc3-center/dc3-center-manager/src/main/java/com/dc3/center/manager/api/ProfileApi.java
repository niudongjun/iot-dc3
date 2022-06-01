/*
 * Copyright (c) 2022. Pnoker. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dc3.center.manager.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc3.api.center.manager.feign.ProfileClient;
import com.dc3.center.manager.service.NotifyService;
import com.dc3.center.manager.service.ProfileService;
import com.dc3.common.bean.R;
import com.dc3.common.constant.CommonConstant;
import com.dc3.common.constant.ServiceConstant;
import com.dc3.common.dto.ProfileDto;
import com.dc3.common.model.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 模板 Client 接口实现
 *
 * @author pnoker
 */
@Slf4j
@RestController
@RequestMapping(ServiceConstant.Manager.PROFILE_URL_PREFIX)
public class ProfileApi implements ProfileClient {

    @Resource
    private NotifyService notifyService;
    @Resource
    private ProfileService profileService;

    @Override
    public R<Profile> add(Profile profile, String tenantId) {
        try {
            Profile add = profileService.add(profile.setTenantId(tenantId));
            if (null != add) {
                return R.ok(add);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @Override
    public R<Boolean> delete(String id) {
        try {
            Profile profile = profileService.selectById(id);
            if (null != profile && profileService.delete(id)) {
                notifyService.notifyDriverProfile(CommonConstant.Driver.Profile.DELETE, profile);
                return R.ok();
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @Override
    public R<Profile> update(Profile profile, String tenantId) {
        try {
            Profile update = profileService.update(profile.setTenantId(tenantId));
            if (null != update) {
                notifyService.notifyDriverProfile(CommonConstant.Driver.Profile.UPDATE, update);
                return R.ok(update);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @Override
    public R<Profile> selectById(String id) {
        try {
            Profile select = profileService.selectById(id);
            if (null != select) {
                return R.ok(select);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @Override
    public R<List<Profile>> selectByDeviceId(String deviceId) {
        try {
            List<Profile> select = profileService.selectByDeviceId(deviceId);
            if (null != select) {
                return R.ok(select);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @Override
    public R<Page<Profile>> list(ProfileDto profileDto, String tenantId) {
        try {
            profileDto.setTenantId(tenantId);
            Page<Profile> page = profileService.list(profileDto);
            if (null != page) {
                return R.ok(page);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

}
