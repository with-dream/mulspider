package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class PexelsModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//img[@class='js-photo-page-image-img']/@src")
    public String imgUrl;
    public List<String> tags;
    @SField(xpath = "//table[@class='photo-page__info__additional-information__table']/tbody/tr[3]/td[2]/text()")
    public String imgW;
    public String imgH;
    @SField(xpath = "//table[@class='photo-page__info__additional-information__table']/tbody/tr[2]/td[2]/text()")
    public String size;
    @SField(xpath = "//div[@class='rd__card__section']/div[@class='photo-page__info__statistics__view-avatar-container__icons']/div[2]/div/text()")
    public String fav;
    @SField(xpath = "//div[@class='photo-page__info__statistics__view-avatar-container__statistics__title']/text()")
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
