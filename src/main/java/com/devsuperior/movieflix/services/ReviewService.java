package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

public class ReviewService {
	
	@Autowired
	private ReviewRepository repository; 
	
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
	
}
