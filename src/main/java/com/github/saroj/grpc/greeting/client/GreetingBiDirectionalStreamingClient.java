package com.github.saroj.grpc.greeting.client;

import com.proto.greet.GreetEveryoneRequest;
import com.proto.greet.GreetEveryoneResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingBiDirectionalStreamingClient {

    public static void main(String[] args) {
        System.out.println("Starting Bi-Directional Streaming Client");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50050).usePlaintext().build();
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        StreamObserver<GreetEveryoneRequest> requestObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
            @Override
            public void onNext(GreetEveryoneResponse greetEveryoneResponse) {
                System.out.println("Received Response from server");
                System.out.println(greetEveryoneResponse.getResult());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                countDownLatch.countDown();
            }
        });

        Arrays.asList("Saroj", "Steve", "Marc", "Tim").forEach(name ->
                requestObserver.onNext(GreetEveryoneRequest.newBuilder().setGreeting(Greeting.newBuilder().setFirstName(name).build()).build()));

        requestObserver.onCompleted();

        try {
            countDownLatch.await(10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
