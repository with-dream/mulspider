package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class DesktopwallpaperModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//div[@class='main-photo-below']/div[@class='main-photo-below-right']/a/@href")
    public String imgUrl;
    @SField(xpath = "//div[@class='main-photo-above-right']/a/text()")
    public List<String> tags;
    @SField(xpath = "//div[@class='main-photo-below']/div[@class='main-photo-below-right']/a/text()")
    public String imgW;
    public String imgH;
    public String size;
    @SField(xpath = "//div[@class='main-photo-over-left']/div[2]/text()")
    public String fav;
    @SField(xpath = "//div[@class='main-photo-over-left']/div[1]/text()")
    public String views;
    public String thumbnail;
    public String thumbnailW;
    public String thumbnailH;

    public WallPaperResultModel cover() {
        return new WallPaperResultModel(imgWrapUrl, imgUrl, tags, fav, views);
    }

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
