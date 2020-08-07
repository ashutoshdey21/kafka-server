

/*
/*
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;


//public class LongKafkaCar
public class KafkaExample
{
    private final String topic;
    private final Properties props;

    public KafkaExample(String brokers, String username, String password)
    {
        this.topic = "cars";
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        String intserializer = IntegerSerializer.class.getName(); //AS
        String deserializer = StringDeserializer.class.getName();
        String intdeserializer = IntegerDeserializer.class.getName(); //AS
        props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", username + "-consumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", deserializer);
        //props.put("value.deserializer", deserializer);
        props.put("key.serializer", serializer);
        //props.put("value.serializer", serializer);
        props.put("value.serializer", intserializer); //AS
        props.put("value.deserializer", intdeserializer);
    }

    private void consume() {
        KafkaConsumer<String, Integer> consumer = new KafkaConsumer<String, Integer>(props);
        consumer.subscribe(Arrays.asList(topic+".1", topic+".2", topic+".3"));
        while (true)
        {
            ConsumerRecords<String, Integer> records = consumer.poll(1000);
            for (ConsumerRecord<String, Integer> record : records)
            {
                System.out.printf("%s, key=%s, value=\"%s\"\n",record.topic(), record.key(), record.value());
            }
        }

    }

    private void produce() {
        Thread one = new Thread() {
            public void run() {
                try {
                    Producer<String, Integer> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        producer.send(new ProducerRecord<String, Integer>(topic+".1", "car1", i));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        one.start();
        Thread two = new Thread() {
            public void run() {
                try {
                    Producer<String, Integer> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        producer.send(new ProducerRecord<String, Integer>(topic+".2", "car2", i));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        two.start();
        Thread three = new Thread() {
            public void run() {
                try {
                    Producer<String, Integer> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        producer.send(new ProducerRecord<String, Integer>(topic+".3", "car3", i));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        three.start();
    }

    public static void main(String[] args)
    {
        String brokers = "localhost:9092";
        String username = "datasyslab";//System.getenv("CLOUDKARAFKA_USERNAME");
        String password = "";
        KafkaExample lkc = new KafkaExample (brokers, username, password);
        lkc.produce();
        lkc.consume();
    }
}
*/


/*
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class KafkaExample {
    private final String topic;
    private final Properties props;

    public KafkaExample(String brokers, String username, String password) {
        this.topic = "sensor";

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        String deserializer = StringDeserializer.class.getName();
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
//        props.put("security.protocol", "SASL_SSL");
//        props.put("sasl.mechanism", "SCRAM-SHA-256");
//        props.put("sasl.jaas.config", jaasCfg);
    }

    public void consume() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic+".1", topic+".2"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("%s [%d] offset=%d, key=%s, value=\"%s\"\n",
								  record.topic(), record.partition(),
								  record.offset(), record.key(), record.value());
			}
        }
    }

    public void produce() {
        Thread one = new Thread() {
            public void run() {
                try {
                    Producer<String, String> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        Date d = new Date();
                        producer.send(new ProducerRecord<>(topic+".1", "sensor1", Double.toString(i)));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        one.start();
        Thread two = new Thread() {
            public void run() {
                try {
                    Producer<String, String> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        Date d = new Date();
                        producer.send(new ProducerRecord<>(topic+".2", "sensor2", Double.toString(i)));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        two.start();
    }

    public static void main(String[] args) {
        String brokers = "localhost:9092";
//		String brokers = "moped-01.srvs.cloudkafka.com:9094,moped-02.srvs.cloudkafka.com:9094,moped-03.srvs.cloudkafka.com:9094";//System.getenv("CLOUDKARAFKA_BROKERS");
		String username = "datasyslab";//System.getenv("CLOUDKARAFKA_USERNAME");
        String password = "";
		KafkaExample c = new KafkaExample(brokers, username, password);
        c.produce();
        c.consume();
    }
}*/



/*
/*
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;


//public class LongKafkaCar
public class KafkaExample
{
    private final String topic;
    private final Properties props;

    public KafkaExample(String brokers, String username, String password)
    {
        this.topic = "cars";
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        String intserializer = IntegerSerializer.class.getName(); //AS
        String deserializer = StringDeserializer.class.getName();
        String intdeserializer = IntegerDeserializer.class.getName(); //AS
        props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", username + "-consumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", deserializer);
        //props.put("value.deserializer", deserializer);
        props.put("key.serializer", serializer);
        //props.put("value.serializer", serializer);
        props.put("value.serializer", intserializer); //AS
        props.put("value.deserializer", intdeserializer);
    }

    private void consume() {
        KafkaConsumer<String, Integer> consumer = new KafkaConsumer<String, Integer>(props);
        consumer.subscribe(Arrays.asList(topic+".1", topic+".2", topic+".3"));
        while (true)
        {
            ConsumerRecords<String, Integer> records = consumer.poll(1000);
            for (ConsumerRecord<String, Integer> record : records)
            {
                System.out.printf("%s, key=%s, value=\"%s\"\n",record.topic(), record.key(), record.value());
            }
        }

    }

    private void produce() {
        Thread one = new Thread() {
            public void run() {
                try {
                    Producer<String, Integer> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        producer.send(new ProducerRecord<String, Integer>(topic+".1", "car1", i));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        one.start();
        Thread two = new Thread() {
            public void run() {
                try {
                    Producer<String, Integer> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        producer.send(new ProducerRecord<String, Integer>(topic+".2", "car2", i));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        two.start();
        Thread three = new Thread() {
            public void run() {
                try {
                    Producer<String, Integer> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        producer.send(new ProducerRecord<String, Integer>(topic+".3", "car3", i));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        three.start();
    }

    public static void main(String[] args)
    {
        String brokers = "localhost:9092";
        String username = "datasyslab";//System.getenv("CLOUDKARAFKA_USERNAME");
        String password = "";
        KafkaExample lkc = new KafkaExample (brokers, username, password);
        lkc.produce();
        lkc.consume();
    }
}
*/


/*
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class KafkaExample {
    private final String topic;
    private final Properties props;

    public KafkaExample(String brokers, String username, String password) {
        this.topic = "sensor";

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);

        String serializer = StringSerializer.class.getName();
        String deserializer = StringDeserializer.class.getName();
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
//        props.put("security.protocol", "SASL_SSL");
//        props.put("sasl.mechanism", "SCRAM-SHA-256");
//        props.put("sasl.jaas.config", jaasCfg);
    }

    public void consume() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic+".1", topic+".2"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("%s [%d] offset=%d, key=%s, value=\"%s\"\n",
								  record.topic(), record.partition(),
								  record.offset(), record.key(), record.value());
			}
        }
    }

    public void produce() {
        Thread one = new Thread() {
            public void run() {
                try {
                    Producer<String, String> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        Date d = new Date();
                        producer.send(new ProducerRecord<>(topic+".1", "sensor1", Double.toString(i)));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        one.start();
        Thread two = new Thread() {
            public void run() {
                try {
                    Producer<String, String> producer = new KafkaProducer<>(props);
                    int i = 0;
                    while(true) {
                        Date d = new Date();
                        producer.send(new ProducerRecord<>(topic+".2", "sensor2", Double.toString(i)));
                        Thread.sleep(1000);
                        i++;
                    }
                } catch (InterruptedException v) {
                    System.out.println(v);
                }
            }
        };
        two.start();
    }

    public static void main(String[] args) {
        String brokers = "localhost:9092";
//		String brokers = "moped-01.srvs.cloudkafka.com:9094,moped-02.srvs.cloudkafka.com:9094,moped-03.srvs.cloudkafka.com:9094";//System.getenv("CLOUDKARAFKA_BROKERS");
		String username = "datasyslab";//System.getenv("CLOUDKARAFKA_USERNAME");
        String password = "";
		KafkaExample c = new KafkaExample(brokers, username, password);
        c.produce();
        c.consume();
    }
}*/
