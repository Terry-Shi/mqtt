package kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * ref: https://qq1074123922.github.io/2016/07/22/kafak-new-consumer-use/
 * 
 * @author xzy
 *
 */
public class KafkaConsumerSample {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "group"); // 需要服务器端先配置／创建？
		props.put("enable.auto.commit", "false"); // 关闭自动commit
		props.put("session.timeout.ms", "30000");
		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("message_public", "test2"));
		
		final int minBatchSize = 10;
		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(100);
				int i = 0;
				for (ConsumerRecord<String, String> record : records) {
					System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(),
							record.value());
					i++;
				}
				if (i >= minBatchSize) {
					consumer.commitSync(); // 批量完成写入后，手工同步commit offset
				}
			}
		} finally {
			consumer.close();
		}
	}

	public void pollWithAutoCommit() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "group");
		props.put("enable.auto.commit", "false"); // 关闭自动commit
		props.put("session.timeout.ms", "30000");
		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("test1", "test2"));
		boolean running = true;

		try {
			while (running) {
				ConsumerRecords<String, String> records = consumer.poll(1000);
				for (ConsumerRecord<String, String> record : records)
					System.out.println(record.offset() + ": " + record.value() + " offset=" + record.offset());
			}
		} finally {
			consumer.close();

		}
	}
}
