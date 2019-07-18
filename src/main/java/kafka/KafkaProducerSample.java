package kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerSample {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");// broker的地址
		props.put("acks", "all"); // 需要客户端返回回执
		props.put("retries", Integer.MAX_VALUE);
		props.put("max.in.flight.requests.per.connection", 1); // 发送端缓存
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("schema.registry.url", "http://localhost:8081");
		props.put("enable.idempotence", "true"); // 幂等性
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
		// topic，value（partition和key是可选字段）
		kafkaProducer.send(new ProducerRecord<String, String>("udemy-reviews", ""));
		kafkaProducer.close();
	}
}
