package my.cubes.testtraininggradle.service;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class GetActivityService {

    public String getActivity() {
        Random random = new Random();
        int i = random.nextInt(3);
        return switch (i) {
            case 0 -> "swimming";
            case 1 -> "running";
            case 2 -> "cycling";
            default -> "unknown";
        };
    }
}
