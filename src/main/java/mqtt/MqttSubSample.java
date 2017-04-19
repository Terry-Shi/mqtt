package mqtt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubSample {

    public static void main(String[] args) {

        String topic        = "MQTT Examples";
        //String content      = "Message from MqttPublishSample" + new Date().toString();
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSubClient";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setUserName(userName);
//            connOpts.setPassword(password);
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            sampleClient.subscribe(topic, qos);
            System.out.println("Message subscribed");
            sampleClient.setCallback(new MyMqttCallback());
            
            try {
    		    String text = null;
    		    try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name())) {
    		        text = scanner.useDelimiter("\n").next();
    		    }
    		    System.out.print(text);
    		} catch (Exception e) {
    			//If we can't read we'll just exit
    		}
            
            sampleClient.disconnect();
            System.out.println("Disconnected");
            //System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}

