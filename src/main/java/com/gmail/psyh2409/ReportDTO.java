package com.gmail.psyh2409;

import java.util.Objects;

public class ReportDTO {
    private int id;
    private String date;
    private double courseUSD;

    public ReportDTO() {
        super();
    }

    public ReportDTO(String date, double courseUSD) {
        this.date = date;
        this.courseUSD = courseUSD;
    }

    public ReportDTO(int id, String date, double courseUSD) {
        this.id = id;
        this.date = date;
        this.courseUSD = courseUSD;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCourseUSD() {
        return courseUSD;
    }

    public void setCourseUSD(double courseUSD) {
        this.courseUSD = courseUSD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDTO reportDTO = (ReportDTO) o;
        return id == reportDTO.id &&
                Double.compare(reportDTO.courseUSD, courseUSD) == 0 &&
                Objects.equals(date, reportDTO.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, courseUSD);
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", courseUSD=" + courseUSD +
                '}';
    }
}
