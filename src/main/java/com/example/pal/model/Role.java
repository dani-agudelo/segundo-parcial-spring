package com.example.pal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;


import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


	@NotBlank(message="El nombre del rol no puede estar vacío")
	@Pattern(regexp="ADMIN|INSTRUCTOR|ESTUDIANTE",message="El rol debe ser 'ADMIN', 'INSTRUCTOR' o 'ESTUDIANTE'")
    @Column(nullable = false, unique = true)
    private String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<User> users;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Role role = (Role) o;
    return Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
