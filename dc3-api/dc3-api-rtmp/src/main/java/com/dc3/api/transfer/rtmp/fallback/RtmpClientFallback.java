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

package com.dc3.api.transfer.rtmp.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc3.api.transfer.rtmp.feign.RtmpClient;
import com.dc3.common.bean.R;
import com.dc3.common.dto.RtmpDto;
import com.dc3.common.model.Rtmp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RtmpClientFallback
 *
 * @author pnoker
 */
@Slf4j
@Component
public class RtmpClientFallback implements FallbackFactory<RtmpClient> {

    @Override
    public RtmpClient create(Throwable throwable) {
        String message = throwable.getMessage() == null ? "No available server for client: DC3-TRANSFER-RTMP" : throwable.getMessage();
        log.error("Fallback:{}", message);

        return new RtmpClient() {
            @Override
            public R<Rtmp> add(Rtmp rtmp, String tenantId) {
                return R.fail(message);
            }

            @Override
            public R<Boolean> delete(String id) {
                return R.fail(message);
            }

            @Override
            public R<Rtmp> update(Rtmp rtmp, String tenantId) {
                return R.fail(message);
            }

            @Override
            public R<Rtmp> selectById(String id) {
                return R.fail(message);
            }

            @Override
            public R<Page<Rtmp>> list(RtmpDto rtmpDto, String tenantId) {
                return R.fail(message);
            }

            @Override
            public R<Boolean> start(String id) {
                return R.fail(message);
            }

            @Override
            public R<Boolean> stop(String id) {
                return R.fail(message);
            }
        };
    }
}