package com.app.grpc.aggregator.service;

import com.app.grpc.aggregator.dto.RecommendedMovie;
import com.app.grpc.aggregator.dto.UserGenre;
import com.app.grpc.common.Genre;
import com.app.grpc.movie.MovieSearchRequest;
import com.app.grpc.movie.MovieSearchResponse;
import com.app.grpc.movie.MovieServiceGrpc;
import com.app.grpc.user.UserGenreUpdateRequest;
import com.app.grpc.user.UserResponse;
import com.app.grpc.user.UserSearchRequest;
import com.app.grpc.user.UserServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getUserMovieSuggestions(String loginId){
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse = this.userStub.getUserGenre(userSearchRequest);
        MovieSearchResponse movieSearchResponse = this.movieStub.getMovies(MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build());
        List<RecommendedMovie> movieList = movieSearchResponse.getMovieList().stream().map(movieDto ->
                new RecommendedMovie(movieDto.getTitle(), movieDto.getYear(), movieDto.getRating())
        ).collect(Collectors.toList());
        return movieList;
    }

    public void setUserGenre(UserGenre userGenre){
        UserResponse response = this.userStub.updateUserGenre(UserGenreUpdateRequest.newBuilder().setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase())).build());
    }

}
