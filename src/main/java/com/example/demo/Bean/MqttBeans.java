package com.example.demo.Bean;


import com.example.demo.Service.CheckInOutServiceInterface;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class MqttBeans {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private CheckInOutServiceInterface checkInOutServiceInteface;
    private String cardNumber;
    private String licensePalate;
    @Bean
    public MqttPahoClientFactory mqttClientFactory(){

        // Đây là một factory class cung cấp cách tạo và cấu hình MQTT client.
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        // Đối tượng MqttConnectOptions dùng để  cấu hình các tùy chọn kết nối MQTT.
        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[] { "ssl://0e0034e9177c429bbaefeaf9a1aabfa7.s1.eu.hivemq.cloud:8883" });
        options.setUserName("b20dccn099");

        String pass = "b20DCCN099";
        options.setPassword(pass.toCharArray());

        options.setCleanSession(true);

        //  khi tạo MQTT client, factory sẽ sử
        //  dụng các thông số này để cấu hình client.
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        // "serverIn" : clientID, có thể đặt tùy thích miễn sao tên client là duy nhất để máy chủ biết đó là client nào.
        // mqttClientFactory():  Đối tượng này sẽ được sử dụng để tạo MQTT client cho adapter.
        //# :  cho phép adapter nhận tất cả các tin nhắn từ tất cả các chủ đề.
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                mqttClientFactory(), "#");

        //  Đặt thời gian kết thúc cho adapter là (5 giây).
        adapter.setCompletionTimeout(5000);

        adapter.setConverter(new DefaultPahoMessageConverter());

        // Đặt mức độ đảm bảo chất lượng dịch vụ của adapter thành 2.
        // QoS 2 đại diện cho mức đảm bảo cao nhất trong MQTT, đảm bảo rằng
        // mỗi tin nhắn sẽ được gửi đúng một lần và theo đúng thứ tự.
        adapter.setQos(2);

        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    @SendTo("/topic/message")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                //  một Message có thể chứa nhiều tiêu đề khác nhau để đính
                // kèm thông tin bổ sung về tin nhắn, nhưng nó chỉ có một phần dữ liệu được biểu diễn bởi payload
                // MqttHeaders.RECEIVED_TOPIC: là một hằng số  đại diện cho tiêu đề chứa thông tin về
                // chủ đề của tin nhắn MQTT đã nhận.
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();

                if(topic.equals("IdCard")) {
                    cardNumber= message.getPayload().toString();
                    System.out.println("Card number: " + cardNumber);
                }

                if (topic.equals("detected_plate_topic")) {
                    licensePalate = message.getPayload().toString();
                    licensePalate = licensePalate.replaceAll("[\\s-+.^:,]", "");
                    System.out.println("Licence palate " + licensePalate);
                    checkInOut();
                }

//                if (!cardNumber.equals("") && !licensePalate.equals("")) {
//                    checkInOutServiceInteface.checkInOut(cardNumber, licensePalate);
//                }

                if (topic.equals("parkingSlot")) {
                    String payload = message.getPayload().toString();
                    System.out.println("Parking slot " + payload);
                }
            }

        };
    }

    public void checkInOut () {
        if (!cardNumber.isEmpty() && !licensePalate.isEmpty()) {
            checkInOutServiceInteface.checkInOut(cardNumber, licensePalate);
        }
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        // mqttClientFactory(): Đây là một phương thức được gọi để lấy một
        // đối tượng MqttPahoClientFactory.
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());

        // Đặt trình xử lý dịch vụ là bất đồng bộ (async). Điều này có
        // nghĩa rằng việc gửi tin nhắn MQTT sẽ không chờ đợi cho đến khi
        // tin nhắn được xử lý hoàn tất mà sẽ tiếp tục thực hiện các công
        // việc khác ngay lập tức.
        messageHandler.setAsync(true);

        messageHandler.setDefaultTopic("#");

        //  Đặt thuộc tính "retained" mặc định cho tin nhắn MQTT.
        //  Thuộc tính này xác định liệu tin nhắn được lưu trữ lại trên
        //  máy chủ MQTT sau khi nó được gửi đến hoặc không.
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }

}

