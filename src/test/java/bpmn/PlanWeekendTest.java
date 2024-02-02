package bpmn;

import my.cubes.testtraininggradle.delegate.CheckWeatherDelegate;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.camunda.bpm.extension.mockito.ProcessExpressions;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.camunda.bpm.scenario.delegate.EventSubscriptionDelegate;
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
@ExtendWith(MockitoExtension.class)
@Deployment(resources = "bpmn/diagram.bpmn")
public class PlanWeekendTest {

    ProcessScenario processScenario = mock(ProcessScenario.class);

    @Mock
    private CheckWeatherDelegate checkWeatherDelegate;

    @BeforeEach
    public void setUp() {
        Mocks.register("checkWeatherDelegate", checkWeatherDelegate);
        openMocks(this);
    }
    @Test
    public void weatherIsGoodSwimmingTest() throws Exception {
        doAnswer(invocation -> {
            DelegateExecution execution = invocation.getArgument(0);
            execution.setVariable("weather", "good");
            return invocation;
        }).when(checkWeatherDelegate).execute(Mockito.any(DelegateExecution.class));

        ProcessExpressions.registerCallActivityMock("GetFunActivity")
                .onExecutionAddVariable("activity", "swimming")
                .deploy(processEngine());

        when(processScenario.waitsAtReceiveTask("WaitForAnything"))
                .thenReturn(EventSubscriptionDelegate::receive);

        when(processScenario.waitsAtUserTask("Activity_1bbo09w")).thenReturn(TaskDelegate::complete);

        Scenario scenario = Scenario.run(processScenario)
                .startByKey("PlanWeekend")
                .execute();
        ProcessInstance instance = scenario.instance(processScenario);

        assertThat(instance).isEnded();
        assertThat(instance).hasVariables("todo");
        assertThat(instance).hasPassed("FigureOutActivity");
        assertThat(instance).hasNotPassed("WatchMovies");
    }
}
