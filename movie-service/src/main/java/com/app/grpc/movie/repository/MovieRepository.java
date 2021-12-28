package com.app.grpc.movie.repository;

import com.app.grpc.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> getMoviesByGenreOrderByYearDesc(String genre);
    List<Movie> getMovieByGenre(String genre);

}
