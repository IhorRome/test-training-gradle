package bpmn;

import java.util.Map;
import my.cubes.testtraininggradle.delegate.CheckWeatherDelegate;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.camunda.bpm.extension.mockito.ProcessExpressions;
import org.camunda.bpm.extension.process_test_coverage.junit5.ProcessEngineCoverageExtension;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.bpm.scenario.delegate.EventSubscriptionDelegate;
import org.camunda.bpm.scenario.delegate.ExternalTaskDelegate;
import org.camunda.bpm.scenario.delegate.TaskDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(ProcessEngineExtension.class)
@ExtendWith(ProcessEngineCoverageExtension.class)
@ExtendWith(MockitoExtension.class)
@Deployment(resources = "bpmn/diagram.bpmn")
public class PlanWeekendTest {

    ProcessScenario processScenario = mock(ProcessScenario.class);

    @Mock
    private CheckWeatherDelegate checkWeatherDelegate;

    @BeforeEach
    public void setUp() throws Exception {
        openMocks(this);
        Mocks.register("checkWeatherDelegate", checkWeatherDelegate);

        doAnswer(invocation -> {
            DelegateExecution execution = invocation.getArgument(0);
            execution.setVariable("weather", "good");
            return invocation;
        }).when(checkWeatherDelegate).execute(Mockito.any(DelegateExecution.class));

    }
    @Test
    public void weatherIsGoodSwimmingTest() throws Exception {


        ProcessExpressions.registerCallActivityMock("GetFunActivity")
                .onExecutionAddVariable("activity", "swimming")
                .deploy(processEngine());

        when(processScenario.waitsAtReceiveTask("WaitForAnything"))
                .thenReturn(EventSubscriptionDelegate::receive);

        when(processScenario.waitsAtUserTask("Activity_1bbo09w")).thenReturn(TaskDelegate::complete);

        Scenario scenario = Scenario.run(processScenario)
                .startByMessage("PlanWeekend")
                .execute();
        ProcessInstance instance = scenario.instance(processScenario);

        assertThat(instance).isEnded();
        assertThat(instance).hasVariables("todo");
        assertThat(instance).hasPassed("FigureOutActivity");
        assertThat(instance).hasNotPassed("WatchMovies");
    }

    @Test
    void weatherIsBadTest() throws Exception {
        doAnswer(invocation -> {
            DelegateExecution execution = invocation.getArgument(0);
            execution.getCurrentActivityId();
            execution.setVariable("weather", "bad");
            return invocation;
        }).when(checkWeatherDelegate).execute(Mockito.any(DelegateExecution.class));

        when(processScenario.waitsAtUserTask("WatchMovies"))
                .thenReturn(task -> task.complete(Map.of("activity", "watch movies")));

        when(processScenario.waitsAtReceiveTask("WaitForAnything"))
                .thenReturn(EventSubscriptionDelegate::receive);

        when(processScenario.waitsAtUserTask("Activity_1bbo09w")).thenReturn(TaskDelegate::complete);

        Scenario scenario = Scenario.run(processScenario)
                .startByKey("PlanWeekend")
                .execute();
        ProcessInstance instance = scenario.instance(processScenario);

        when(processScenario.waitsAtServiceTask("CheckWeather")).thenReturn(task -> task.complete(Map.of("ss", "ss")));
        assertThat(instance).isEnded();
        assertThat(instance).hasVariables("todo");
        assertThat(instance).hasNotPassed("FigureOutActivity");
        assertThat(instance).hasPassed("WatchMovies");



        when(processScenario.waitsAtBusinessRuleTask("Activity_1iguwq4")).thenReturn(task -> {
            task.
        });
    }
}
