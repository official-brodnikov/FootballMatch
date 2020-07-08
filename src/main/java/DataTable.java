public class DataTable {
    private String id;
    private String home;
    private String away;
    private String res;
    private String date;

    public DataTable(String id, String home, String away, String res, String date) {
        this.id = id;
        this.home = home;
        this.away = away;
        this.res = res;
        this.date = date;
    }

    public DataTable() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
