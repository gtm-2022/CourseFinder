package com.example.courseFinder.service;

import com.example.courseFinder.document.CourseDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchHits<CourseDocument> searchCourses(
            String keyword,
            Integer minAge,
            Integer maxAge,
            String category,
            String type,
            Double minPrice,
            Double maxPrice,
            Instant startDate,
            String sort,
            int page,
            int size) {

        Criteria criteria = new Criteria();

        if (keyword != null && !keyword.isEmpty()) {
            criteria = criteria.and(new Criteria("title").matches(keyword))
                    .or(new Criteria("description").matches(keyword));
        }

        if (category != null && !category.isEmpty()) {
            criteria = criteria.and(new Criteria("category").is(category));
        }

        if (type != null && !type.isEmpty()) {
            criteria = criteria.and(new Criteria("type").is(type));
        }

        if (minAge != null || maxAge != null) {
            Criteria ageCriteria = new Criteria("minAge");
            if (minAge != null) ageCriteria = ageCriteria.greaterThanEqual(minAge);
            if (maxAge != null) ageCriteria = ageCriteria.lessThanEqual(maxAge);
            criteria = criteria.and(ageCriteria);
        }

        if (minPrice != null || maxPrice != null) {
            Criteria priceCriteria = new Criteria("price");
            if (minPrice != null) priceCriteria = priceCriteria.greaterThanEqual(minPrice);
            if (maxPrice != null) priceCriteria = priceCriteria.lessThanEqual(maxPrice);
            criteria = criteria.and(priceCriteria);
        }

        if (startDate != null) {
            criteria = criteria.and(new Criteria("nextSessionDate").greaterThanEqual(startDate.toString()));
        }

        Sort sorting;
        if ("priceAsc".equals(sort)) {
            sorting = Sort.by("price").ascending();
        } else if ("priceDesc".equals(sort)) {
            sorting = Sort.by("price").descending();
        } else {
            sorting = Sort.by("nextSessionDate").ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        CriteriaQuery query = new CriteriaQuery(criteria, pageable);

        return elasticsearchOperations.search(query, CourseDocument.class);
    }
}
