package kafka.lab.test;

import kafka.lab.events.v1.UploadCompleted;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

import java.util.Properties;

public class ProducerService {
    private final KafkaProducer<String, UploadCompleted> producer;
    private final String topic;

    public ProducerService(String bootstrapServers, String schemaRegistryUrl, String topic) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", schemaRegistryUrl);

        producer = new KafkaProducer<>(props);
    }

    public void sendMessage(UploadCompleted message) {
        producer.send(new ProducerRecord<>(topic, message.getUploadId().toString(), message));
    }

    public void close() {
        producer.close();
    }
}

