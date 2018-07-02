package com.ferrovial.digitalhub.travel;


import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TravelPipeline {
    private static final Logger LOG = LoggerFactory.getLogger(TravelPipeline.class);
    static final int WINDOW_SIZE = 10;  // Default window duration in minutes
    /**
     * Specific pipeline options.
     */
    private interface Options extends PipelineOptions {
        @Description("Kafka Bootstrap Servers")
        @Default.String("40.113.158.168:9092")
        String getKafkaServer();
        void setKafkaServer(String value);

        @Description("Kafka Topic Name")
        @Default.String("taxi_rides")
        String getTaxiRidesTopic();
        void setTaxiRidesTopic(String value);

        @Description("Kafka Topic Name")
        @Default.String("taxi_fares")
        String getTaxiFaresTopic();
        void setTaxiFaresTopic(String value);

        @Description("Kafka Output Topic Name")
        @Default.String("taxi_output")
        String getOutputTopic();
        void setOutputTopic(String value);

        @Description("Pipeline duration to wait until finish in seconds")
        @Default.Long(-1)
        Long getDuration();
        void setDuration(Long duration);
        @Description("Fixed window duration, in minutes")
        @Default.Integer(WINDOW_SIZE)
        Integer getWindowSize();
        void setWindowSize(Integer value);

    }


    private static String transformTravel(String line) {
        String[] data= line.split(",");
        return line;
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
        PCollection<String> taxiRidesStream =
                pipeline
                        .apply("ReadFromKafka", KafkaIO.<String, String>read()
                                .withKeyDeserializer(StringDeserializer.class)
                                .withValueDeserializer(StringDeserializer.class)
                                .withBootstrapServers(options.getKafkaServer())
                                .withTopics(Collections.singletonList(options.getTaxiRidesTopic()))
                                .updateConsumerProperties(consumerProperties)
                                .withoutMetadata()
                        )
                        .apply("ExtractPayload", Values.<String>create());

        // now we connect to the queue and process every event
        PCollection<String> taxiFaresStream =
                pipeline
                        .apply("ReadFromKafka", KafkaIO.<String, String>read()
                                .withKeyDeserializer(StringDeserializer.class)
                                .withValueDeserializer(StringDeserializer.class)
                                .withBootstrapServers(options.getKafkaServer())
                                .withTopics(Collections.singletonList(options.getTaxiFaresTopic()))
                                .updateConsumerProperties(consumerProperties)
                                .withoutMetadata()
                        )
                        .apply("ExtractPayload", Values.<String>create());


        PCollection<KV<String,String>> rides =
                taxiRidesStream.apply("getRides", ParDo.of(new DoFn<String, KV<String,String>>() {
                    @ProcessElement
                    public void processElement(ProcessContext c){
                        c.output(TravelUtils.mapRidesData(c.element(),c.timestamp()));
                    }
                })).apply(Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSize()))));

        PCollection<KV<String,String>> fares =
                taxiFaresStream.apply("getFares", ParDo.of(new DoFn<String, KV<String,String>>() {
                    @ProcessElement
                    public void processElement(ProcessContext c){
                        c.output(TravelUtils.mapFaresData(c.element(),c.timestamp()));
                    }
                })).apply(Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSize()))));
                //})).apply(Window.into(Sessions.withGapDuration(Duration.standardMinutes(options.getWindowSize()))));


        final TupleTag<String> faresTag = new TupleTag<>();
        final TupleTag<String> ridesTag = new TupleTag<>();

        PCollection<KV<String, CoGbkResult>> kvpCollection =
                KeyedPCollectionTuple.of(faresTag, fares)
                        .and(ridesTag, rides)
                        .apply(CoGroupByKey.create());

        PCollection<KV<String,String>> taxi =
                kvpCollection.apply("join", ParDo.of(new DoFn<KV<String,CoGbkResult>, KV<String,String>>() {
                    @ProcessElement
                    public void processElement(ProcessContext c){
                        //String text  = transformTravel(c.element());
                        try {
                            String rides = TravelUtils.getValueTag(c.element().getValue(), ridesTag);
                            String fares = TravelUtils.getValueTag(c.element().getValue(), faresTag);
                            if (!rides.isEmpty() && !fares.isEmpty()) {
                                c.output(TravelUtils.joinTaxiSources(c.element(), rides, fares));
                            }
                        }
                        catch (IllegalArgumentException e)
                        {
                            LOG.info(e.getMessage());
                        }
                    }
                }));


        /**
         * write text in topic
         */

        taxi.apply("WriteToKafka",
                KafkaIO.<String, String>write()
                        .withBootstrapServers(options.getKafkaServer())
                        .withTopic(options.getOutputTopic())
                        .withKeySerializer(org.apache.kafka.common.serialization.StringSerializer.class)
                        .withValueSerializer(org.apache.kafka.common.serialization.StringSerializer.class));
        PipelineResult pipelineResult = pipeline.run();
        pipelineResult.waitUntilFinish(Duration.standardSeconds(options.getDuration()));
    }
}
