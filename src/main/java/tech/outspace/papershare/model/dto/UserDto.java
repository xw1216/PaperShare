package tech.outspace.papershare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.outspace.papershare.model.enumerate.ERole;
import tech.outspace.papershare.model.enumerate.EUserStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String email;
    private String name;
    private ERole role;
    private EUserStatus status;
    private String orcid;
    private LocalDateTime signUpTime;
    private LocalDateTime signInTime;
    private String jwt;
}
