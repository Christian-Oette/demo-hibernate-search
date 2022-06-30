package com.christianoette.demo.hibernatesearch;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.util.AnalyzerUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AnalyzerTest {

    private static final String storyLine = """
    %32 2341 $$-_
    +Shortly _after /the end of his second year at Hogwarts, Harry Potter spends another dissatisfying summer with the Dursleys.     
    On his thirteenth birthday, Vernon's sister Marge arrives for a visit, during which she viciously insults Harry and his parents, 
    angering Harry, who causes her to inflate and float away. Expecting to be expelled for using magic outside of Hogwarts, 
    Harry flees the Dursleys with his belongings.
    """;


    // Testing the result of the standard analyzer
    @Test
    public void displayAnalyzer() throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        AnalyzerUtils.displayTokens(analyzer, "storyLine", storyLine);
    }
}
