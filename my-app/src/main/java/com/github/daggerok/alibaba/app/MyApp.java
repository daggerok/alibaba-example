package com.github.daggerok.alibaba.app;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@SpringBootApplication
public class MyApp {
  public static void main(String[] args) {
    SpringApplication.run(MyApp.class);
  }
}

@RestController
class HelloResource {
  @GetMapping("/hey")
  public ResponseEntity hey() {
    return ResponseEntity.ok("hey!");
  }
}

@Configuration
class HeyConfig {

  @EventListener(ApplicationStartedEvent.class)
  public void secureHey() {
    final FlowRule rule = new FlowRule()
        .setGrade(RuleConstant.FLOW_GRADE_QPS)
        .setCount(2);
    rule.setResource("/hey")
        .setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
    FlowRuleManager.loadRules(Collections.singletonList(rule));
  }
}
