package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class BadfonModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//div[@class='wal2']/a[1]/@href")
    public String imgUrl;
    @SField(xpath = "//div[@itemprop='description']/a/text()")
    public List<String> tags;
    public String imgW;
    public String imgH;
    public String size;
    @SField(xpath = "//div[@class='grid_3']/div[2]/div/a[last()]/text()")
    public String fav;
    public String views;
    public String thumbnail;
    public String thumbnailW;
    public String thumbnailH;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
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

    public WallPaperResultModel cover() {
        return new WallPaperResultModel(imgWrapUrl, imgUrl, tags, fav, views);
    }
}
