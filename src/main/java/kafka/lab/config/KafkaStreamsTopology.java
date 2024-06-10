package kafka.lab.config;


import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import kafka.lab.events.v1.UploadCompleted;
import kafka.lab.events.v1.UploadCompletedResult;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsTopology {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "proteo4.int.callcenter.processor.events");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, UploadCompleted> kStream(StreamsBuilder streamsBuilder,SpecificAvroSerde<UploadCompletedResult> uploadCompletedResultSerde,
                                                    SpecificAvroSerde<UploadCompleted> uploadCompletedSerde) {
        KStream<String, UploadCompleted> sourceStream = streamsBuilder
                                                            .stream("uploadedfile");

        // Filtrar los KStreams
        KStream<String, UploadCompleted> filteredStreamCsv = sourceStream.filter((key, value) ->
                value != null && "mystore".equals(value.getFileStore().toString()) && value.getFileName().toString().endsWith(".csv")
        );
        KStream<String, UploadCompleted> filteredStreamMp3 = sourceStream.filter((key, value) ->
                 value != null && "mystore".equals(value.getFileStore().toString()) && value.getFileName().toString().endsWith(".mp3")
        );
        // Transformar la key
        KStream<String, UploadCompleted> transformedStream1 = filteredStreamCsv.selectKey((key, value) -> value.getUploadId().toString());
        KStream<String, UploadCompleted> transformedStream2 = filteredStreamMp3.selectKey((key, value) -> value.getUploadId().toString());

        // Unir los KStreams
        KStream<String, UploadCompletedResult> joinedStream = transformedStream1.join(
                transformedStream2,
                (csvRecord, mp3Record) -> new UploadCompletedResult(csvRecord.getUploadId(), csvRecord.getFileName(), mp3Record.getFileName()),
                JoinWindows.of(Duration.ofMinutes(60)),
                StreamJoined.with(Serdes.String(), uploadCompletedSerde,uploadCompletedSerde)
                        .withName("selectkey-by-filename-store")
                        .withStoreName("by-filename-store")
        );

        // Enviar el resultado a otro tópico
        joinedStream.to("output", Produced.with(Serdes.String(), uploadCompletedResultSerde));
        // Para propósitos de depuración, imprime los resultados
        joinedStream.print(Printed.<String, UploadCompletedResult>toSysOut().withLabel("joined-stream"));
        return sourceStream;
    }

}
