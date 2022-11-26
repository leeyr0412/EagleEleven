package univ.yonsei.eagle_eleven;

public class Emblem {
    private String imageUrl;

    Emblem(){}

    public Emblem(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
