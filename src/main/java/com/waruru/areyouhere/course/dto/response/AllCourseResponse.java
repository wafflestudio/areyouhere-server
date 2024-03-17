package com.waruru.areyouhere.course.dto.response;

import com.waruru.areyouhere.course.dto.CourseData;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllCourseResponse {

    private List<CourseData> courses;

    public AllCourseResponse(List<CourseData> courses) {
        this.courses = courses;
    }
}
