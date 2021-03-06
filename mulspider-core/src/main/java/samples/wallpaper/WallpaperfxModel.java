package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class WallpaperfxModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//*[@id=\"content\"]/div[@class='col-right']/div[@class='inner']/div[@class='pic-left']/div/span[1]/a/@href")
    public String imgUrl;
    @SField(xpath = "//div[@class='tags']/a/text()")
    public List<String> tags;
    @SField(xpath = "//*[@id=\"content\"]/div[@class='col-right']/div[@class='inner']/div[@class='pic-left']/div/span[1]/a/text()")
    public String imgW;
    public String imgH;
    public String size;
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
