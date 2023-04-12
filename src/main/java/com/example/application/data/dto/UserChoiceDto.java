package com.example.application.data.dto;

import com.example.application.data.entity.Meal;
import com.example.application.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserChoiceDto {
    private LocalDateTime eventDateTime;
    private LocalDateTime deadlineDateTime;

    private User participant;
    private User organizer;

    private String eventName;
    private String eventDescription;

    private String menuName;
    private Long menuId;

    private List<String> potentialMealName;

    private Boolean confirmed;

    private String chosenMealName;
    private String notesForOrganizer;
}
