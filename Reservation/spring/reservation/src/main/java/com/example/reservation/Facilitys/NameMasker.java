package com.example.reservation.Facilitys;

/**
 * 화면에 보여줄 이름을 가린다. (개인정보 노출 최소화)
 * 홍 -> *, 홍길 -> 홍*, 홍길동 -> 홍*동, 남궁길동 -> 남**동
 */
public final class NameMasker {

    private NameMasker() {
    }

    public static String mask(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        String trimmed = name.strip();
        int length = trimmed.codePointCount(0, trimmed.length());
        if (length == 1) {
            return "*";
        }
        int[] chars = trimmed.codePoints().toArray();
        if (length == 2) {
            return new String(chars, 0, 1) + "*";
        }
        return new String(chars, 0, 1)
                + "*".repeat(length - 2)
                + new String(chars, length - 1, 1);
    }
}
