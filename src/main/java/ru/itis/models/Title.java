package ru.itis.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Title {
    private Long id;
    private String name;
    private String description;
    private String type;
    private Long authorId;
    private Author author;
    private List<Genre> genres;
    private List<File> files;
    private Double averageRating;
}
