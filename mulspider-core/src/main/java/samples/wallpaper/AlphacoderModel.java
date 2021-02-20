package samples.wallpaper;

import com.example.core.annotation.SField;

import java.io.Serializable;
import java.util.List;

public class AlphacoderModel implements Serializable {
    private static final long serialVersionUID = 673293417482457120L;

    public String imgWrapUrl;
    @SField(xpath = "//*[@id=\"page_container\"]/div[@class='center img-container-desktop']/a//img/@src")
    public String imgUrl;
    @SField(xpath = "//*[@id=\"list_tags\"]/div[@class='tag-element']/a/text()")
    public List<String> tags;
    @SField(xpath = "//*[@id=\"page_container\"]/div[@class='center img-container-desktop']/a//img/@width")
    public String imgW;
    @SField(xpath = "//*[@id=\"page_container\"]/div[@class='center img-container-desktop']/a//img/@height")
    public String imgH;
    @SField(xpath = "//*[@id='wallpaper_info_table']/tbody/tr/td/span/span[last()]/text()")
    public List<String> sizeContent;
    public String size;
    @SField(xpath = "//*[@id=\"submitter-info\"]/div/div/div/div/div/h3[@title='Favorites']/text()")
    public String fav;
    @SField(xpath = "//*[@id=\"submitter-info\"]/div/div/div/div/div/h3[@title='Views']/text()")
    public String views;
    @SField(xpath = "//*[@id=\"wallpaper_info_table\"]/tbody/tr/td[@class='colors-container']/a/@style")
    public List<String> colors;
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
                ", sizeContent='" + sizeContent + '\'' +
                ", size='" + size + '\'' +
                ", fav='" + fav + '\'' +
                ", views='" + views + '\'' +
                ", colors='" + colors + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", thumbnailW='" + thumbnailW + '\'' +
                ", thumbnailH='" + thumbnailH + '\'' +
                '}';
    }
}
