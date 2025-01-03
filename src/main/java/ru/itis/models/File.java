package ru.itis.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    private Long id;
    private Long titleId;
    private Long chapterId;
    private String filePath;
}
