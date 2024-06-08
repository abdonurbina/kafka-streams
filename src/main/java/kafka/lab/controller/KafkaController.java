package kafka.lab.controller;




import kafka.lab.events.v1.UploadCompleted;
import kafka.lab.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService producerService;

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestBody UploadCompleted message) {

            producerService.sendMessage(message);





    }
}
