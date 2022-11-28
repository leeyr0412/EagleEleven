package univ.yonsei.eagle_eleven.Model;

public class Team {
    private String TeamName;
    private String CaptainName;
    private int GameNum;
    private String EmblemUrl;
    private int TeamNum;

    public Team(String teamName, String captainName, int gameNum, String EmblemUrl, int teamNum) {
        this.TeamName = teamName;
        this.CaptainName = captainName;
        this.GameNum = gameNum;
        this.EmblemUrl = EmblemUrl;
        this.TeamNum = teamNum;
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

    public int getTeamNum() {
        return TeamNum;
    }

    public void setTeamNum(int teamNum) {
        TeamNum = teamNum;
    }
}
