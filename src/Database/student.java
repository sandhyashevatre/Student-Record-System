package com.model;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Component
@Data
public class student {
  private int id;
  private String name;
  @Min(value = 0) private int score;
  private int rank;  
}
