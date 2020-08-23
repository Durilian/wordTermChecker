package pl.durilian.wordTermsChecker.entities;

import java.util.HashMap;
import java.util.Map;

public enum ExamType {
    PRACTICE("praktyka"),
    THEORY("teoria");

    private static final Map<String, ExamType> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static {
        for (ExamType examType : ExamType.values()) {
            lookup.put(examType.getExamTypeName(), examType);
        }
    }

    private final String examType;

    ExamType(String examType) {
        this.examType = examType;
    }

    //This method can be used for reverse lookup purpose
    public static ExamType get(String examType) {
        return lookup.get(examType);
    }

    public String getExamTypeName() {
        return examType;
    }
}
