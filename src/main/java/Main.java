import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.*;
import java.lang.ref.PhantomReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final String Url = "jdbc:mysql://localhost:3306/footballmatch"+
            "?verifyServerCertificate=false"+
            "&useSSL=false"+
            "&requireSSL=false"+
            "&useLegacyDatetimeCode=false"+
            "&amp"+
            "&serverTimezone=UTC";
    private static final String Password = "root";
    private static final String User = "root";

    public static void main(String[] args) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("https://api-football-v1.p.mashape.com/fixtures/team/52")
                .header("X-Mashape-Key", "U57s2FlC5Dmsh10KZWqUCQPI6YWBp14rzuKjsnX9eQGjv6SSCw")
                .header("Accept", "application/json")
                .asJson();
        /*HttpResponse<JsonNode> response = Unirest.get("https://api-football-v1.p.mashape.com/teams/team/52")
                .header("X-Mashape-Key", "U57s2FlC5Dmsh10KZWqUCQPI6YWBp14rzuKjsnX9eQGjv6SSCw")
                .header("Accept", "application/json")
                .asJson();*/
        //копируем результат запроса в строку
        System.out.println(response.getHeaders().toString());
        String str = response.getBody().toString();
        //
        str = str.replace("\"fixtures\":{", "\"fixtures\":[");
        str = str.replace("}}}}", "}]}}");



        //System.out.println(str);

        //десериализуем с помощью pojo
        Gson gson = new Gson();
        Match match = gson.fromJson(str, Match.class);
        try {
            FileWriter fw = new FileWriter("f:\\FootballMatch\\Text\\Text3.txt");
            fw.write(str);
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        try {

            //устанавливаем соединение с базой данных
            Connection connection = DriverManager.getConnection(Url, User, Password);
            PreparedStatement preparedStatement;
            List<Fixtures> matchInfoList = match.api.fixtures;

            //заполняем из классов в базу данных
            for (int i = 0; i < matchInfoList.size(); i++) {
                String query = "insert into footballmatch.matches values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, matchInfoList.get(i).fixture_id);
                preparedStatement.setString(2, matchInfoList.get(i).homeTeam);
                preparedStatement.setString(3, matchInfoList.get(i).awayTeam);
                preparedStatement.setString(4, matchInfoList.get(i).halftime_score);
                preparedStatement.setString(5, matchInfoList.get(i).final_score);
                preparedStatement.setString(6, matchInfoList.get(i).round);
                preparedStatement.setString(7, matchInfoList.get(i).event_date);
                preparedStatement.setString(8, matchInfoList.get(i).penalty);
                preparedStatement.setString(9, matchInfoList.get(i).elapsed);
                preparedStatement.setString(10, matchInfoList.get(i).status);
                preparedStatement.execute();
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
