package ru.imagebook.server.model.calc;

import java.util.List;
import java.util.Map;

// TODO Builder/lombook?
public class CalcProductDetail extends CalcProduct {
    private final Map<Integer, String> coverLamValues;
    private final Map<Integer, String> pageLamValues;
    private final Integer minPageCount;
    private final Integer maxPageCount;
    private final Integer multiplicity;
    private final String approxProdTime;
    private final String paper;
    private final String blockFormat;
    private final String calcComment;
    private final List<String> images;

    public CalcProductDetail(int id, String name, Map<Integer, String> coverLamValues,
                             Map<Integer, String> pageLamValues, Integer minPageCount, Integer maxPageCount,
                             Integer multiplicity, String approxProdTime, String paper, String blockFormat,
                             String calcComment, List<String> images) {
        super(id, name);
        this.coverLamValues = coverLamValues;
        this.pageLamValues = pageLamValues;
        this.minPageCount = minPageCount;
        this.maxPageCount = maxPageCount;
        this.multiplicity = multiplicity;
        this.approxProdTime = approxProdTime;
        this.paper = paper;
        this.blockFormat = blockFormat;
        this.calcComment = calcComment;
        this.images = images;
    }

    public Map<Integer, String> getCoverLamValues() {
        return coverLamValues;
    }

    public Map<Integer, String> getPageLamValues() {
        return pageLamValues;
    }

    public Integer getMinPageCount() {
        return minPageCount;
    }

    public Integer getMaxPageCount() {
        return maxPageCount;
    }

    public Integer getMultiplicity() {
        return multiplicity;
    }

    public String getApproxProdTime() {
        return approxProdTime;
    }

    public String getPaper() {
        return paper;
    }

    public String getBlockFormat() {
        return blockFormat;
    }

    public String getCalcComment() {
        return calcComment;
    }

    public List<String> getImages() {
        return images;
    }
}
