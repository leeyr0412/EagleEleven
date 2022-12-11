package univ.yonsei.eagle_eleven.Model;

public class Team {
    private String TeamName;
    private String CaptainName;
    private int GameNum;
    private String EmblemUrl;
    private String TeamNumber;

    public Team(String teamName, String captainName,  String EmblemUrl, String teamNumber, int GameNum) {
        this.TeamName = teamName;
        this.CaptainName = captainName;
        this.GameNum = GameNum;
        this.EmblemUrl = EmblemUrl;
        this.TeamNumber = teamNumber;
    }

    public Team() {
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getCaptainName() {
        return CaptainName;
    }

    public int getGameNum() {
        return GameNum;
    }

    public String getEmblemUrl() {
        return EmblemUrl;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public void setCaptainName(String captainName) {
        CaptainName = captainName;
    }

    public void setGameNum(int gameNum) {
        GameNum = gameNum;
    }

    public void setEmblemUrl(String EmblemUrl) {
        EmblemUrl = EmblemUrl;
    }

    public String getTeamNum() {
        return TeamNumber;
    }

    public void setTeamNum(String teamNumber) {
        TeamNumber = teamNumber;
    }
}
