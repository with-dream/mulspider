package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class UnsplashModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    public String imgUrl;
    public List<String> tags;
    @SField(xpath = "//*dl[@class='_3lSAR']/div[last()]/dd/text()")
    public String imgW;
    public String imgH;
    public String size;
    @SField(xpath = "//*dl[@class='_2Mfyb']/div[1]/dd/span[1]/text()")
    public String fav;
    @SField(xpath = "//*dl[@class='_2Mfyb']/div[2]/dd/span[1]/text()")
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
