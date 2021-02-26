package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class WallpaperScraftModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//div[@class='wallpaper-table']/div[@class='wallpaper-table__row']/span[@class='wallpaper-table__cell']/a/@href")
    public String imgUrl;
    public List<String> tags;
    @SField(xpath = "//div[@class='wallpaper-table']/div[@class='wallpaper-table__row']/span[@class='wallpaper-table__cell']/a/text()")
    public String imgW;
    public String imgH;
    public String size;
    public String fav;
    @SField(xpath = "//div[@class='wallpaper-table']/div[2]/span[2]/text()")
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
