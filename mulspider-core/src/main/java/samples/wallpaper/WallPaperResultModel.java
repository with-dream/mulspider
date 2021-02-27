package samples.wallpaper;

import com.example.core.annotation.SField;

import java.util.List;

public class WallPaperResultModel {
    private static final long serialVersionUID = 673293417482457120L;

    public WallPaperResultModel(String imgWrapUrl, String imgUrl, List<String> tags, String fav, String views) {
        this.imgWrapUrl = imgWrapUrl;
        this.imgUrl = imgUrl;
        this.tags = tags;
        this.fav = fav;
        this.views = views;
    }

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
        return "WallPaperResultModel{" +
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
