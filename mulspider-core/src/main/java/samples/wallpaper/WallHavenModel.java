package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class WallHavenModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//*[@id=\"wallpaper\"]/@src")
    public String imgUrl;
    @SField(xpath = "//*[@id=\"tags\"]/li/a/text()")
    public List<String> tags;
    @SField(xpath = "//*[@id=\"showcase-sidebar\"]/div/div[1]/h3/text()")
    public String imgW;
    public String imgH;
    @SField(xpath = "//*[@id=\"showcase-sidebar\"]/div/div[1]/div[2]/dl/dd[4]/text()")
    public String size;
    @SField(xpath = "//*[@id=\"showcase-sidebar\"]/div/div[1]/div[2]/dl/dd[5]/text()")
    public String fav;
    @SField(xpath = "//*[@id=\"showcase-sidebar\"]/div/div[1]/div[2]/dl/dd[6]/a/text()")
    public String views;
    public String thumbnail;
    public String thumbnailW;
    public String thumbnailH;

    @Override
    public String toString() {
        return "WP10Model{" +
                "imgWrapUrl='" + imgWrapUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", tags=" + tags +
                ", imgW='" + imgW + '\'' +
                ", imgH='" + imgH + '\'' +
                ", size='" + size + '\'' +
                ", fav='" + fav + '\'' +
                ", views='" + views + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", thumbnailW='" + thumbnailW + '\'' +
                ", thumbnailH='" + thumbnailH + '\'' +
                '}';
    }
}
