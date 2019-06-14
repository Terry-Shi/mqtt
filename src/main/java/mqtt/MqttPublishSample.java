package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample implements Runnable{
	
	public String topic        = "/MQTT_Examples";
	public int qos             = 2;
    //public String broker       = "tcp://localhost:1883";//"tcp://10.19.130.5:1883"; //"tcp://10.19.139.125:1883"; //"tcp://10.19.138.145:1883";
	public String broker       =  "tcp://218.92.191.5:31200"; //"tcp://emqttd-service-all.cust-pro.cloud.he2.io:1883"; //"tcp://10.19.248.34:1883";   ////"tcp://10.37.151.23:1883";
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
		MqttClient sampleClient = null;
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // inflight 这个是客户端飞行窗口的概念，如果你设置了飞行窗口是10，那么如果你发布了10条消息的qos都是>0的，而还没有收到服务器回复的publish的ack,这里的飞行窗口就会存满
            connOpts.setMaxInflight(1000);// related to error code REASON_CODE_MAX_INFLIGHT=32202
            connOpts.setUserName("admin3");
            connOpts.setPassword("lghlmcl".toCharArray()); 
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);
            System.out.println("Connected to broker: "+ broker + " for " + this.clientId);
            
            for (int i=0; i<requestsCount; i++) {
            	String content = "Message from " + this.clientId + "-" + i;
	            MqttMessage message = new MqttMessage(content.getBytes());
	            message.setQos(qos);
	            message.setRetained(true);
	            sampleClient.publish(topic, message);
	            System.out.println("Message "+i+" published from client " + this.clientId);
	            //Thread.sleep(10);
            }
        } catch(MqttException me) {
        	//System.err.println("clientId "+ clientId);
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage() + " for client " + this.clientId);
            //System.out.println("loc "+me.getLocalizedMessage());
            //System.out.println("cause "+me.getCause());
            //System.out.println("excep "+me);
            //me.printStackTrace();
//        } catch (InterruptedException e) {
//			e.printStackTrace();
		} finally {
			try {
				sampleClient.disconnect();
				System.out.println("Disconnected for " + this.clientId);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
    
}