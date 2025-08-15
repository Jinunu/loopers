package com.loopers.domain.product;


import java.util.Comparator;

public class ProductSort {
    private final SortField field;
    private final SortDirection direction;

    private ProductSort(SortField field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }


    public static ProductSort of(SortField field, SortDirection direction) {
        if (field.equals(SortField.LATEST)) {
            return new ProductSort(SortField.LATEST, SortDirection.ASC);
        }
        return new ProductSort(field, direction);
    }

    public static ProductSort of(SortField field) {
        return of(field, SortDirection.ASC);
    }

    public Comparator<ProductInfo> getComparator() {
        Comparator<ProductInfo> comparator = switch (field) {
            case PRICE -> Comparator.comparing(ProductInfo::getPrice);
            case LATEST -> Comparator.comparing(ProductInfo::getProductName);
            case LIKES -> Comparator.comparing(ProductInfo::getLikeCount);
        };

        return direction == SortDirection.DESC ? comparator.reversed() : comparator;
    }



    public enum SortField {
        PRICE("price"),
        LATEST("createdAt"),
        LIKES("likeCount");
        private final String value;

        SortField(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SortField fromString(String value) {
            for (SortField field : values()) {
                if (field.value.equalsIgnoreCase(value)) {
                    return field;
                }
            }
            throw new IllegalArgumentException("지원하지 않는 정렬 필드입니다: " + value);
        }
    }

    public enum SortDirection {
        ASC("asc"),
        DESC("desc");

        private final String value;

        SortDirection(String value) {
            this.value = value;
        }

        public static SortDirection fromString(String value) {
            for (SortDirection direction : values()) {
                if (direction.value.equalsIgnoreCase(value)) {
                    return direction;
                }
            }
            throw new IllegalArgumentException("지원하지 않는 정렬 방향입니다: " + value);
        }

        public String getValue() {
            return value;
        }
    }
}
