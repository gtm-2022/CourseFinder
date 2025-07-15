package com.example.courseFinder.config;

import com.example.courseFinder.document.CourseDocument;
import com.example.courseFinder.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseDataLoader {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadCourses() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("sample-courses.json");
        List<CourseDocument> courses = objectMapper.readValue(is, new TypeReference<>() {});
        // Add suggest field
        courses = courses.stream()
                .peek(c -> c.setSuggest(new Completion(List.of(c.getTitle()))))
                .collect(Collectors.toList());
        courseRepository.saveAll(courses);
        System.out.println("Sample courses indexed successfully");
    }
}
