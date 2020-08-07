
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class KafkaExample {
    private final String topicP1, topicP2, consumerTopic;
    private final Properties props;


    public KafkaExample(String brokers, String username, String password)
    {
        this.topicP1 = "streams-plaintext-input";
        this.topicP2 = "yellow_trip_2";

        this.consumerTopic = "";


        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        //String intserializer = IntegerSerializer.class.getName(); //AS
        String deserializer = StringDeserializer.class.getName();
        //String intdeserializer = IntegerDeserializer.class.getName(); //AS
        props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", username + "-consumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", deserializer);
        props.put("value.deserializer", deserializer);
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        //props.put("value.serializer", intserializer); //AS
        //props.put("value.deserializer", intdeserializer);
        //props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
    }
    public void consume()
    {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String,String>(props);
        //consumer.subscribe(Collections.singletonList(topicP));
        consumer.subscribe(Arrays.asList(topicP1, topicP2));
        //consumer.subscribe(Arrays.asList("test1"));
        System.out.println(topicP1);
        while (true)
        {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String,String> record : records)
            {
                //System.out.printf("key=%s, value=%d"+"\n",record.key(), record.value());
                System.out.printf("%s,%s",record.topic(),record.value());
                System.out.println();
            }
        }
    }
    public void produce () {
        Thread one = new Thread()
        {
            public void run() {
                try {
                    Producer<String, String> producer = new KafkaProducer<>(props);
                    BufferedReader reader;
                    String csvFile = "/mnt/c/apache_kafka/data/yellow_trip_1.csv";
                    reader = new BufferedReader(new FileReader(csvFile));
                    while(true) {
                        String line = reader.readLine();
                        ProducerRecord<String, String> data = new ProducerRecord<>(topicP1, "Data_yellow_trip_1", line);
                        producer.send(data);
                        reader.readLine();
                        Thread.sleep(1000);
                    }
                }          catch (InterruptedException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };one.start();
        Thread two = new Thread()
        {
            public void run() {
                try {
                    Producer<String, String> producer = new KafkaProducer<>(props);
                    BufferedReader reader;
                    String csvFile = "/mnt/c/apache_kafka/data/yellow_trip_2.csv";
                    reader = new BufferedReader(new FileReader(csvFile));
                    while(true) {
                        String line = reader.readLine();
                        ProducerRecord<String, String> data = new ProducerRecord<>(topicP2, "Data_yellow_trip_2", line);
                        producer.send(data);
                        reader.readLine();
                        Thread.sleep(1000);
                    }
                }          catch (InterruptedException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };two.start();
    }

    public static void main(String[] args) throws InterruptedException {
        String brokers = "localhost:9092";
        String username = "datasyslab";//System.getenv("CLOUDKARAFKA_USERNAME");
        String password = "";
        KafkaExample kc = new KafkaExample(brokers, username, password);
        kc.produce();
//        kc.consume();
    }
}


