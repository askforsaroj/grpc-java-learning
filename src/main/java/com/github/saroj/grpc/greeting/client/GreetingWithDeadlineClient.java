package com.github.saroj.grpc.greeting.client;

import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.GreetWithDeadlineRequest;
import com.proto.greet.GreetWithDeadlineResponse;
import com.proto.greet.Greeting;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class GreetingWithDeadlineClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50050).usePlaintext().build();
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        GreetWithDeadlineResponse greetWithDeadlineResponse = greetClient.withDeadline(Deadline.after(3000, TimeUnit.MILLISECONDS))
                .greetWithDeadline(GreetWithDeadlineRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("Saroj").build()).build());
        System.out.println(greetWithDeadlineResponse.getResult());

        try {
            greetWithDeadlineResponse = greetClient.withDeadline(Deadline.after(200, TimeUnit.MILLISECONDS))
                    .greetWithDeadline(GreetWithDeadlineRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName("Saroj").build()).build());
            System.out.println(greetWithDeadlineResponse.getResult());
        } catch (StatusRuntimeException e) {
            if (e.getStatus() == Status.DEADLINE_EXCEEDED) {
                System.out.println("Deadline Exceeded, no need of response");
            } else {
                e.printStackTrace();
            }

        }

    }
}
