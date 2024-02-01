package my.cubes.testtraininggradle.delegate;

import java.util.Random;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("checkWeatherDelegate")
public class CheckWeatherDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Random random = new Random();
        boolean isGoodWeather = random.nextBoolean();
        if (isGoodWeather) {
            delegateExecution.setVariable("weather", "good");
        } else {
            delegateExecution.setVariable("weather", "bad");
        }
    }
}
