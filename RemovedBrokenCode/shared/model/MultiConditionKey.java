package ru.imagebook.shared.model;

/**
 * @author M. Bogovic
 * @since 01.06.2016
 */
public final class MultiConditionKey {
    private final int type;
    private final int productNumber;
    private final int pageLamination;
    private final int coverLamination;
    private final int pageCount;

    public MultiConditionKey(int type, int productNumber, int pageLamination, int coverLamination, int pageCount) {
        this.type = type;
        this.productNumber = productNumber;
        this.pageLamination = pageLamination;
        this.coverLamination = coverLamination;
        this.pageCount = pageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultiConditionKey that = (MultiConditionKey) o;
        return type == that.type
            && productNumber == that.productNumber
            && pageLamination == that.pageLamination
            && coverLamination == that.coverLamination
            && pageCount == that.pageCount;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + productNumber;
        result = 31 * result + pageLamination;
        result = 31 * result + coverLamination;
        result = 31 * result + pageCount;
        return result;
    }
}
