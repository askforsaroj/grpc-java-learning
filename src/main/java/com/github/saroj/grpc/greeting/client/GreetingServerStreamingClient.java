package com.github.saroj.grpc.greeting.client;

import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingServerStreamingClient {

    public static void main(String[] args) {

        System.out.println("Hello I'm gRPC Streaming Client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50050).usePlaintext().build();

        GreetServiceGrpc.GreetServiceBlockingStub greetStreamingClient = GreetServiceGrpc.newBlockingStub(channel);

        GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("Saroj").build()).build();

        greetStreamingClient.greetManyTimes(greetManyTimesRequest).forEachRemaining(greetManyTimesResponse -> {
            System.out.println(greetManyTimesResponse.getResult());
        });

        channel.shutdown();

    }
}
