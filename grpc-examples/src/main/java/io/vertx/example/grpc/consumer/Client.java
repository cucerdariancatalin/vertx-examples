package io.vertx.example.grpc.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ConsumerServiceGrpc;
import io.vertx.example.grpc.Messages;
import io.vertx.example.util.Runner;
import io.vertx.grpc.client.GrpcClient;

import java.nio.charset.StandardCharsets;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() {

    // Create the client
    GrpcClient client = GrpcClient.client(vertx);

    // Call the remote service
    client.request(SocketAddress.inetSocketAddress(8080, "localhost"), ConsumerServiceGrpc.getStreamingOutputCallMethod())
      .compose(request -> {
        request
          .end(Messages
            .StreamingOutputCallRequest
            .newBuilder()
            .build());
        return request.response().compose(response -> response
          .handler(msg -> System.out.println(new String(msg.getPayload().toByteArray(), StandardCharsets.UTF_8)))
          .end());
      }).onSuccess(resp -> {
        System.out.println("Response has ended.");
      });
  }
}
