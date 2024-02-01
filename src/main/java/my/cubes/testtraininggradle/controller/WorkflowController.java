package my.cubes.testtraininggradle.controller;

import lombok.RequiredArgsConstructor;
import my.cubes.testtraininggradle.service.WorkflowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @GetMapping("/clear-instances/{processDefinitionKey}")
    public void clearInstances(@PathVariable String processDefinitionKey) {
        workflowService.clearInstancesByKey(processDefinitionKey);
    }

    @GetMapping("/correlate-message/{messageName}/{processDefinitionKey}")
    public void correlateMessage(@PathVariable String messageName, @PathVariable String processDefinitionKey) {
        workflowService.correlateMessage(messageName, processDefinitionKey);
    }

    @GetMapping("/complete-user-task/{processInstanceId}")
    public void completeUserTask(@PathVariable String processInstanceId) {
        workflowService.completeUserTask(processInstanceId);
    }

    @GetMapping("/start-process-10/{processDefinitionKey}")
    public void startProcess10(@PathVariable String processDefinitionKey) {
        workflowService.startProcess10(processDefinitionKey);
    }

    @GetMapping("/complete-all-user-tasks/{processDefinitionKey}")
    public void completeAllUserTasks(@PathVariable String processDefinitionKey) {
        workflowService.completeAllUserTasks(processDefinitionKey);
    }
}
