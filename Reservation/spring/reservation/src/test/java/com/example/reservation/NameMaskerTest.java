package com.example.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.reservation.Facilitys.NameMasker;

class NameMaskerTest {

    @DisplayName("예약자명 마스킹 규칙")
    @Test
    void maskName() {
        assertEquals("*", NameMasker.mask("박"));
        assertEquals("박*", NameMasker.mask("박경"));
        assertEquals("박*택", NameMasker.mask("박경택"));
        assertEquals("남**동", NameMasker.mask("남궁길동"));
        assertEquals("J**n", NameMasker.mask("John"));
    }

    @DisplayName("이름이 비어 있으면 빈 문자열")
    @Test
    void maskEmpty() {
        assertEquals("", NameMasker.mask(null));
        assertEquals("", NameMasker.mask(""));
        assertEquals("", NameMasker.mask("   "));
    }
}
