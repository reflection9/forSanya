package ru.itis.models;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {
    private Long id;
    private Long userId;
    private Long titleId;
    private int rating;
}
