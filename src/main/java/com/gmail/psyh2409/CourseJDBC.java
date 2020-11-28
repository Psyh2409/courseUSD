package com.gmail.psyh2409;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CourseJDBC {
    private Connection connection;
    private ReportDTO dto;

    public CourseJDBC() {
        super();
        try {
            Class.forName(Config.getMyProp().getProperty("cfn"));
            connection = DriverManager.getConnection(
                    Config.getMyProp().getProperty("url"),
                    Config.getMyProp().getProperty("user"),
                    Config.getMyProp().getProperty("password"));
            addAllReportsForLastYearToMySQLCourseusddbCourse();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addAllReportsForLastYearToMySQLCourseusddbCourse() throws IOException {
        List<Report> reportList = getReportsForLastYear();
        List<ReportDTO> dtoList = new ArrayList<>();
        for (Report r: reportList) {
            dtoList.add(new ReportDTO(r.getExchangedate(), r.getRate()));
        }
        initDB();
        for (ReportDTO dto: dtoList) {
            addDTO(dto);
        }
    }

    private List<Report> getReportsForLastYear() throws IOException {
        List<Report> reportList = new ArrayList<>();
        LocalDate start = LocalDate.now();
        LocalDate finish = start.minusYears(1);
        for (; start.isAfter(finish); start=start.minusDays(1)) {
            reportList.add(Jackson.getReport(start.toString().replaceAll("-", "")));
        }
        return reportList;
    }

    public void setDto(ReportDTO dto) {
        this.dto = dto;
    }

    public ReportDTO getDto() {
        return dto;
    }

    public void initDB() {
        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS courseusddb.course");
            st.execute("CREATE TABLE course (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "date VARCHAR(20) NOT NULL, " +
                    "courseUSD DOUBLE(5, 2) NOT NULL)");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addDTO(ReportDTO dto) {
        if (dto == null) {
            throw new NullPointerException();
        }
        this.dto = dto;
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO courseusddb.course (date, courseUSD) " +
                            "VALUES (?,?)");
            NumberFormat nf = new DecimalFormat("#0.00");
            ps.setString(1, dto.getDate());
            ps.setDouble(2, dto.getCourseUSD());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ReportDTO getReportDTOById(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM courseusddb.course WHERE id=?");
            ps.setInt(1, id);
            ps.executeQuery();
            ResultSet resultSet = ps.getResultSet();
            resultSet.next();
            return new ReportDTO(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void updateDTObyId(ReportDTO dto) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE courseusddb.course SET date = ?, courseusd = ? WHERE id = ?")) {
            ps.setString(1, dto.getDate());
            ps.setDouble(2, dto.getCourseUSD());
            ps.setInt(3, dto.getId());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<ReportDTO> getAllReportDTO(){
        List<ReportDTO> dtoList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            statement.execute("SELECT * FROM courseusddb.course");
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                dtoList.add(new ReportDTO(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
            return dtoList;
        }catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    public void deleteReportDTOById(int id){
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM courseusddb.course WHERE id = ?")){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ReportDTO getMAX() {
        try (Statement statement = connection.createStatement()){
            statement.execute("select * from course where courseusd=(select MAX(courseusd) from course)");
            ResultSet rs = statement.getResultSet();
            rs.next();
            return new ReportDTO(rs.getInt(1), rs.getString(2), rs.getDouble(3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ReportDTO getAVG() {
        try (Statement statement = connection.createStatement()){
            statement.execute(
                    "select * from course " +
                            "where courseusd < ((select AVG(courseusd) from course)+0.02) " +
                            "and courseusd > ((select AVG(courseusd) from course)-0.02)");
            ResultSet rs = statement.getResultSet();
            rs.next();
            return new ReportDTO(rs.getInt(1), rs.getString(2), rs.getDouble(3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }public ReportDTO getMIN() {
        try (Statement statement = connection.createStatement()){
            statement.execute("select * from course where courseusd=(select MIN(courseusd) from course)");
            ResultSet rs = statement.getResultSet();
            rs.next();
            return new ReportDTO(rs.getInt(1), rs.getString(2), rs.getDouble(3));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
