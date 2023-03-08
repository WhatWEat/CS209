package OJ1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineCoursesAnalyzer {

    public OnlineCoursesAnalyzer(String datasetPath) {
    }

    public Map<String, Integer> getPtcpCountByInst() {
        return new HashMap<>();
    }

    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        return new HashMap<>();
    }

    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        return new HashMap<>();
    }

    public List<String> getCourses(int topK, String by) {
        return new ArrayList<>();
    }

    public List<String> searchCourses(String courseSubject, double
        percentAudited, double totalCourseHours) {
        return new ArrayList<>();
    }

    public List<String> recommendCourses(int age, int gender, int
        isBachelorOrHigher) {
        return new ArrayList<>();
    }
}
