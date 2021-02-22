package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class WallpaperMaidenModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//div[@class='wpPSCList']/ul/li/a/@href")
    public String imgUrl;
    public List<String> tags;
    @SField(xpath = "//div[@class='wpPSCList']/ul/li/a/text()")
    public String imgW;
    public String imgH;
    @SField(xpath = "//div[@class='wpInformationList']/div[3]/div[@class='wpInformationInfo']/div[@class='wpInformationButton']/a/text()")
    public String size;
    public String fav;
    @SField(xpath = "//div[@class='wpInformationList']/div[5]/div[@class='wpInformationInfo']/div[@class='wpInformationButton']/a/text()")
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
