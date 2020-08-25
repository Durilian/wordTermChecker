package pl.durilian.wordTermsChecker.entities;

import lombok.Getter;
import lombok.Setter;
import pl.durilian.wordTermsChecker.utils.ConfigurationManager;

/**
 * Class containing data set describing exam: @cities, @category, @examType
 */
@Getter
@Setter
public class Exam {
    private String[] cities;
    private String category;
    private ExamType examType;

    /**
     * The only constructor for exam
     *
     * @param cities   as array, at least one
     * @param category usually a single letter e.g. "B"
     * @param examType enum "THEORY" or "PRACTICE"
     */
    public Exam(String[] cities, String category, ExamType examType) {
        this.cities = cities;
        this.category = category;
        this.examType = examType;
    }

    public static Exam getExamFromProperties() {
        String[] cities = ConfigurationManager.getTermCheckerPropertyValue("cities").split(",");
        return new Exam(
                cities,
                ConfigurationManager.getTermCheckerPropertyValue("category"),
                ExamType.get(ConfigurationManager.getTermCheckerPropertyValue("examType"))
        );
    }

    public String getExamType() {
        return examType.getExamType();
    }
}
