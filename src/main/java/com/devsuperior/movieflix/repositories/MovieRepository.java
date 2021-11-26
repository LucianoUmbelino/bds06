package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{

	@Query("SELECT obj "
		 + "  FROM Movie obj "
		 + " WHERE :genre IS NULL OR obj.genre = :genre "
		 + " ORDER BY obj.title ")
    Page<Movie> find(Genre genre, Pageable pageable);
	
	@Query("SELECT obj "
		 + "  FROM Movie obj "
		 + "  JOIN FETCH obj.genre "
		 + " WHERE obj IN :movies")
    List<Movie> findMoviesAndGenres(List<Movie> movies);
	
	@Query("SELECT obj "
	  	 + "  FROM Review obj "
	  	 + "  JOIN FETCH obj.user "
	  	 + " WHERE obj.movie = :movie")
    List<Review> findByMovie(Movie movie);
	
}
