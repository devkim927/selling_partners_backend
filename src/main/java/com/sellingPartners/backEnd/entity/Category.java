package com.sellingPartners.backEnd.entity;

public enum Category {
    IT_SOFTWARE("IT/소프트웨어"),
    DESIGN_CREATIVE("디자인/영상/콘텐츠"),
    MANUFACTURING("제조/기계/부품"),
    CONSTRUCTION("건축/토목/도시개발"),
    CONSULTING("경영/컨설팅"),
    EDUCATION("교육/강의"),
    HEALTHCARE("헬스케어/의료"),
    MARKETING_SALES("마케팅/영업"),
    DISTRIBUTION_TRADE("유통/무역"),
    ENVIRONMENT_ENERGY("환경/에너지"),
    SAFETY_MANAGEMENT("안전/방재/시설관리");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
