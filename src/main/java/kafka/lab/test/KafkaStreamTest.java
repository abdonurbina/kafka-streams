package kafka.lab.test;

import kafka.lab.events.v1.UploadCompleted;

public class KafkaStreamTest {
    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String schemaRegistryUrl = "http://localhost:8081";
        String topic = "uploadedfile";

        ProducerService producerService = new ProducerService(bootstrapServers, schemaRegistryUrl, topic);

        try {
            for (int i = 0; i < 1600; i++) {
                // Send a message with a random .mp3 file extension
                UploadCompleted messageMp3 = MessageGenerator.generateRandomMessage(".mp3");
                producerService.sendMessage(messageMp3);

                // Send a message with the same uploadId but .csv file extension
                UploadCompleted messageCsv = new UploadCompleted(
                        messageMp3.getUploadId(),
                        messageMp3.getFileStore(),
                        messageMp3.getFileId(),
                        messageMp3.getTargetFileStore(),
                        messageMp3.getTargetFileId(),
                        "example.csv"
                );
                producerService.sendMessage(messageCsv);
            }
        } finally {
            producerService.close();
        }
    }
}

