package com.christianoette.demo.hibernatesearch.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "movie")
@ToString
@Indexed // <- Important to add this one
@FieldNameConstants
public class Movie {

    @Id // <- Is also used by Hibernate search
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    @Field(store = Store.NO)
    private String title;

    @Column(name = "story_line")
    @Field(store = Store.NO)
    private String storyLine;
}

