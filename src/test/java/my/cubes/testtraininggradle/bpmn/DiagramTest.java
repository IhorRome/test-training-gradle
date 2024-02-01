package my.cubes.testtraininggradle.bpmn;

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
import org.camunda.bpm.scenario.delegate.TaskDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(ProcessEngineCoverageExtension.class)
@ExtendWith(ProcessEngineExtension.class)
@ExtendWith(MockitoExtension.class)
@Deployment(resources = {"bpmn/diagram.bpmn"})
public class DiagramTest {

    ProcessScenario processScenario = mock(ProcessScenario.class);

    @Mock
    private CheckWeatherDelegate checkWeatherDelegate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mocks.register("checkWeatherDelegate", checkWeatherDelegate);
    }

    @Test
    void test() throws Exception {
        doAnswer(invocation -> {
            DelegateExecution execution = invocation.getArgument(0);
            execution.setVariable("weather", "good");
            return invocation;
        }).when(checkWeatherDelegate).execute(any(DelegateExecution.class));

        ProcessExpressions.registerCallActivityMock("GetFunActivity")
                .onExecutionAddVariable("activity", "swimming")
                .deploy(processEngine());

        when(processScenario.waitsAtReceiveTask("WaitForAnything")).thenReturn(EventSubscriptionDelegate::receive);

        when(processScenario.waitsAtUserTask("CheckResults")).thenReturn(TaskDelegate::complete);

        Scenario scenario = Scenario.run(processScenario)
                .startByKey("PlanWeekend")
                .execute();
        ProcessInstance instance = scenario.instance(processScenario);
        assertThat(instance).isEnded();
        verify(processScenario, times(1)).hasFinished("WaitForAnything");
    }

}
