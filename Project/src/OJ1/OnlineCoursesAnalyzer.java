package OJ1;
import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is just a demo for you, please run it on JDK17.
 * This is just a demo, and you can extend and implement functions
 * based on this demo, or implement it in a different way.
 */
public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();

    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]), info[3], info[4], info[5],
                    Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]),
                    Integer.parseInt(info[9]), Integer.parseInt(info[10]), Double.parseDouble(info[11]),
                    Double.parseDouble(info[12]), Double.parseDouble(info[13]), Double.parseDouble(info[14]),
                    Double.parseDouble(info[15]), Double.parseDouble(info[16]), Double.parseDouble(info[17]),
                    Double.parseDouble(info[18]), Double.parseDouble(info[19]), Double.parseDouble(info[20]),
                    Double.parseDouble(info[21]), Double.parseDouble(info[22]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    public Map<String, Integer> getPtcpCountByInst() {
        return courses.stream().sorted((s1,s2)-> s2.institution.compareTo(s1.institution)).collect(Collectors.groupingBy(i->i.institution,Collectors.summingInt(i->i.participants)));
    }

    //2
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        return courses.stream().collect(Collectors.groupingBy(i-> format("%s-%s",i.institution.toString(),i.subject.toString()),Collectors.summingInt(i->i.participants)))
            .entrySet().stream().sorted((e1,e2)-> {if(e2.getValue().compareTo(e1.getValue())>0) return 1;
                                                    else if(e2.getValue().equals(e1.getValue())) return e2.getKey().compareTo(e1.getKey());
                                                    else return -1;})
            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1, e2) -> e1, LinkedHashMap::new));
    }
    //3
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String, List<List<String>>> instruct = new TreeMap<>(String::compareTo);
        courses.stream().sorted(Comparator.comparing(c -> c.title)).forEach(e->{
            String[] insList = e.instructors.split(", ",0);
            Arrays.stream(insList).forEach(ins ->{
                if(!instruct.containsKey(ins)){
                    ArrayList<List<String>> tmp = new ArrayList<>();
                    tmp.add(new ArrayList<>());
                    tmp.add(new ArrayList<>());
                    instruct.put(ins,tmp);
                }
                List<List<String>> courseList = instruct.get(ins);
                if(insList.length == 1){
                    if(!courseList.get(0).contains(e.title)){
                        courseList.get(0).add(e.title);
                    }
                } else {
                    if(!courseList.get(1).contains(e.title)){
                        courseList.get(1).add(e.title);
                    }
                }
                instruct.replace(ins,courseList);
            });
        });
        return instruct;
    }

    //4
    public List<String> getCourses(int topK, String by) {
        Comparator<Course> cmp = null;

        if(by.equals("hours")){
            cmp = ((c1, c2) -> {
                if(c1.totalHours < c2.totalHours) return 1;
                else if(c1.totalHours == c2.totalHours) return c2.title.compareTo(c1.title);
                else return -1;
            });
        } else {
            cmp = ((c1, c2) -> {
                if(c1.participants < c2.participants) return 1;
                else if(c1.participants == c2.participants) return c2.title.compareTo(c1.title);
                else return -1;
            });
        }

        return courses.stream().sorted(cmp).map(e->e.title).distinct().collect(Collectors.toList()).subList(0,topK);
    }

    //5
    public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
        return courses.stream().filter(e->{
            return e.subject.toLowerCase().contains(courseSubject.toLowerCase())
                && e.percentAudited >= percentAudited
                && e.totalHours <= totalCourseHours;
        }).map(e->e.title).distinct().sorted().toList();
    }

    //6
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        List<String> courseList = new ArrayList<>();
        Map<String,String> courseName = courses.stream().
            collect(Collectors.groupingBy(i->i.number,
                                            Collectors.collectingAndThen(
                                                Collectors.maxBy(Comparator.comparing(i->i.launchDate)),c->c.map(i->i.title).orElse("")
                                            )));
        courses.stream()
            .collect(Collectors.groupingBy(i->i.number,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> Arrays.asList(
                        list.stream().mapToDouble(i->i.medianAge).average().orElse(0), // age
                        list.stream().mapToDouble(i->i.percentMale).average().orElse(0), // male
                        list.stream().mapToDouble(i->i.percentDegree).average().orElse(0) // bachelor
                    )
                )
            )).entrySet().stream().sorted((e1,e2) ->{
                double age1 = e1.getValue().get(0), age2 = e2.getValue().get(0);
                double male1 = e1.getValue().get(1), male2 = e2.getValue().get(1);
                double degree1 = e1.getValue().get(2), degree2 = e2.getValue().get(2);
                double s1 = Math.pow((age - age1),2) + Math.pow((gender * 100 - male1),2) + Math.pow((isBachelorOrHigher * 100 - degree1),2),
                        s2 = Math.pow((age - age2),2) + Math.pow((gender * 100 - male2),2) + Math.pow((isBachelorOrHigher * 100 - degree2),2);
                return Double.compare(s1, s2);
            }).map(i->i.getKey().toString()).distinct().toList()
            .forEach(i->{
                courseList.add(courseName.get(i));
            });

        return courseList.stream().distinct().toList().subList(0,10);
    }

}
class Course {
    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;

    public Course(String institution, String number, Date launchDate,
        String title, String instructors, String subject,
        int year, int honorCode, int participants,
        int audited, int certified, double percentAudited,
        double percentCertified, double percentCertified50,
        double percentVideo, double percentForum, double gradeHigherZero,
        double totalHours, double medianHoursCertification,
        double medianAge, double percentMale, double percentFemale,
        double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) title = title.substring(1);
        if (title.endsWith("\"")) title = title.substring(0, title.length() - 1);
        this.title = title;
        if (instructors.startsWith("\"")) instructors = instructors.substring(1);
        if (instructors.endsWith("\"")) instructors = instructors.substring(0, instructors.length() - 1);
        this.instructors = instructors;
        if (subject.startsWith("\"")) subject = subject.substring(1);
        if (subject.endsWith("\"")) subject = subject.substring(0, subject.length() - 1);
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }
}