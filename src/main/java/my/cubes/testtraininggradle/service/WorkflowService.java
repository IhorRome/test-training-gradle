package my.cubes.testtraininggradle.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final RuntimeService runtimeService;

    private final TaskService taskService;

    public void clearInstancesByKey(String processDefinitionKey) {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        processInstances.forEach(pi -> runtimeService.deleteProcessInstance(pi.getId(), "test"));
    }

    public void correlateMessage(String messageName, String processDefinitionKey) {

        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        list.forEach(pi -> {
        runtimeService.createMessageCorrelation(messageName)
                .processInstanceId(pi.getId())
                .correlate();
        });
    }

    public void completeUserTask(String processInstanceId) {
        taskService.createTaskQuery().processInstanceId(processInstanceId).list().forEach(task -> {
            taskService.complete(task.getId());
        });
    }

    public void startProcess10(String processDefinitionKey) {
        for (int i = 0; i < 10; i++) {
            runtimeService.startProcessInstanceByKey(processDefinitionKey);
        }
    }

    public void completeAllUserTasks(String processDefinitionKey) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        list.forEach(pi -> {
            taskService.createTaskQuery().processInstanceId(pi.getId()).list().forEach(task -> {
                taskService.complete(task.getId());
            });
        });
    }
}
