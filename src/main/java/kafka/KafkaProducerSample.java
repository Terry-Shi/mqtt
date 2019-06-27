package kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerSample {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");// broker的地址
		props.put("acks", "all");
		props.put("retries", Integer.MAX_VALUE);
		props.put("max.in.flight.requests.per.connection", 1);
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("schema.registry.url", "http://localhost:8081");
		
		KafkaProducer kafkaProducer = new KafkaProducer<>(props);
		
		// topic，value
		kafkaProducer.send(new ProducerRecord<>("udemy-reviews", ""));
		
	}
}
