package com.example.courseFinder.controller;

import com.example.courseFinder.document.CourseDocument;
import com.example.courseFinder.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
// /api/search
    @GetMapping
    public Map<String, Object> searchCourses(
            @RequestParam(value = "q", required = false) String keyword,
            @RequestParam(value = "minAge", required = false) Integer minAge,
            @RequestParam(value = "maxAge", required = false) Integer maxAge,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Instant startInstant = (startDate != null && !startDate.isEmpty()) ? Instant.parse(startDate) : null;

        SearchHits<CourseDocument> hits = courseService.searchCourses(
                keyword, minAge, maxAge, category, type, minPrice, maxPrice, startInstant, sort, page, size
        );

        Map<String, Object> response = new HashMap<>();
        response.put("total", hits.getTotalHits());
        response.put("courses", hits.getSearchHits().stream().map(hit -> hit.getContent()).toList());

        return response;
    }


     //  /api/search/suggest?q=phy

    @GetMapping("/suggest")
    public List<String> suggestTitles(@RequestParam("q") String partialTitle) {
        return courseService.suggestTitles(partialTitle);
    }
}
