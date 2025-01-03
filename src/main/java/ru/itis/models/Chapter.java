package ru.itis.models;

import lombok.*;

import java.io.File;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chapter {
    private Long id;
    private String name;
    private Long titleId;
    private int chapterNumber;
    private List<File> files;
}
