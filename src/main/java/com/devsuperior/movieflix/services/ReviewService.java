package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository repository; 
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private AuthService authService;
	
	@Transactional(readOnly = true)
	public Page<ReviewDTO> findAllPaged(Pageable pageable) {
		Page<Review> list = repository.findAll(pageable);
		return list.map(x -> new ReviewDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ReviewDTO findById(Long id) {		
		Optional<Review> obj = repository.findById(id);
		Review entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ReviewDTO(entity);
	}
	
	@Transactional(readOnly = true)
    public List<ReviewDTO> findByIdReviews(Long movieId) {
        try {
            Movie movie = movieRepository.getOne(movieId);
            List<Review> list = repository.findByMovie(movie);
            return list.stream().map(x -> new ReviewDTO(x)).collect(Collectors.toList());
        } 
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + movieId);
        }
    }

	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		Review entity = new Review();
		entity.setText(dto.getText());
		entity.setMovie(movieRepository.getOne(dto.getMovieId()));
		entity.setUser(authService.authenticated());
		entity =repository.save(entity);
		
		return new ReviewDTO(entity);
	}
	
}
