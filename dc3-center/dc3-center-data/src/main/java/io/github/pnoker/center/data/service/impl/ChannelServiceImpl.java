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

package io.github.pnoker.center.data.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pnoker.center.data.entity.vo.RabbitMQDataVo;
import io.github.pnoker.center.data.service.ChannelService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {
    @Override
    public RabbitMQDataVo queryChan(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "sum(rabbitmq_channels * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
            return queryPromethues(promQLQuery, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RabbitMQDataVo queryToChan(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "rabbitmq_channels * on(instance) group_left(rabbitmq_cluster, rabbitmq_node) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''}";
            return queryPromethues(promQLQuery, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RabbitMQDataVo queryChanOpen(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "sum(rate(rabbitmq_channels_opened_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
            return queryPromethues(promQLQuery, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RabbitMQDataVo queryChanClose(String cluster) {
        try {
            // 构建原始 PromQL 查询字符串
            String promQLQuery = "sum(rate(rabbitmq_channels_closed_total[60s]) * on(instance) group_left(rabbitmq_cluster) rabbitmq_identity_info{rabbitmq_cluster='" + cluster + "', namespace=''})";
            return queryPromethues(promQLQuery, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //处理Promethues接口调用，接收数据
    private RabbitMQDataVo queryPromethues(String promQLQuery, boolean iord) throws Exception {
        // 将原始查询字符串转换为 URL 编码格式
        String encodedQuery = URLEncoder.encode(promQLQuery, "UTF-8");
        // 构建查询 URL
        String queryUrl = "http://10.6.0.107:9090/api/v1/query?query=" + encodedQuery;
        List<Double> values = new ArrayList<>();
        List<Integer> ivalues = new ArrayList<>();
        List<Long> times = TimeUnix();
        for (Long time : times) {
            // 发送 GET 请求并获取响应
            String jsonResponse = sendGetRequest(queryUrl + "&time=" + time);
            // 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultNode = rootNode.path("data").path("result").get(0);
            if (iord == true) {//根据iord判断给前端的值时double类型，还是int类型
                double value = resultNode.path("value").get(1).asDouble();
                values.add(value);
            } else {
                int ivalue = resultNode.path("value").get(1).asInt();
                ivalues.add(ivalue);
            }
        }
        RabbitMQDataVo rabbitMQDataVo = new RabbitMQDataVo();
        rabbitMQDataVo.setTimes(times);
        rabbitMQDataVo.setValues(values);
        rabbitMQDataVo.setIvalues(ivalues);
        return rabbitMQDataVo;
    }

    // 发送 HTTP GET 请求并返回响应内容
    private static String sendGetRequest(String queryUrl) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = new URL(queryUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private List<Long> TimeUnix() {
        // 获取当前时间并转换为 UTC 时间
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        // 计算前 15 分钟之前的时间并转换为 UTC 时间
        LocalDateTime fifteenMinutesAgo = now.minusMinutes(15);
        // 初始化时间列表
        List<Long> timestamps = new ArrayList<>();
        // 生成 61 个时间点，每个间隔为 15 秒
        LocalDateTime current = fifteenMinutesAgo;
        for (int i = 0; i < 61; i++) {
            long unixTimestamp = current.toEpochSecond(ZoneOffset.UTC);
            timestamps.add(unixTimestamp);
            current = current.plusSeconds(15);
        }
        return timestamps;
    }

}
