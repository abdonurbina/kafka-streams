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
        UploadCompleted uploadCompleted = new UploadCompleted();
        for (int i=0;i<1500;i++){
            uploadCompleted.setUploadId("123");
            uploadCompleted.setFileStore("local");
            uploadCompleted.setFileId("456");
            uploadCompleted.setTargetFileStore("s3");
            uploadCompleted.setTargetFileId("789");
            uploadCompleted.setFileName("example.txt");
            producerService.sendMessage(message);
        }




    }
}
