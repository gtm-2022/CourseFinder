package com.example.courseFinder.service;

import com.example.courseFinder.document.CourseDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
        int pageIndex=page>0?page-1:0;
        Pageable pageable = PageRequest.of(pageIndex, size, sorting);
        CriteriaQuery query = new CriteriaQuery(criteria, pageable);

        return elasticsearchOperations.search(query, CourseDocument.class);
    }

    public List<String> suggestTitles(String partialTitle) {
        Criteria criteria = new Criteria("title").startsWith(partialTitle);

        Pageable pageable = PageRequest.of(0, 10);
        CriteriaQuery query = new CriteriaQuery(criteria, pageable);

        SearchHits<CourseDocument> hits = elasticsearchOperations.search(query, CourseDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(hit -> hit.getContent().getTitle())
                .distinct()
                .collect(Collectors.toList());
    }
}
