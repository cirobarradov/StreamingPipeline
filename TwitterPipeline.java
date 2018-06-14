package com.ferrovial.digitalhub.twitter;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterPipeline {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterPipeline.class);
    /**
     * Specific pipeline options.
     */
    private interface Options extends PipelineOptions {
        @Description("Kafka Bootstrap Servers")
        @Default.String("52.166.0.40:9092")
        String getKafkaServer();
        void setKafkaServer(String value);

        @Description("Kafka Topic Name")
        @Default.String("twitter_input")
        String getInputTopic();
        void setInputTopic(String value);

        @Description("Kafka Output Topic Name")
        @Default.String("twitter_output")
        String getOutputTopic();
        void setOutputTopic(String value);

        @Description("Pipeline duration to wait until finish in seconds")
        @Default.Long(-1)
        Long getDuration();
        void setDuration(Long duration);

       /* class GDELTFileFactory implements DefaultValueFactory<String> {
            public String create(PipelineOptions options) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                return format.format(new Date());
            }
        }*/
    }




    public static void main(String[] args) throws Exception {
        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        LOG.info(options.toString());


        /**
         * Create Pipeline Object
         */
        Pipeline pipeline = Pipeline.create(options);

        /**
         * Create input stream
         */
        Map<String, Object> consumerProperties = new HashMap<>();
        // "auto.offset.reset" -> "earliest" = from-beginning
        consumerProperties.put("auto.offset.reset","earliest");

        // now we connect to the queue and process every event
        PCollection<String> inputStream =
                pipeline
                        .apply("ReadFromKafka", KafkaIO.<String, String>read()
                                .withKeyDeserializer(StringDeserializer.class)
                                .withValueDeserializer(StringDeserializer.class)
                                .withBootstrapServers(options.getKafkaServer())
                                .withTopics(Collections.singletonList(options.getInputTopic()))
                                .updateConsumerProperties(consumerProperties)
                                .withoutMetadata()
                        )
                        .apply("ExtractPayload", Values.<String>create());

/*
        */
        /**
         * filter input stream to tweetsinEnglish
         */


        PCollection<String> tweetsInEnglish =
                inputStream.apply("FilterByLanguage", ParDo.of(new DoFn<String, String>() {
                    @ProcessElement
                    public void processElement(ProcessContext c){
                        //String lang=twitterUtils.getLanguage(c.element());
                        if (TwitterUtils.isAllowedLanguage(c.element()))
                        //if (lang.toUpperCase().equals(TwitterUtils.LANGUAGE))
                        {
                            c.output(c.element());
                        }

                    }
                }));


        /**
         * analyze sentiment tweets
         */

        final String key=options.getInputTopic();
        PCollection<KV<String,String>> textTweets =
                tweetsInEnglish.apply("getTextField", ParDo.of(new DoFn<String, KV<String,String>>() {
                    @ProcessElement
                    public void processElement(ProcessContext c){
                        String text  = TwitterUtils.analyzeTweet(c.element());
                        c.output(KV.of(key,text));
                        LOG.info(text);
                    }
                }));



        /**
         * write text in topic
         */
        textTweets.apply("WriteToKafka",
                KafkaIO.<String, String>write()
                        .withBootstrapServers(options.getKafkaServer())
                        .withTopic(options.getOutputTopic())
                        .withKeySerializer(org.apache.kafka.common.serialization.StringSerializer.class)
                        .withValueSerializer(org.apache.kafka.common.serialization.StringSerializer.class));
        PipelineResult pipelineResult = pipeline.run();
        pipelineResult.waitUntilFinish(Duration.standardSeconds(options.getDuration()));
    }
}
