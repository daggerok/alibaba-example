package com.github.daggerok.alibaba.app;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@SpringBootApplication
public class UrlBlockHandlerApp {
  public static void main(String[] args) {
    SpringApplication.run(UrlBlockHandlerApp.class);
  }
}

@RestController
class HelloResource {
  @GetMapping("/hello")
  public ResponseEntity hello() {
    return ResponseEntity.ok("hello!");
  }
}

@Configuration
class HelloConfig {
  @EventListener(ApplicationStartedEvent.class)
  public void secureHello() {
    final FlowRule rule = new FlowRule()
        .setGrade(RuleConstant.FLOW_GRADE_QPS)
        .setCount(2);
    rule.setResource("/hello")
        .setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
    FlowRuleManager.loadRules(Collections.singletonList(rule));
  }
}

@Component
class HelloUrlBlockHandler implements UrlBlockHandler {
  @Override
  public void blocked(HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse,
                      BlockException e) throws IOException {

    try (final PrintWriter writer = httpServletResponse.getWriter()) {
      writer.println("(>_<) Cartman is crying...");
    }
  }
}
