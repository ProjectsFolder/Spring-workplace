package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  private Long rate;

  private Integer active = 0;

  public Task() { }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getRate() {
    return rate;
  }

  public void setRate(Long rate) {
    this.rate = rate;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }
}
