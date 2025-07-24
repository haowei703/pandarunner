package com.shiroha.pandarunner.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shiroha.pandarunner.config.ApiProperties;
import com.shiroha.pandarunner.config.MapAPIConstants;
import com.shiroha.pandarunner.exception.APIInvokeException;
import com.shiroha.pandarunner.exception.InvalidParamException;
import com.shiroha.pandarunner.service.MapService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "map")
public class MapServiceImpl implements MapService {

    private final RestTemplate restTemplate;
    private final ApiProperties apiProperties;

    private static final String COORDINATE_REGEX =
            "^(-?(?:180(?:\\.0+)?|1[0-7]\\d|\\d{1,2})(?:\\.\\d+)?),(-?(?:90(?:\\.0+)?|[1-8]\\d|\\d)(?:\\.\\d+)?)$";

    private static final String MULTI_COORDINATE_REGEX =
            "^[-+]?(180(\\.0+)?|1?[0-7]?\\d(\\.\\d+)?),[-+]?(90(\\.0+)?|[1-8]?\\d(\\.\\d+)?)(\\|[-+]?(180(\\.0+)?|1" +
                    "?[0-7]?\\d(\\.\\d+)?),[-+]?(90(\\.0+)?|[1-8]?\\d(\\.\\d+)?))*$";

    /**
     * 获取三级地址的json数据
     *
     * @return json格式的三级地址
     */
    @Override
    @Cacheable(key = "'districts'", unless = "#result == null")
    public JSON getDistricts() {
        return fetchUrl(MapAPIConstants.DISTRICT_PATH,
                // 设置请求参数
                (builder) -> builder.queryParam("subdistrict", 3),
                // 处理响应
                (response) -> response.getJSONArray("districts"));
    }

    /**
     * 获取地址的经纬度坐标
     *
     * @param address 地址
     * @return 经纬度
     */
    @Override
    public String getLocation(String address) {
        return fetchUrl(MapAPIConstants.LOCATION_PATH,
                (builder) -> builder.queryParam("address", address),
                (response) -> response.getJSONArray("geocodes")
                        .getJSONObject(0)
                        .getStr("location"));
    }

    /**
     * 获取两个经纬度点的距离
     *
     * @param origins     起始点坐标
     * @param destination 终点坐标
     * @return 两点的距离
     */
    @Override
    @Cacheable(key = "#origins + #destination")
    public JSON getDistance(String origins, String destination) {
        // 检验参数格式
        if(!Pattern.matches(MULTI_COORDINATE_REGEX, origins)) {
            throw new InvalidParamException("坐标格式不正确", "origins", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        if(!Pattern.matches(COORDINATE_REGEX, destination)) {
            throw new InvalidParamException("坐标格式不正确", "destination", InvalidParamException.ErrorType.ILLEGAL_VALUE);
        }

        // API获取
        return fetchUrl(MapAPIConstants.DISTANCE_PATH,
                // 设置请求参数
                (builder) -> builder.queryParam("origins", origins)
                        .queryParam("destination", destination),
                // 处理响应
                (response) -> response.getJSONArray("results"));
    }

    /**
     *
     * 请求URL并对响应做校验
     *
     * @param path          请求路径
     * @param consumer      函数式接口，处理uri的builder
     * @param function      函数式接口，处理响应结果
     * @return              提取的json部分
     * @param <T>           响应类型
     */
    private <T> T fetchUrl(String path, Consumer<UriComponentsBuilder> consumer, Function<JSONObject, T> function) {
        // 构造URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(MapAPIConstants.BASE_URL)
                .path(path);
        builder.queryParam("key", apiProperties.getKey());

        consumer.accept(builder);
        String url = builder
                // 禁用自动编码，防止"|"被编码为"%7c"
                .build(false)
                .toUriString();

        // 发送请求，并对响应做校验
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );

            validateResponseEntity(responseEntity);
            // 将json字符串转JSONObj
            JSONObject body = JSONUtil.parseObj(responseEntity.getBody());
            validateResponseBody(body);
            return function.apply(body);
        } catch (APIInvokeException e) {
            throw e;
        } catch (Exception e) {
            throw new APIInvokeException("API调用失败", e);
        }
    }

    /**
     * 检验响应是否成功
     */
    private void validateResponseEntity(ResponseEntity<?> responseEntity) {
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new APIInvokeException("API调用失败: " + responseEntity.getBody());
        }
    }

    /**
     * 检验响应体是否符合预期
     */
    private void validateResponseBody(JSONObject body) {
        if(body.isEmpty()) {
            throw new APIInvokeException("响应体为空");
        }

        if(!"1".equals(body.get("status")) || !"OK".equals(body.get("info"))) {
            throw new APIInvokeException("API调用失败: " + body.get("info"));
        }
    }
}
