package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class EnterdeskModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(jsoup = "#images_show_zoom > a", jsoupAttr = "href")
    public String imgUrl;
    @SField(jsoup = "a.arc_tags_color0")
    public List<String> tags;
    public String imgW;
    public String imgH;
    public String size;
    @SField(jsoup = "div.arc_like")
    public String fav;
    @SField(jsoup = "div.arc_down")
    public String views;
    public String thumbnail;
    public String thumbnailW;
    public String thumbnailH;

    public WallPaperResultModel cover() {
        return new WallPaperResultModel(imgWrapUrl, imgUrl, tags, fav, views);
    }

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
}
