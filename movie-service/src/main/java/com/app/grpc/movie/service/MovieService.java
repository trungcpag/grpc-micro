package com.app.grpc.movie.service;

import com.app.grpc.movie.MovieDto;
import com.app.grpc.movie.MovieSearchRequest;
import com.app.grpc.movie.MovieSearchResponse;
import com.app.grpc.movie.MovieServiceGrpc;
import com.app.grpc.movie.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDto> movieDtoList = this.movieRepository.getMoviesByGenreOrderByYearDesc(request.getGenre().toString())
                .stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setYear(movie.getYear())
                        .setRating(movie.getRating()).build())
                .collect(Collectors.toList());
        responseObserver.onNext(MovieSearchResponse.newBuilder().addAllMovie(movieDtoList).build());
        responseObserver.onCompleted();
    }
}
