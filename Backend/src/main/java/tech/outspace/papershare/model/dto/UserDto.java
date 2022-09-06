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
    private String id;
    private String email;
    private String name;
    private ERole role;
    private String motto;
    private EUserStatus status;
    private LocalDateTime signInTime;
    private String jwt;
}
