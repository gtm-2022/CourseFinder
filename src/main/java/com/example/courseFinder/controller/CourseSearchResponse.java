package com.example.courseFinder.controller;

import com.example.courseFinder.document.CourseDocument;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CourseSearchResponse {
    private long total;
    private List<CourseDocument> courses;
}
