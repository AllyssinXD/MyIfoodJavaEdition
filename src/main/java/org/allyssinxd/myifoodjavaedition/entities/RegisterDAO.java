package org.allyssinxd.myifoodjavaedition.entities;

import javafx.util.converter.LocalTimeStringConverter;
import org.allyssinxd.myifoodjavaedition.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterDAO {

    private final LocalDate date;
    private final float value;
    private final LocalTime start;
    private final LocalTime end;
    private final String average;
    private final int accepted;
    private final int denied;

    public RegisterDAO(LocalDate date, float value, LocalTime start, LocalTime end, String average, int accepted, int denied){
        this.date = date;
        this.value = value;
        this.start = start;
        this.end = end;
        this.average = average;
        this.accepted = accepted;
        this.denied = denied;
    }

    public void Create(){
        Connection con = ConnectionFactory.CreateConnection();
        if (con == null) return;

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("INSERT INTO registers (date, value, start, end, averange, accepted, denied)" +
                    " VALUES (?,?,?,?,?,?,?) ");

            ps.setDate(1, java.sql.Date.valueOf(date));
            ps.setFloat(2, value);
            ps.setString(3, start.toString());
            ps.setString(4, end.toString());
            ps.setString(5, average);
            ps.setInt(6, accepted);
            ps.setInt(7, denied);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionFactory.CloseConnection(con);
        }
    }

    public static ArrayList<Register> GetAllByMonth(String month) {
        LocalTimeStringConverter timeConverter = new LocalTimeStringConverter();
        Connection con = ConnectionFactory.CreateConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        HashMap<String, String> toEnglish = getToEnglish();

        try {
            assert con != null;
            ps = con.prepareStatement("SELECT * from registers where DATE_FORMAT(date, '%M') = '" + toEnglish.get(month) + "';");
            rs = ps.executeQuery();

            ArrayList<Register> registers = new ArrayList<>();

            while (rs.next()) {
                Register register = new Register();
                register.setDate(rs.getDate("date").toLocalDate());
                register.setAverage(rs.getString("averange"));
                register.setEnd(timeConverter.fromString(rs.getString("end")));
                register.setStart(timeConverter.fromString(rs.getString("start")));
                register.setValue(rs.getFloat("value"));
                register.setAcceptedRuns(rs.getInt("accepted"));
                register.setDeniedRuns(rs.getInt("denied"));

                registers.add(register);
            }

            return registers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            assert con != null;
            ConnectionFactory.CloseConnection(con);
        }
    }

    private static HashMap<String, String> getToEnglish() {
        HashMap<String, String> toEnglish = new HashMap<>();

        toEnglish.put("Janeiro", "January");
        toEnglish.put("Fevereiro", "February");
        toEnglish.put("Março", "March");
        toEnglish.put("Abril", "April");
        toEnglish.put("Maio", "May");
        toEnglish.put("Junho", "June");
        toEnglish.put("Julho", "July");
        toEnglish.put("Agosto", "August");
        toEnglish.put("Setembro", "September");
        toEnglish.put("Outubro", "October");
        toEnglish.put("Novembro", "November");
        toEnglish.put("Dezembro", "December");
        return toEnglish;
    }
}
