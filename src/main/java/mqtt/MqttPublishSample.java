package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample implements Runnable{
	
	public String topic        = "/MQTT_Examples";
	public int qos             = 2;
    //public String broker       = "tcp://localhost";//"tcp://10.19.130.5:1883"; //"tcp://10.19.139.125:1883"; //"tcp://10.19.138.145:1883";
	public String broker       = "tcp://10.37.151.21:1883";
	public String clientId     = "clientId_MqttPublishSample"; // clientId 不可重复，否则会造成其他使用相同clientId的客户端断线。
    public int requestsCount = 1000;
	
    public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getRequestsCount() {
		return requestsCount;
	}

	public void setRequestsCount(int requestsCount) {
		this.requestsCount = requestsCount;
	}

	public static void main(String[] args) {
		MqttPublishSample pub = new MqttPublishSample();
		pub.run();
    }
	

	
	@Override
	public void run() {
		MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setUserName("client03");
//            connOpts.setPassword("psw03".toCharArray()); 
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);
            System.out.println("Connected to broker: "+broker);
            
            for (int i=0; i<requestsCount; i++) {
            	String content = "Message from MqttPublishSample " + this.clientId + "-" + i;
	            MqttMessage message = new MqttMessage(content.getBytes());
	            message.setQos(qos);
	            message.setRetained(true);
	            sampleClient.publish(topic, message);
	            System.out.println("Message published");
	            Thread.sleep(100);
            }
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
}