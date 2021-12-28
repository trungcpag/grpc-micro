package com.app.grpc.user.service;


import com.app.grpc.common.Genre;
import com.app.grpc.user.UserGenreUpdateRequest;
import com.app.grpc.user.UserResponse;
import com.app.grpc.user.UserSearchRequest;
import com.app.grpc.user.UserServiceGrpc;
import com.app.grpc.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.userRepository.findById(request.getLoginId()).ifPresent(user -> {
            builder.setName(user.getName())
                    .setLoginId(user.getLogin())
                    .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional //update automatic
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
