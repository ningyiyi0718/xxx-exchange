package com.exchange.demo.controller;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @Author: ElevenYang
 * @Description: 获取定义的网关的限流规则
 * @Date 2023/4/24 23:12
 */
@RestController
public class GatewayFlowRulesController {


    /**
     * 获取当前的限流策略
     */
    @GetMapping("/gw/flow/rules")
    public Set<GatewayFlowRule> getCurrentGatewayFlowRules() {
        // [{"resource":"admin-service-api","resourceMode":1,"grade":1,"count":1.0,"intervalSec":60,"controlBehavior":0,"burst":0,"maxQueueingTimeoutMs":500,"paramItem":null},{"resource":"admin-service_router","resourceMode":0,"grade":1,"count":5.0,"intervalSec":60,"controlBehavior":0,"burst":0,"maxQueueingTimeoutMs":500,"paramItem":null}]
        return GatewayRuleManager.getRules();
    }

    /**
     * 获取我定义的api分组
     */
    @GetMapping("/gw/api/rules")
    public Set<ApiDefinition> getApiGroups() {
        // [{"apiName":"admin-service-api","predicateItems":[{"pattern":"/admin/login","matchStrategy":0}]}]
        return GatewayApiDefinitionManager.getApiDefinitions();
    }
}
