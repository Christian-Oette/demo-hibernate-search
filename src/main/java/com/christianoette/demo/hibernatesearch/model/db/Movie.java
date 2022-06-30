package com.christianoette.demo.hibernatesearch.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.LengthFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "movie")
@ToString
@FieldNameConstants
// Hibernate Search annotations
@Indexed // <- Important to add this one
@AnalyzerDef(name = "customAnalyzer", // <- Optional analyzer customization
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), // is deprecated because Hibernate Search 6 does it differently
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = LengthFilterFactory.class, params = {
                        @Parameter(name = "min", value = "3"),
                        @Parameter(name = "max", value = "20")
                })
        })
public class Movie {

    @Id // <- Is also used by Hibernate search
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    @Field(store = Store.NO)
    private String title;

    @Column(name = "story_line")
    @Field(store = Store.NO, analyzer = @Analyzer(definition = "customAnalyzer"))
    private String storyLine;
}

