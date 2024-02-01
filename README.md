<h2>Test dependencies</h2>
```
testImplementation 'org.camunda.bpm.springboot:camunda-bpm-spring-boot-starter-test:7.20.0'
testImplementation 'org.camunda.bpm.extension.scenario.project:camunda-platform-scenario-test-7.12:2.0.0.alpha.2'
testImplementation 'org.camunda.bpm.extension.scenario:camunda-platform-scenario-runner:2.0.0.alpha.2'
testImplementation 'org.camunda.bpm.extension.mockito:camunda-bpm-mockito:5.16.0'
testImplementation 'org.camunda.bpm.extension:camunda-bpm-assert:1.2'
```
<h2>camunda.cfg.xml</h2>
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="processEngineConfiguration" class="org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration">
        <property name="history" value="full" />
        <property name="expressionManager">
            <bean class="org.camunda.bpm.engine.test.mock.MockExpressionManager"/>
        </property>
        <property name="processEnginePlugins">
            <list></list>
        </property>
        <property name="historyTimeToLive" value="P1D"/>
    </bean>
</beans>
```
<h2>Coverage</h2>
```
    testImplementation 'org.camunda.bpm.extension:camunda-bpm-process-test-coverage-junit5:1.0.3'
    
    org.camunda.bpm.extension.process_test_coverage.engine.ProcessCoverageInMemProcessEngineConfiguration
```
<h2>Links<h2>
```
https://training.camunda.com/java-dev/master/exercise3a.html
https://docs.camunda.org/manual/7.20/user-guide/testing/assert-examples/
```
