package ru.itis.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    private Long id;
    private Long titleId;
    private Long userId;
    private String content;
    private Timestamp createdAt;
}
