package kafka.lab.service;



import kafka.lab.events.v1.UploadCompleted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "uploadedfile";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(UploadCompleted message) {
        this.kafkaTemplate.send(TOPIC, message);
    }
}
