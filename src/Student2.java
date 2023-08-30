package com.model;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Component
@Data
public class Student2 {
  private int id;
  private String name;
  @Min(value = 0) private int score;
  private int rank;  
}
