package com.gmail.psyh2409;

public class Main {

    public static void main(String[] args) {
        CourseJDBC courseJDBC = new CourseJDBC();
        System.out.println(courseJDBC.getReportDTOById(365));
        courseJDBC.updateDTObyId(new ReportDTO(365, "30.11.2019", 25.00));
        System.out.println(courseJDBC.getReportDTOById(365));
        courseJDBC.deleteReportDTOById(365);
        for (ReportDTO dto: courseJDBC.getAllReportDTO()) {
            System.out.println(dto);
        }
        System.out.println();
        System.out.println(courseJDBC.getMAX());
        System.out.println(courseJDBC.getAVG());
        System.out.println(courseJDBC.getMIN());
    }
}

