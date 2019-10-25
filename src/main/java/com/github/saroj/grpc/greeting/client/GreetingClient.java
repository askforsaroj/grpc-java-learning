package com.github.saroj.grpc.greeting.client;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I'm gRPC Client");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50050).usePlaintext().build();

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        Greeting greeting = Greeting.newBuilder().setFirstName("Saroj").setLastName("Kumar").build();
        GreetRequest greetRequest =GreetRequest.newBuilder().setGreeting(greeting).build();
        GreetResponse greetResponse = greetClient.greet(greetRequest);

        System.out.println(greetResponse);


        System.out.println("Shutting Down Client");
        channel.shutdown();
    }
}
