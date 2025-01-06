package ru.itis.models;

import ru.itis.helper.ReadingStatus;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserTitle {
    private Long userId;
    private Long titleId;
    private ReadingStatus status;
}