package mqtt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadPublisher {
	
	/**
	 * 
	 * @param
	 */
	public static void main(String[] args) {
		if (args == null) {
			System.out.println("Arguments ...");
			System.out.println("arg 0: broker of emq; eg: tcp://10.37.151.21:1883");
			System.out.println("arg 1: clientsCount; default 500");
			System.out.println("arg 2: requestsCount; default 1000");
			System.out.println("arg 3: topic; default \"topic\"");
			System.exit(1);
		}
		
		String broker = "tcp://10.37.151.21:1883";
		int clientsCount = 5;
		int requestsCount = 200;
		String topic = "topic";
		
		switch(args.length) { 
	        case 4: 
	        	topic = args[3];
	        case 3:
	        	requestsCount = Integer.parseInt(args[2]);
	        case 2:
	        	clientsCount = Integer.parseInt(args[1]);
	        case 1:
	        	broker = args[0];
	        	break;
	        default: 
	            // nothing
        } 
		
        ExecutorService executor = Executors.newFixedThreadPool(clientsCount);
        for (int i = 0; i < clientsCount; i++) {
        	MqttPublishSample worker = new MqttPublishSample();
        	worker.setClientId("clientId_" + clientsCount);
        	worker.setQos(1);
        	worker.setRequestsCount(requestsCount);
        	worker.setBroker(broker);
        	worker.setTopic(topic);
            executor.execute(worker);
          }
        executor.shutdown();
        while (!executor.isTerminated()) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        System.out.println("Finished all threads");
    }
	
}
