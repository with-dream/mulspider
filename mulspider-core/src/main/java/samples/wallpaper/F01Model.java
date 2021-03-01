package samples.wallpaper;

import java.io.Serializable;
import java.util.List;

public class F01Model implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    public String imgUrl;
    public List<String> tags;
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
